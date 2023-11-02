package hama.industries.jackal.logic;

import hama.industries.jackal.JackalMod;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public interface ICLManagerCapability {
    public static final Capability<ICLManagerCapability> CL_MANAGER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public void addPrimaryCL(ChunkPos pos);
    public void addSecondaryCL(ChunkPos pos);
    public void removePrimaryCL(ChunkPos pos);
    public void removeSecondaryCL(ChunkPos pos);

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ICLManagerCapability.class);
    }
}
