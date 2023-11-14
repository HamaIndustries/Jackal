package hama.industries.jackal.logic.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.google.common.collect.HashMultimap;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.capability.ICLManagerCapability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.common.world.ForgeChunkManager.TicketHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = JackalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CLManager implements ICLManagerCapability, INBTSerializable<CompoundTag> {
    public static final String RESOURCE_ID = "chunk_loader_manager_impl";
    private static final int RANGE = 7;

    private Map<ChunkPos, UUID> pcls = new HashMap<>();
    private Set<ChunkPos> scls = new HashSet<>();
    private Multimap<ChunkPos, UUID> triggers = HashMultimap.create();

    private ServerLevel level;
    public CLManager (ServerLevel level){
        this.level = level;
    }

    @SubscribeEvent
    public static void attachCapability(final AttachCapabilitiesEvent<Level> event){
        var level = event.getObject();
        if (! (level instanceof ServerLevel) ) return;

        var manager = new CLManager((ServerLevel)level);
        LazyOptional<ICLManagerCapability> optional = LazyOptional.of(() -> manager);

        var provider = new ICapabilitySerializable<CompoundTag>() {             
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction direction) {
                if (cap == ICLManagerCapability.TOKEN) {
                    return optional.cast();
                }
                return LazyOptional.empty();
            }

            @Override
            public CompoundTag serializeNBT() {
                return manager.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                manager.deserializeNBT(nbt);
            }
        };
        event.addCapability(new ResourceLocation(JackalMod.MODID, RESOURCE_ID), provider);
        event.addListener(optional::invalidate);
    }

    public static void validateTickets(ServerLevel level, TicketHelper ticketHelper) {
        // we manage our own reinstatement with serialization (callback is unreliable; not always called due to caching)
        ticketHelper.getEntityTickets().keySet().stream().forEach(id ->{
            System.out.println("removing tickets for " + id);
            ticketHelper.removeAllTickets(id);
        });
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event){
        //     var cap = ((ServerLevel)level).getCapability(ICLManagerCapability.TOKEN);
        //     cap.ifPresent(manager -> {
        //         manager.removeAllCLs();
        //     });
    }

    @Override
    public void removeAllCLs(){
        for (var pos : pcls.keySet().stream().collect(Collectors.toList())){
            removePrimaryCL(pos);
        }
        for (var pos : scls.stream().collect(Collectors.toList())){
            removeSecondaryCL(pos);
        }
        triggers.clear();
    }

    @Override
    public void addPrimaryCL(ChunkPos pos) {
        pcls.putIfAbsent(pos, UUID.randomUUID());
        if (isPrimaryActiveAt(pos)) 
            setPrimaryActive(pos, true);
    }

    @Override
    public void removePrimaryCL(ChunkPos pos) {
        setPrimaryActive(pos, false);
        pcls.remove(pos);
        System.out.println("wow");
    }

    @Override
    public void addSecondaryCL(ChunkPos pos) {
        if (!scls.contains(pos))
            scls.add(pos);
            for (var ppos : getChunksInRange(pcls.keySet().stream(), pos).filter(this::isPrimaryActiveAt).collect(Collectors.toList())){
                forceChunk(pcls.get(ppos), pos, true);
            }
    }

    @Override
    public void removeSecondaryCL(ChunkPos pos) {
        for (var ppos : getChunksInRange(pcls.keySet().stream(), pos).collect(Collectors.toList())){ // ignore these sorry its like 1 am
            forceChunk(pcls.get(ppos), pos, false);
        }
        scls.remove(pos);
    }

    private Stream<ChunkPos> getChunksInRange(Stream<ChunkPos> cls, ChunkPos pos){
        return cls.filter(p -> (p.getChessboardDistance(pos) <= RANGE));
    }

    @Override
    public CompoundTag serializeNBT() {
        var pTags = new ListTag();
        for (var entry : pcls.entrySet()){
            var pt = new CompoundTag();
            pt.putInt("x", entry.getKey().x);
            pt.putInt("z", entry.getKey().z);
            pt.putUUID("id", entry.getValue());
            pTags.add(pTags.size(), pt);
        }
        var sTags = new ListTag();
        for (var entry : scls){
            var st = new CompoundTag();
            st.putInt("x", entry.x);
            st.putInt("z", entry.z);
            sTags.add(sTags.size(), st);
        }
        var TrigTags = new ListTag();
        for (var entry : triggers.entries()){
            var t = new CompoundTag();
            t.putInt("x", entry.getKey().x);
            t.putInt("z", entry.getKey().z);
            t.putUUID("id", entry.getValue());
            TrigTags.add(TrigTags.size(), t);
        }
        var nbt = new CompoundTag();
        nbt.put("primaries", pTags);
        nbt.put("secondaries", sTags);
        nbt.put("triggers", TrigTags);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getList("secondaries", Tag.TAG_COMPOUND).forEach(pt -> {
            var ptag = (CompoundTag)pt;
            scls.add(new ChunkPos(ptag.getInt("x"), ptag.getInt("z")));
        });
        nbt.getList("primaries", Tag.TAG_COMPOUND).forEach(pt -> {
            var ptag = (CompoundTag)pt;
            var pos = new ChunkPos(ptag.getInt("x"), ptag.getInt("z"));
            pcls.put(pos, ptag.getUUID("id"));
        });
        nbt.getList("triggers", Tag.TAG_COMPOUND).forEach(tr -> {
            var ttag = (CompoundTag)tr;
            var pos = new ChunkPos(ttag.getInt("x"), ttag.getInt("z"));
            addTrigger(ttag.getUUID("id"), pos);
        });
    }

    @Override
    public ServerLevel getLevel() {
        return level;
    }

    // triggers determine actual active logic
    // has trigger -> attempt set active
    @Override
    public void addTrigger(UUID id, ChunkPos pos) {
        var firstTrigger = false;
        if (!triggers.containsKey(pos))
            firstTrigger = true;
        triggers.put(pos, id);
        if (firstTrigger)
            setPrimaryActive(pos, true);
        
    }

    // has no trigger -> set inactive (disable force load)
    @Override
    public void removeTrigger(UUID id, ChunkPos pos) {
        triggers.remove(pos, id);
        if (!triggers.containsKey(pos))
            setPrimaryActive(pos, false);
    }

    private boolean isPrimaryActiveAt(ChunkPos pos) {
        // doesn't say if it is actually forcing a given chunk, just whether it should force
        return triggers.containsKey(pos) && pcls.containsKey(pos);
    }

    private void setPrimaryActive(ChunkPos pos, boolean active){
        var primary = pcls.get(pos);
        if (primary == null)
            return;
        forceChunk(primary, pos, active);
        // getChunksInRange(scls.stream(), pos)
        //     .forEach(secondary -> forceChunk(primary, pos, active));
        for (var sc : getChunksInRange(scls.stream(), pos).collect(Collectors.toList())) {
            forceChunk(primary, sc, active);
        }
    }

    private void forceChunk(UUID primary, ChunkPos pos, boolean forced){
        System.out.println(pos);
        ForgeChunkManager.forceChunk(level, JackalMod.MODID, primary, pos.x, pos.z, forced, true);
    }

    // for now, throw if no existing primary at position
    @Override
    public void setSelfTrigger(ChunkPos pos, boolean value) {
        if (value) addTrigger(pcls.get(pos), pos);
        else removeTrigger(pcls.get(pos), pos);
    }

    private void report(){
        System.out.println("==========");
        System.out.println("pcls: " + pcls);
        System.out.println("scls: " + scls);
        System.out.println("trigs: " + triggers);
        
    }

    @SubscribeEvent
    public static void waw (RegisterCommandsEvent event) {
        var jcmd = Commands.literal("jakal").requires(pred -> pred.hasPermission(2));

        var cmd_check = Commands.literal("status").executes(
           new Command<CommandSourceStack>() {

                @Override
                public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                    context.getSource().getLevel().getCapability(ICLManagerCapability.TOKEN).ifPresent(mgr -> {
                        ((CLManager)mgr).report();
                        context.getSource().sendSuccess(new TextComponent("Reported JAKAL state (check server logs)"), true);
                    });
                    return 0;
                }
           }
        );
        var cmd_clear = Commands.literal("clear").requires(v -> true).executes(
           new Command<CommandSourceStack>() {

                @Override
                public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                    context.getSource().getLevel().getCapability(ICLManagerCapability.TOKEN).ifPresent(mgr -> {
                        mgr.removeAllCLs();
                        context.getSource().sendSuccess(new TextComponent("Cleared all JAKAL Anatta"), true);
                    });
                    return 0;
                }
           }
        );
        event.getDispatcher().register(jcmd.then(cmd_check));
        event.getDispatcher().register(jcmd.then(cmd_clear));
    }

}
