package hama.industries.jackal.data;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.data.client.ModelProviders;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {

    @SubscribeEvent
    public static void genData(GatherDataEvent event){
        var generator = event.getGenerator();
        var fileHelper = event.getExistingFileHelper();
        if (event.includeClient()){
            generator.addProvider(new ModelProviders.CLBlockStateProvider(generator, JackalMod.MODID, fileHelper));
        } 
        if (event.includeServer()) {
            TagProviders.provideAll(generator, fileHelper);
        }
    }
}
