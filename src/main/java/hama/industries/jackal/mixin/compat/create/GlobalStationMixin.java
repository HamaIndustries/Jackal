package hama.industries.jackal.mixin.compat.create;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.capability.ICLManagerCapability;
import hama.industries.jackal.logic.manager.ICLTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mixin(com.simibubi.create.content.trains.station.GlobalStation.class)
public abstract class GlobalStationMixin extends SingleBlockEntityEdgePoint implements ICLTrigger {
    // on load, get level capability and cache.
    // register with level in this chunk with our uuid when train present

    private LazyOptional<ICLManagerCapability> clManager = LazyOptional.empty();
    private void setManager(ServerLevel level){
        if (level != null && !level.isClientSide && !clManager.isPresent())
            clManager = level.getCapability(ICLManagerCapability.TOKEN);
    }

    @Shadow(remap = false) @Nullable
	public abstract Train getPresentTrain();

    public void registerCLTrigger(){// should be called when a train appears
        JackalMod.logger().debug("registering station with id " + this.id);
        if (getBlockEntityPos() != null){
            clManager.ifPresent(manager -> manager.addTrigger(id, new ChunkPos(getBlockEntityPos())));
        } 
    }

    public void removeCLTrigger(){// should be called when train departs or when this station is removed
        JackalMod.logger().debug("removing station with id " + this.id);
        if (getBlockEntityPos() != null){
            clManager.ifPresent(manager -> manager.removeTrigger(id, new ChunkPos(getBlockEntityPos())));
        }
    }

    @Inject(method = "read", at = @At("HEAD"), remap = false)
    public void onRead(CompoundTag nbt, boolean migration, DimensionPalette dimensions, CallbackInfo ci) {
        setManager(ServerLifecycleHooks.getCurrentServer().getLevel(blockEntityDimension));
    }

    @Inject(method = "blockEntityAdded", at = @At("RETURN"), remap = false)
	public void onBlockEntityAdded(BlockEntity blockEntity, boolean front, CallbackInfo ci) {
        if (blockEntity.getLevel() instanceof ServerLevel level){
            setManager(level);
            if (getPresentTrain() != null)
                registerCLTrigger();
        }
    }

    @Inject(method = "blockEntityRemoved", at = @At("HEAD"), remap = false)
	public void onBlockEntityRemoved(BlockPos blockEntityPos, boolean front, CallbackInfo ci) {
        removeCLTrigger();
	}
    
    // Question: does trainDeparted handle all cases? or will
    // derailing while at station lead to incorrect state?

    @Inject(method = "trainDeparted", at = @At("HEAD"), remap = false)
    public void onTrainDeparted(Train train, CallbackInfo ci){
        removeCLTrigger();
    }
}
