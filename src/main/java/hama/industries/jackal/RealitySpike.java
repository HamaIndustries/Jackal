package hama.industries.jackal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import hama.industries.jackal.block.PrimaryRealitySpike;
import hama.industries.jackal.block.SecondaryRealitySpike;
import hama.industries.jackal.lib.JackalTags;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;


public final class RealitySpike {
// RegistryManager manager, String id, Supplier<T> blockSupplier
    public static void registerAll(RegistryManager manager) {
        register(manager, PrimaryRealitySpike.ID, RealitySpikeBlock.supplierOf(PrimaryRealitySpike::new));
        register(manager, SecondaryRealitySpike.ID, RealitySpikeBlock.supplierOf(SecondaryRealitySpike::new));
    }

    private RealitySpike(){}

    @Mod.EventBusSubscriber(modid=JackalMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static abstract class RealitySpikeBlock extends Block implements EntityBlock {

        private static Map<LevelChunk, Set<BlockPos>> chunkCache = new WeakHashMap<>();
        
        public static <T extends RealitySpikeBlock> Supplier<T> supplierOf(Function<Properties, T> rsb) {
            return () -> rsb.apply(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).noOcclusion());            
        }

        public RealitySpikeBlock(Properties props) {
           super(props);
           this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.POWERED, false));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.POWERED, BlockStateProperties.ENABLED);
            // power = has fuel source; enabled = valid configuration
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext ctx){
            return defaultBlockState()
                .setValue(BlockStateProperties.POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()))
                .setValue(BlockStateProperties.ENABLED, false);
        }

        // copy [Minecraft]
        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
            if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
               level.removeBlockEntity(pos);
               removeFromCache(level, pos);
            }
         }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
            if (!oldState.is(JackalTags.BLOCK.REALITY_SPIKES)) updateCache(level, pos);
        }

        @Override
        public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean mov) {
            // called on neighbor block update
            updateState(state, level, pos);
        }

        /* returns true if state was updated. general function for checking if our state should be changed and changing it if so. */
        protected boolean updateState(BlockState state, Level level, BlockPos pos){
            var newState = state;
            var signal = false;
            boolean power = state.getValue(BlockStateProperties.POWERED);
            if (power != level.hasNeighborSignal(pos)) {
                newState = newState.setValue(BlockStateProperties.POWERED, !power);
                signal = !power;
            }
            
            boolean valid = state.getValue(BlockStateProperties.ENABLED);
            if (valid != hasValidConfiguration(level.getChunkAt(pos))) {
                newState = newState.setValue(BlockStateProperties.ENABLED, !valid);
            }

            if (!(state.equals(newState))){
                level.setBlock(pos, state, UPDATE_CLIENTS);
                ((RealitySpikeBlockEntity)level.getBlockEntity(pos)).onRedstoneSignal();
                return true;
            }
            return false;
        }

        /* Checks if this spike is allowed to be powered (only spike in chunk, etc) */
        public static boolean hasValidConfiguration(LevelChunk chunk){
            // save some time in casting by requiring chunk as parameter
            return getSpikeCache(chunk).size() == 1;
        }

        protected void removeFromCache(LevelAccessor level, BlockPos pos){
            var chunk = level.getChunk(pos);
            JackalMod.logger().info(chunk.toString());
            if (chunk instanceof LevelChunk && chunkCache.containsKey((LevelChunk)chunk)) {
                chunkCache.get((LevelChunk)chunk).remove(pos);
                JackalMod.logger().info("Spikes in chunk: " + chunkCache.get((LevelChunk)chunk).size() + (level.isClientSide() ? " client" : " server"));
            }
        }
        protected Set<BlockPos> updateCache(LevelAccessor level, BlockPos pos) {
            var chunk = level.getChunk(pos);
            if (chunk instanceof LevelChunk){
                var cache = getSpikeCache((LevelChunk)chunk);
                cache.add(pos);
                JackalMod.logger().info("Spikes in chunk: " + cache.size());
                return cache;
            }
            return null;
        }
        protected static Set<BlockPos> getSpikeCache(LevelChunk chunk){
            // this could be generalized to a util function for arbitrary tags
            // also could possibly be optimized? not sure if jvm inlines the getTag
            return chunkCache.computeIfAbsent(chunk, c -> 
                chunk.getBlockEntities().values().stream()
                    .filter(be -> be.getBlockState().is(JackalTags.BLOCK.REALITY_SPIKES))
                    .map(be -> be.getBlockPos())
                    .collect(Collectors.toCollection(HashSet::new))
            );
        }

        /*
            @Override
            public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos nPos) {
                // called when a nearby tileentity updates (is marked dirty)
                // useful to prevent checking for neighbors every tick
            }
        */
        @SubscribeEvent
        public final static void clientSetup (final FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(JackalMod.BLOCKS.PRS, RenderType.cutout());
        }
	}

    public static abstract class RealitySpikeBlockEntity extends BlockEntity {
        protected RealitySpikeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        public void onRedstoneSignal(){}
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
