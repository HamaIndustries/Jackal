package hama.industries.jackal;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid=JackalMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryManager {

    public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, JackalMod.MODID);
    public final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JackalMod.MODID);
    public final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JackalMod.MODID);
    
    public <B extends IEventBus> void registerAllModBus(B mod_bus){

        RealitySpike.registerAll(this);

        BLOCK_ENTITIES.register(mod_bus);
        BLOCKS.register(mod_bus);
        ITEMS.register(mod_bus);
    }

    // public <T extends BlockEntity, B extends Block> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block[]> validBlocks){
    //     // blockentitytype cruft hand hurt
    //     return BLOCK_ENTITIES.register(JackalMod.MODID, 
    //         () -> BlockEntityType.Builder.of(supplier, validBlocks.get()).build(null)
    //     );
    // }

}
