// package hama.industries.jackal.compat.create;

// import javax.annotation.Nullable;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import hama.industries.jackal.block.entity.PrimaryCLBlockEnt;
// import net.minecraft.core.BlockPos;
// import net.minecraft.core.Direction;
// import net.minecraft.server.level.ServerLevel;
// import net.minecraft.world.level.block.entity.BlockEntity;
// import net.minecraft.world.level.block.entity.BlockEntityType;
// import net.minecraft.world.level.block.state.BlockState;

// import com.simibubi.create.content.trains.station.GlobalStation;

// //remmove
// import com.simibubi.create.content.trains.station.StationBlockEntity;

// @Mixin(com.simibubi.create.content.trains.station.StationBlockEntity.class)
// public abstract class StationBlockEntityMixin extends StationBlockEntity {
//     public StationBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {super(p_155228_, p_155229_, p_155230_);}

//     @Shadow @Nullable
// 	public abstract GlobalStation getStation();

//     @Nullable
//     private PrimaryCLBlockEnt findNeighborCL(){
        
//         var world = (ServerLevel)level;
//         for(Direction direction : Direction.values()) {
//             var neighbor = level.getBlockEntity(worldPosition.relative(direction));
//             if (neighbor != null && neighbor instanceof PrimaryCLBlockEnt) {
//                 return (PrimaryCLBlockEnt)neighbor;
//             }
//         }
//         return null;
//     }
    
//     @Inject(method = "onLoad", at = @At("HEAD"))
//     public void tryAttachCLTrigger(CallbackInfo cbInfo) {
        
//         if (!level.isClientSide){
            
//             // grab an adjacent cl
//             var cl = findNeighborCL();
//             if (cl != null){

//                 // get our abstract station
//                 var station = getStation();

//                 // register our trigger callback with the station from cl
//             }
//         }
//     }
// }
