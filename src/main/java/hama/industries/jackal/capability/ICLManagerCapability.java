package hama.industries.jackal.capability;

import java.util.UUID;

import hama.industries.jackal.JackalMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public interface ICLManagerCapability {
    public static final Capability<ICLManagerCapability> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ICLManagerCapability.class);
    }
    
    public void addPrimaryCL(ChunkPos pos);
    public void addSecondaryCL(ChunkPos pos);
    public void removePrimaryCL(ChunkPos pos);
    public void removeSecondaryCL(ChunkPos pos);
    public void removeAllCLs();

    public void addTrigger(UUID id, ChunkPos pos);
    public void removeTrigger(UUID id, ChunkPos pos);
    public void setSelfTrigger(ChunkPos pos, boolean value);

    public ServerLevel getLevel();
}
