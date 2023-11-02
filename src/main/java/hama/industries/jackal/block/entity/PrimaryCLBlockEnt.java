package hama.industries.jackal.block.entity;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.logic.ICLManagerCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;

public final class PrimaryCLBlockEnt extends AbstractCLBlockEnt {

    public PrimaryCLBlockEnt(BlockPos pos, BlockState state) {
        super(JackalMod.BLOCK_ENTITIES.PRIMARY_CL, pos, state);
    }

    @Override
    public boolean isActive(){
        return super.isActive() && getBlockState().getValue(BlockStateProperties.POWERED);
    }
    
    LazyOptional<ICLManagerCapability> manager = LazyOptional.empty();

    @Override
    protected void registerSelf() {
        if(this.level.isClientSide) return;
        if (!manager.isPresent()){
            manager = getLevel().getCapability(ICLManagerCapability.CL_MANAGER_CAPABILITY);
            manager.addListener(m -> {manager = LazyOptional.empty();});
        }
        manager.ifPresent(m -> m.addPrimaryCL(getLevel().getChunkAt(worldPosition).getPos()));
    }

    @Override
    protected void deregisterSelf() {
        if(this.level.isClientSide) return;
        if (!manager.isPresent()) return;
        manager.ifPresent(m -> m.removePrimaryCL(getLevel().getChunkAt(worldPosition).getPos()));
    }
}