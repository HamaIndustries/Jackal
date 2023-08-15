package hama.industries.jackal;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.lwjgl.system.CallbackI.B;

import hama.industries.jackal.block.PrimaryRealitySpike;
import hama.industries.jackal.block.SecondaryRealitySpike;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;


public final class RealitySpike  {
// RegistryManager manager, String id, Supplier<T> blockSupplier
    public static void registerAll(RegistryManager manager) {
        register(manager, PrimaryRealitySpike.ID, RealitySpikeBlock.makeSupplier(PrimaryRealitySpike::new));
        register(manager, SecondaryRealitySpike.ID, RealitySpikeBlock.makeSupplier(SecondaryRealitySpike::new));
    }

    private RealitySpike(){} // do not instantiate

    public static abstract class RealitySpikeBlock extends Block implements EntityBlock {
        public static <T extends RealitySpikeBlock> Supplier<T> makeSupplier(Function<Properties, T> rsb) {
            return () -> rsb.apply(BlockBehaviour.Properties.of(Material.STONE));
        }

        public static final BooleanProperty POWERED = BooleanProperty.create("powered");

        public RealitySpikeBlock(Properties props) {
           super(props);
           this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(POWERED);
        }
    }

    public static BlockItem makeItem(Block block){
        return new BlockItem(block, new Item.Properties()
            .tab(CreativeModeTab.TAB_SEARCH)
            .tab(CreativeModeTab.TAB_REDSTONE)
        );
    }

    private static <T extends RealitySpikeBlock> void register(RegistryManager manager, String id, Supplier<T> blockSupplier) {
        RegistryObject<T> block = manager.BLOCKS.register(id, blockSupplier);
        manager.BLOCK_ENTITIES.register(id, () -> BlockEntityType.Builder.of(block.get()::newBlockEntity, block.get()).build(null));
        manager.ITEMS.register(id, () -> RealitySpike.makeItem(block.get()));
    }

}
