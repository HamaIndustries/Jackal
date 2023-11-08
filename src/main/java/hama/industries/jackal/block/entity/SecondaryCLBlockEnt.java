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
        getManager().ifPresent(m -> m.addSecondaryCL(getChunkPos()));
    }
    
    @Override
    protected void deregisterSelf() {
        if(level.isClientSide || !manager.isPresent()) return;
        getManager().ifPresent(m -> m.removeSecondaryCL(getChunkPos()));
    }

    @Override
    public boolean isActive() {
        return isEnabled();
    }    
}
