package hama.industries.jackal.block;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.RealitySpike;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class PrimaryRealitySpike extends RealitySpike.RealitySpikeBlock {

    public static final String ID = "primary_reality_spike";

    public PrimaryRealitySpike(Properties props) {
        super(props);
    }

    public static final class BE extends BlockEntity {
        public BE(BlockPos pos, BlockState state) {
            super(JackalMod.BLOCK_ENTITIES.PRS, pos, state);
        }
    }
    
    @Override
    public PrimaryRealitySpike.BE newBlockEntity(BlockPos pos, BlockState state) {
        return new PrimaryRealitySpike.BE(pos, state);
    }
}
