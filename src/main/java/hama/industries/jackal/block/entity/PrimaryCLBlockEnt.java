package hama.industries.jackal.block.entity;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class PrimaryCLBlockEnt extends AbstractCLBlockEnt {

    public PrimaryCLBlockEnt(BlockPos pos, BlockState state) {
        super(JackalMod.BLOCK_ENTITIES.PRIMARY_CL, pos, state);
    }
    
    /*
     * PCLBE verifies it is valid and powered, and sends load signal to manager if true.
     */

    @Override
    public boolean isActive(){
        return super.isActive() && getBlockState().getValue(BlockStateProperties.POWERED);
    }    
}