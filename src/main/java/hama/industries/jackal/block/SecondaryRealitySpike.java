package hama.industries.jackal.block;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.RealitySpike;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SecondaryRealitySpike extends RealitySpike.RealitySpikeBlock {

    public static final String ID = "secondary_reality_spike";

    public SecondaryRealitySpike(Properties props) {
        super(props);
    }

    public static final class BE extends RealitySpike.RealitySpikeBlockEntity {
        public BE(BlockPos pos, BlockState state) {
            super(JackalMod.BLOCK_ENTITIES.SRS, pos, state);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BE(pos, state);
    }
   
}
