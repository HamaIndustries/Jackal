package hama.industries.jackal.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import com.mojang.datafixers.types.templates.Tag.TagType;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;
import net.minecraft.nbt.TagVisitor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfig.Server;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.common.world.ForgeChunkManager.TicketHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CLManager implements ICLManagerCapability, INBTSerializable<CompoundTag> {
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

        var provider = new ICapabilitySerializable<CompoundTag>() { 
            // https://forge.gemwire.uk/wiki/Capabilities/1.18#Attaching_a_Capability
            
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction direction) {
                if (cap == ICLManagerCapability.CL_MANAGER_CAPABILITY) {
                    return optional.cast();
                }
                return LazyOptional.empty();
            }

            @Override
            public CompoundTag serializeNBT() {
                return manager.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                manager.deserializeNBT(nbt);
            }
        };
        event.addCapability(new ResourceLocation(JackalMod.MODID, RESOURCE_ID), provider);
        event.addListener(optional::invalidate);
    }

    public boolean hasPrimaryCL(UUID id){
        return pcls.values().stream().anyMatch(pcl -> pcl.id == id);
    }

    public static void validateTickets(ServerLevel level, TicketHelper ticketHelper) {
        // var cap = level.getCapability(CL_MANAGER_CAPABILITY);
        // if (!cap.isPresent()) throw new UnsupportedOperationException("chunkloading manager not gettable for world " + level);
        // cap.ifPresent(manager ->
        //     ticketHelper.getEntityTickets().entrySet().stream().forEach(entry -> {
        //         if (!manager.hasPrimaryCL(entry.getKey())){
        //             ticketHelper.removeAllTickets(entry.getKey());
        //         }
        //     }
        // ));
        ticketHelper.getEntityTickets().keySet().stream().forEach(id -> ticketHelper.removeAllTickets(id));
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event){
        var level = event.getWorld();
        if (level instanceof ServerLevel) {
            var cap = ((ServerLevel)level).getCapability(CL_MANAGER_CAPABILITY);
            cap.ifPresent(manager -> {
                manager.removeAllCLs();
            });
        }
    }

    @Override
    public void removeAllCLs(){
        for (var pos : pcls.keySet()){
            removePrimaryCL(pos);
        }
        for (var pos : scls.keySet()){
            removeSecondaryCL(pos);
        }
    }

    @Override
    public void addPrimaryCL(ChunkPos pos) {
        pcls.putIfAbsent(pos, new PrimaryCL());
    }

    @Override
    public void addSecondaryCL(ChunkPos pos) {
        scls.putIfAbsent(pos, new SecondaryCL());
        getCLsInRange(pcls, pos)
            .filter(primary -> primary.isActive())
            .forEach(primary -> forceChunk(primary, pos, true));
    }

    @Override
    public void removePrimaryCL(ChunkPos pos) {
        setPrimaryActive(pos, false);
        pcls.remove(pos);
    }

    @Override
    public void removeSecondaryCL(ChunkPos pos) {
        getCLsInRange(pcls, pos)
            .filter(primary -> primary.isActive())
            .forEach(primary -> forceChunk(primary, pos, false));
        scls.remove(pos);
    }

    @Override
    public void setPrimaryActive(ChunkPos pos, boolean active){
        var primary = pcls.get(pos);

        if (primary == null) {
            if (active){
                throw new UnsupportedOperationException("Tried to set a nonexistent primary at " + pos + " to active");
            } else { return; }
        } else if (primary.isActive() == active) { return; }

        primary.setActive(active);
        forceChunk(primary, pos, active);
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

    @Override
    public CompoundTag serializeNBT() {
        var pTags = new ListTag();
        for (var entry : pcls.entrySet()){
            var pt = new CompoundTag();
            pt.putInt("x", entry.getKey().x);
            pt.putInt("z", entry.getKey().z);
            pt.putUUID("id", entry.getValue().id);
            pt.putBoolean("active", entry.getValue().isActive());
            pTags.add(pTags.size(), pt);
        }
        var sTags = new ListTag();// step into this, see why it skips
        for (var entry : scls.entrySet()){
            var st = new CompoundTag();
            st.putInt("x", entry.getKey().x);
            st.putInt("z", entry.getKey().z);
            sTags.add(sTags.size(), st);
        }
        var nbt = new CompoundTag();
        nbt.put("primaries", pTags);
        nbt.put("secondaries", sTags);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getList("secondaries", Tag.TAG_COMPOUND).forEach(pt -> {
            var ptag = (CompoundTag)pt;
            scls.put(new ChunkPos(ptag.getInt("x"), ptag.getInt("z")), new SecondaryCL());
        });

        nbt.getList("primaries", Tag.TAG_COMPOUND).forEach(pt -> {
            var ptag = (CompoundTag)pt;
            var pos = new ChunkPos(ptag.getInt("x"), ptag.getInt("z"));
            pcls.put(pos, new PrimaryCL(ptag.getUUID("id")));
            if (ptag.getBoolean("active")){
                setPrimaryActive(pos, true);
            }
        });
    }

}
