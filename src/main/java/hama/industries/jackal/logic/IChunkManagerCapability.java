package hama.industries.jackal.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IChunkManagerCapability<T extends IChunkManagerCapability<?>> {
    public static Capability<IChunkManagerCapability<?>> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});

    public T setup(ChunkPos pos, ServerLevel level);      // set core chunk others are derived from. added to list of chunks and cannot be removed.
    public void destroy();
    public void addChunkAndLoad(ChunkPos pos);                // add a chunk to list of chunks if valid
    public void removeChunkAndUnload(ChunkPos pos);             // remove a chunk from the list of chunks, if possible
    
}
