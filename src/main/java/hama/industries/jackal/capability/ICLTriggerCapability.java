package hama.industries.jackal.capability;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.logic.manager.ITriggerCL;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public interface ICLTriggerCapability  {
    public static final Capability<ICLManagerCapability> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ICLTriggerCapability.class);
    }

    public void addTriggerable(ITriggerCL cl);
    public void removeTriggerable(ITriggerCL cl);
}
