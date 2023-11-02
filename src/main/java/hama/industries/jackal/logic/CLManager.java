package hama.industries.jackal.logic;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CLManager implements ICLManagerCapability{
    public static final String ID = "cl_manager_impl";

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
        event.addCapability(new ResourceLocation(JackalMod.MODID, ID), provider);
        event.addListener(optional::invalidate);
    }

    @Override
    public void addPrimaryCL(ChunkPos pos) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addPrimaryCL'");
    }

    @Override
    public void addSecondaryCL(ChunkPos pos) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addSecondaryCL'");
    }

    @Override
    public void removePrimaryCL(ChunkPos pos) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'removePrimaryCL'");
    }

    @Override
    public void removeSecondaryCL(ChunkPos pos) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'removeSecondaryCL'");
    }
}
