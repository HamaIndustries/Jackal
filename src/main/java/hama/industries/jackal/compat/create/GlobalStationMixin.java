// package hama.industries.jackal.compat.create;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.function.Consumer;

// import javax.annotation.Nullable;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;

// import com.simibubi.create.content.trains.entity.Carriage;
// import com.simibubi.create.content.trains.entity.Train;
// import com.simibubi.create.content.trains.graph.TrackGraph;
// import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
// import com.simibubi.create.content.trains.station.GlobalStation;

// import hama.industries.jackal.capability.ICLManagerCapability;
// import net.minecraft.client.Minecraft;
// import net.minecraft.resources.ResourceKey;
// import net.minecraft.server.MinecraftServer;
// import net.minecraft.server.level.ServerLevel;
// import net.minecraft.world.level.ChunkPos;
// import net.minecraft.world.level.Level;
// import net.minecraftforge.api.distmarker.OnlyIn;
// import net.minecraftforge.common.ForgeConfig.Server;
// import net.minecraftforge.common.util.LazyOptional;
// import net.minecraftforge.server.ServerLifecycleHooks;

// @Mixin(com.simibubi.create.content.trains.station.GlobalStation.class)
// public abstract class GlobalStationMixin extends SingleBlockEntityEdgePoint {
//     // on load, get level capability and cache.
//     // register with level in this chunk with our uuid when train present

//     @Shadow
//     public abstract ResourceKey<Level> getBlockEntityDimension();

//     //public void notifyUpdate() // use to check train present

//     @Shadow @Nullable
// 	public abstract Train getPresentTrain();

//     private Optional<ICLManagerCapability> clManager = Optional.empty();
//     void registerWithCLManager(ICLManagerCapability manager){
        
//     }

//     private static class NewTrain extends Train {

//         public NewTrain(UUID id, UUID owner, TrackGraph graph, List<Carriage> carriages, List<Integer> carriageSpacing,
//                 boolean doubleEnded) {
//             super(id, owner, graph, carriages, carriageSpacing, doubleEnded);
//             //TODO Auto-generated constructor stub
//         }

//         private LazyOptional<ICLManagerCapability> clManager = LazyOptional.empty();

//         public void tick(Level level) {
//             // update current CLmanager pointed to
//             if (level instanceof ServerLevel){
//                 if (!clManager.isPresent() ){
//                     clManager = ((ServerLevel)level).getCapability(ICLManagerCapability.TOKEN);
//                 } else {
//                     clManager.ifPresent(manager -> {
//                         if (level != manager.getLevel()) {
//                             clManager.invalidate();
//                         }
//                     });
//                 }
//             }
//         }

//         public void leaveStation() {
//             // inject at head
//             GlobalStation station = getCurrentStation();
//             if (station != null) {
//                 clManager.ifPresent(manager -> {
//                     var pos = new ChunkPos(station.blockEntityPos);

//                 });
//             }
//             clManager.invalidate();
//         }

//         public void arriveAt(GlobalStation station) {
//             // inject at end
//         }



//     }
    
// }
