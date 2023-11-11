package hama.industries.jackal.block.entity;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SecondaryCLBlockEnt extends AbstractCLBlockEnt {
    public SecondaryCLBlockEnt(BlockPos pos, BlockState state) {
        super(JackalMod.BLOCK_ENTITIES.SECONDARY_CL, pos, state);
    }

    @Override
    protected void registerSelf() {
        if(this.level.isClientSide) return;
        var manager = getManager();
        manager.ifPresent(
            m -> m.addSecondaryCL(getChunkPos())
        );
    }
    
    @Override
    protected void deregisterSelf() {
        var present = manager.isPresent();
        if(level.isClientSide || !present) return;
        getManager().ifPresent(m -> m.removeSecondaryCL(getChunkPos()));
    }

    @Override
    public boolean isActive() {
        return isEnabled();
    }    
}
