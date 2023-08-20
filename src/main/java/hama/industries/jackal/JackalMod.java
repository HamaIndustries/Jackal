package hama.industries.jackal;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JackalMod.MODID)
public class JackalMod
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "jackal";

    public static RegistryManager REGISTRY = new RegistryManager();

    public static final ModObjects.MOD_ITEMS ITEMS = ModObjects.ITEMS;
    public static final ModObjects.MOD_BLOCKS BLOCKS = ModObjects.BLOCKS;
    public static final ModObjects.MOD_BLOCK_ENTITIES BLOCK_ENTITIES = ModObjects.BLOCK_ENTITIES;

    public JackalMod()
    {  
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        REGISTRY.registerAllModBus(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        // LOGGER.info("HELLO FROM PREINIT");
        // LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        // InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        // LOGGER.info("Got IMC {}", event.getIMCStream().
        //         map(m->m.messageSupplier().get()).
        //         collect(Collectors.toList()));
    }

    public static Logger logger(){ return LOGGER; }


}
