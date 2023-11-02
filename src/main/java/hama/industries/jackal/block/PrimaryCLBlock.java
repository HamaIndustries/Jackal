package hama.industries.jackal.block;

import hama.industries.jackal.block.entity.PrimaryCLBlockEnt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public final class PrimaryCLBlock extends AbstractCLBlock {

    public static final String ID = "primary_reality_spike";

    public PrimaryCLBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(BlockStateProperties.ENABLED, false)
            .setValue(BlockStateProperties.POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ENABLED, BlockStateProperties.POWERED);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx){
        return defaultBlockState().setValue(BlockStateProperties.POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean mov) {
        boolean signal = level.hasNeighborSignal(pos);
        if (state.getValue(BlockStateProperties.POWERED) != signal){
            level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, signal));
        }
    }

    @Override
    public PrimaryCLBlockEnt newBlockEntity(BlockPos pos, BlockState state) {
        return new PrimaryCLBlockEnt(pos, state);
    }
}
