package hama.industries.jackal.block;

import hama.industries.jackal.block.entity.SecondaryCLBlockEnt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SecondaryCLBlock extends AbstractCLBlock {

    public static final String ID = "secondary_reality_spike";

    public SecondaryCLBlock(Properties props) {
        super(props);
    }

    @Override
    public SecondaryCLBlockEnt newBlockEntity(BlockPos pos, BlockState state) {
        return new SecondaryCLBlockEnt(pos, state);
    }
   
}
