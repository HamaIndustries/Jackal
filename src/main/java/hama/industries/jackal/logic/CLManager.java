package hama.industries.jackal.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkTaskPriorityQueue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.common.world.ForgeChunkManager.LoadingValidationCallback;
import net.minecraftforge.common.world.ForgeChunkManager.TicketHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CLManager implements ICLManagerCapability{
    public static final String RESOURCE_ID = "chunk_loader_manager_impl";
    private static final int RANGE = 7;

    private Map<ChunkPos, PrimaryCL> pcls = new HashMap<>();
    private Map<ChunkPos, SecondaryCL> scls = new HashMap<>();

    // private final ForgeChunkManager.TicketTracker<PrimaryCL> tickets = new ForgeChunkManager.TicketTracker<>();

    private ServerLevel level;
    public CLManager (ServerLevel level){
        this.level = level;
    }

    @SubscribeEvent
    public static void attachCapability(final AttachCapabilitiesEvent<Level> event){
        if (! (event.getObject() instanceof ServerLevel) ) return;

        var manager = new CLManager((ServerLevel)event.getObject());
        LazyOptional<ICLManagerCapability> optional = LazyOptional.of(() -> manager);

        ICapabilityProvider provider = new ICapabilityProvider() { 
            // volatile, doesn't save on world load. use serializable to persist.
            // https://forge.gemwire.uk/wiki/Capabilities/1.18#Attaching_a_Capability
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction direction) {
                if (cap == ICLManagerCapability.CL_MANAGER_CAPABILITY) {
                    return optional.cast();
                }
                return LazyOptional.empty();
            }
        };
        event.addCapability(new ResourceLocation(JackalMod.MODID, RESOURCE_ID), provider);
        event.addListener(optional::invalidate);
    }

    @Override
    public void addPrimaryCL(ChunkPos pos) {
        System.out.println("add primary");
        pcls.putIfAbsent(pos, new PrimaryCL(this));
    }

    @Override
    public void addSecondaryCL(ChunkPos pos) {
        System.out.println("add sec");
        scls.putIfAbsent(pos, new SecondaryCL());
        getCLsInRange(pcls, pos)
            .filter(primary -> primary.isActive())
            .forEach(primary -> forceChunk(primary, pos, true));
    }

    @Override
    public void removePrimaryCL(ChunkPos pos) {
        System.out.println("remove prim");
        setPrimaryActive(pos, false);
        pcls.remove(pos);
    }

    @Override
    public void removeSecondaryCL(ChunkPos pos) {
        System.out.println("remove sec");
        getCLsInRange(pcls, pos)
            .filter(primary -> primary.isActive())
            .forEach(primary -> forceChunk(primary, pos, false));
        scls.remove(pos);
    }

    @Override
    public void setPrimaryActive(ChunkPos pos, boolean active){
        var primary = pcls.get(pos);
        
        if (primary == null && !active) return; // allow a crash if we try to set an invalid primary active

        primary.setActive(active);
        getCLsInRange(scls, pos)
            .forEach(secondary -> forceChunk(primary, pos, active));
    }

    private void forceChunk(PrimaryCL primary, ChunkPos pos, boolean forced){
        ForgeChunkManager.forceChunk(level, JackalMod.MODID, primary.id, pos.x, pos.z, forced, true);
    }

    private <R> Stream<ChunkPos> getChunksInRange(Map<ChunkPos, R> clMap, ChunkPos pos){
        return clMap.keySet().stream().filter(p -> (p.getChessboardDistance(pos) <= RANGE));
    }

    private <R> Stream<R> getCLsInRange(Map<ChunkPos, R> clMap, ChunkPos pos){
        return getChunksInRange(clMap, pos).map(p -> clMap.get(p));
    }
}
