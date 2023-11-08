package hama.industries.jackal.block.entity;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class PrimaryCLBlockEnt extends AbstractCLBlockEnt {

    private boolean hasTrigger = false;

    public PrimaryCLBlockEnt(BlockPos pos, BlockState state) {
        super(JackalMod.BLOCK_ENTITIES.PRIMARY_CL, pos, state);
    }

    @Override
    public boolean isActive(){
        return isEnabled() && (getBlockState().getValue(BlockStateProperties.POWERED) != hasTrigger);
    }

    @Override
    protected void registerSelf() {
        if(this.level.isClientSide) return;
        getManager().ifPresent(m -> m.addPrimaryCL(getChunkPos()));
    }

    @Override
    protected void deregisterSelf() {
        if(level.isClientSide || !manager.isPresent()) return;
        getManager().ifPresent(m -> m.removePrimaryCL(getChunkPos()));
    }

    public void updateActiveState(){
        getManager().ifPresent(m -> m.setPrimaryActive(getChunkPos(), isActive()));
    }
}