package hama.industries.jackal.block;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.RegistryManager;

@Mod.EventBusSubscriber(modid=JackalMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public abstract class AbstractCLBlock extends Block implements EntityBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 1.0D, 0.0D, 16.0D, 9.0D, 16.0D);

    public static <T extends AbstractCLBlock> Supplier<T> supplierOf(Function<Properties, T> rsb) {
        return () -> rsb.apply(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).noOcclusion().dynamicShape());            
    }

    @SubscribeEvent
    public final static void clientSetup (final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(JackalMod.BLOCKS.PRIMARY_CL, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(JackalMod.BLOCKS.SECONDARY_CL, RenderType.cutout());
    }	

    public AbstractCLBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.ENABLED, false));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ENABLED);
    }

    // copy [Minecraft]
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) || !newState.hasBlockEntity()) {
            level.removeBlockEntity(pos);
        }
    }

    /*
        @Override
        public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos nPos) {
            // called when a nearby BE updates (is marked dirty)
            // useful to prevent checking for neighbors every tick
        }
    */

    // General CL registration handling endpoint --------------------------------------
    public static void registerAll(RegistryManager manager) {
        register(manager, PrimaryCLBlock.ID, supplierOf(PrimaryCLBlock::new));
        register(manager, SecondaryCLBlock.ID, supplierOf(SecondaryCLBlock::new));
    } 
    
    public static BlockItem makeItem(Block block){
        return new BlockItem(block, new Item.Properties()
            .tab(CreativeModeTab.TAB_SEARCH)
            .tab(CreativeModeTab.TAB_REDSTONE)
        );
    }
    
    private static <T extends AbstractCLBlock> void register(RegistryManager manager, String id, Supplier<T> blockSupplier) {
        RegistryObject<T> block = manager.BLOCKS.register(id, blockSupplier);
        manager.BLOCK_ENTITIES.register(id, () -> BlockEntityType.Builder.of(block.get()::newBlockEntity, block.get()).build(null));
        manager.ITEMS.register(id, () -> makeItem(block.get()));
    }
}
