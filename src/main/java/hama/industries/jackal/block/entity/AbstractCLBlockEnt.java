package hama.industries.jackal.block.entity;

import java.util.HashSet;

import hama.industries.jackal.block.AbstractCLBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractCLBlockEnt extends BlockEntity {
    protected AbstractCLBlockEnt(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected HashSet<AbstractCLBlockEnt> cache; // shared by all others in chunk

    // /* If valid, updates its blockstate to match. Otherwise, updates itself and all neighbors */
    public void updateNeighborhood(){
        if (cache == null){
            var level = getLevel();
            if (!this.level.isClientSide) {
                for (var be : level.getChunkAt(worldPosition).getBlockEntities().values()){
                    if (be.getBlockState().getBlock() instanceof AbstractCLBlock 
                        && be != this
                        && ((AbstractCLBlockEnt)be).cache != null
                    ){
                        cache = ((AbstractCLBlockEnt)be).cache;
                        break;
                    }
                }
                if (cache == null){
                    cache = new HashSet<>();
                }
                cache.add(this);
            }
        }

        if (cache.size() == 1){
            setEnabled(true);
        } else {
            for (var cl : cache){
                cl.setEnabled(false);
            }
        }
    }

    @Override
    public void onLoad(){
        if (!this.level.isClientSide) {
            updateNeighborhood();
        }
    }

    @Override
    public void setRemoved(){
        if (!this.level.isClientSide) {
            cache.remove(this);
            for (var other : cache){
                other.updateNeighborhood();
                return;
            }
            // if no others, this is the main CL, so we want to deregister
            deregisterSelf();
        }
        super.setRemoved();
    }

    private void setEnabled(boolean enabled){
        var level = getLevel();
        if (!this.level.isClientSide && !this.isRemoved() && getBlockState().getValue(BlockStateProperties.ENABLED) != enabled){
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.ENABLED, enabled));
        }

        if (enabled) {
            registerSelf();
        } else {
            deregisterSelf();
        }
    }

    // register with relevant manager that we control this chunk
    protected abstract void registerSelf();
    protected abstract void deregisterSelf();

    /* 
        Check whether we are in a valid active state according to our CL's rules.

        Rules for all CLs:
            - This is the only CL in the chunk
     */
    public boolean isEnabled(){
        return getBlockState().getValue(BlockStateProperties.ENABLED);
    }

    public abstract boolean isActive(); // active = allowed to force load if prompted
}