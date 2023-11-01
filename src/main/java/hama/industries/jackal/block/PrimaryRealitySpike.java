package hama.industries.jackal.block;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.icu.text.RelativeDateTimeFormatter.Direction;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.RealitySpike;
import hama.industries.jackal.logic.ChunkLoadingManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public final class PrimaryRealitySpike extends RealitySpike.RealitySpikeBlock {

    public static final String ID = "primary_reality_spike";

    public PrimaryRealitySpike(Properties props) {
        super(props);
    }

    public static final class BE extends RealitySpike.RealitySpikeBlockEntity {
        /* Primary reality  spike controls all loading and unloading, secondaries only help in registering themselves with the primary.  */
        private final Set<ChunkPos> secondaries =  new HashSet<>();
        

        public BE(BlockPos pos, BlockState state) {
            super(JackalMod.BLOCK_ENTITIES.PRS, pos, state);


            
        }

        public void onLoad(){
            if (!this.level.isClientSide) {
                // INTRO();
            }
        }

        @Override
        public void onRedstoneSignal(){
            // INTRO();
        }

        

        /*
         * Normal course of events:
         * chunk containing primary is loaded
         * primary loads registered chunks and verifies they are all valid (enabled)
         * if they are not valid, it searches for all connected valid chunks and creates the new registered set from that
         * it sets all valid chunks to force loaded
         * 
         */

        private void updateNetwork(){ // primary endpoint for loading/updating a spike network
            
            Set<ChunkPos> invalid = new HashSet<>();
            // this.level.getServer().addTickable(() -> {
                for (var pos : secondaries){
                    // load chunk temporarily
                    ServerLevel level = (ServerLevel)this.level;
                    level.setChunkForced(pos.x, pos.z, true);

                    // verify all loaders are valid (singular/enabled)
                    if (!hasValidConfiguration(level.getChunk(pos.x, pos.z))){
                        invalid.add(pos);
                    }
                }
            // });

            // remove all invalid chunks and search for all connected
            if (invalid.size() > 0 || secondaries.size() == 0) {
                // secondaries = getValidConnected(level, level.getChunk(getBlockPos()).getPos());
            }

            JackalMod.logger().info("Primary has " + secondaries.size() + " valid secondaries");
            for (var x : secondaries) {
                JackalMod.logger().info("(" + x.x + " " + x.z + ")");
            }
        }

        private Set<ChunkPos> getValidConnected(Level level, ChunkPos pos){
            return getValidConnected(level, pos, new HashSet<>());
        }

        private Set<ChunkPos> getValidConnected(Level level, ChunkPos pos, Set<ChunkPos>connected){
            for (var tpos : Set.<ChunkPos>of(new ChunkPos(pos.x+1, pos.z), new ChunkPos(pos.x-1, pos.z), new ChunkPos(pos.x, pos.z+1), new ChunkPos(pos.x, pos.z-1))){
                if (!connected.contains(tpos) &&  hasValidConfiguration(level.getChunk(tpos.x, tpos.z))){
                    connected.add(tpos);
                    getValidConnected(level, tpos, connected); // updates passed set with values
                }
            }
            return connected;
        }

        private void setChunkLoadState(ChunkPos pos, boolean value){
            this.level.getServer().addTickable(() -> {
                ((ServerLevel)this.level).setChunkForced(pos.x, pos.z, value);
            });
            // this.level.getServer().addTickable(() -> ((ServerWorld)this.level).setChunkForced(pos.x, pos.z, true));
        }

        
    }
    
    @Override
    public PrimaryRealitySpike.BE newBlockEntity(BlockPos pos, BlockState state) {
        return new PrimaryRealitySpike.BE(pos, state);
    }
}
