package hama.industries.jackal;

import com.mojang.logging.LogUtils;

import hama.industries.jackal.logic.manager.CLManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.world.ForgeChunkManager;

import org.slf4j.Logger;

@Mod(JackalMod.MODID)
public class JackalMod
{
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
        event.enqueueWork(() -> ForgeChunkManager.setForcedChunkLoadingCallback(JackalMod.MODID, CLManager::validateTickets));
    }

    public static Logger logger(){ return LOGGER; }

}
