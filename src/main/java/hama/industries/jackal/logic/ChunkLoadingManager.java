package hama.industries.jackal.logic;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import hama.industries.jackal.JackalMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=JackalMod.MODID, bus = Bus.FORGE)
public class ChunkLoadingManager {

    static {
        ForgeChunkManager.setForcedChunkLoadingCallback(JackalMod.MODID, (level, ticketHelper) -> {
            ticketHelper.getBlockTickets().forEach((pos, pair) -> ticketHelper.removeAllTickets(pos));
        });
    }

    private ChunkPos root;
    private Set<ChunkPos> chunks = new HashSet<>();
    private ServerLevel level;

    public ChunkLoadingManager(ChunkPos pos, ServerLevel level){
        this.root = pos;
        this.level = level;
    }

    public void destroy(){
        // managers.remove(this);
    }

    private void pruneUnreachable(){
        Set<ChunkPos> reachable = new HashSet<>(Set.of(root));
        reachable.add(root);
        Set<ChunkPos> check = new HashSet<>(Set.of(root));
        check.add(root);
        while(!check.isEmpty()){
            // make set of nodes surrounding checked nodes
            var surround = check.stream().<ChunkPos>mapMulti(
                (p, c) -> {c.accept(new ChunkPos(p.x-1, p.z));c.accept(new ChunkPos(p.x+1, p.z));c.accept(new ChunkPos(p.x, p.z-1));c.accept(new ChunkPos(p.x, p.z+1));}
            ).collect(Collectors.toSet());

            // intersection of checks and search list is our next list of valid chunks to check
            check = Sets.intersection(surround, chunks);

            // add valid chunks to reachable
            reachable.addAll(check);
        }
        // unload unreachable
        Sets.difference(chunks, reachable).forEach(chunks::remove);
        this.chunks = reachable;
    }

    private void addChunk(ChunkPos pos) {
        // simpler but I want to avoid redundancy
        // add this back in if we want to check performance
        // for (var chunk : chunks){
        //     if(chunk.getChessboardDistance(pos) == 1){
        //         chunks.add(pos);
        //         return;
        //     }
        // }
        chunks.add(pos);
    }

    private void removeChunk(ChunkPos pos) {
        chunks.remove(pos);
        // TODO LOAD
    }

    // @Override
    // public void addChunkAndLoad(ChunkPos pos) {
    //     addChunk(pos);
    //     // TODO ADD LOAD
    // }

    // @Override
    // public void removeChunkAndUnload(ChunkPos pos) {
    //     removeChunk(pos);
    //     pruneUnreachable();
    // }
}
