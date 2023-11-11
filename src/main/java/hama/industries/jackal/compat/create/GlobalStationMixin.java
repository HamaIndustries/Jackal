// package hama.industries.jackal.compat.create;

// import javax.annotation.Nullable;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;

// import com.simibubi.create.content.trains.entity.Train;
// import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;

// import net.minecraft.resources.ResourceKey;
// import net.minecraft.server.level.ServerLevel;
// import net.minecraft.world.level.Level;

// @Mixin(com.simibubi.create.content.trains.station.GlobalStation.class)
// public abstract class GlobalStationMixin extends SingleBlockEntityEdgePoint {
//     // on load, get level capability and cache.
//     // register with level in this chunk with our uuid when train present

//     @Shadow
//     public abstract ResourceKey<Level> getBlockEntityDimension();

//     @Shadow @Nullable
// 	public abstract Train getPresentTrain();

//     // public void waw(){
//     //     var dimension = getBlockEntityDimension();

//     //     if (level instanceof ServerLevel){

//     //     }
//     // }
    
// }
