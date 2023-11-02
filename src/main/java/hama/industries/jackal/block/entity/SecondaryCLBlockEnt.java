package hama.industries.jackal.block.entity;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.logic.ICLManagerCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public class SecondaryCLBlockEnt extends AbstractCLBlockEnt {
    public SecondaryCLBlockEnt(BlockPos pos, BlockState state) {
        super(JackalMod.BLOCK_ENTITIES.SECONDARY_CL, pos, state);
    }

    LazyOptional<ICLManagerCapability> manager = LazyOptional.empty();

    @Override
    protected void registerSelf() {
        if(this.level.isClientSide) return;
        if (!manager.isPresent()){
            manager = getLevel().getCapability(ICLManagerCapability.CL_MANAGER_CAPABILITY);
            manager.addListener(m -> {manager = LazyOptional.empty();});
        }
        manager.ifPresent(m -> m.addSecondaryCL(getLevel().getChunkAt(worldPosition).getPos()));
    }
    
    @Override
    protected void deregisterSelf() {
        if(this.level.isClientSide) return;
        if (!manager.isPresent()) return;
        manager.ifPresent(m -> m.removeSecondaryCL(getLevel().getChunkAt(worldPosition).getPos()));
    }    
}
