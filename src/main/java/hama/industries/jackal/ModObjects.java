package hama.industries.jackal;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

import hama.industries.jackal.block.PrimaryCLBlock;
import hama.industries.jackal.block.SecondaryCLBlock;
import hama.industries.jackal.block.entity.PrimaryCLBlockEnt;
import hama.industries.jackal.block.entity.SecondaryCLBlockEnt;
import hama.industries.jackal.lib.JackalTags;

class ModObjects {
    @ObjectHolder(JackalMod.MODID)
    public static final class MOD_ITEMS {
        @ObjectHolder(PrimaryCLBlock.ID) public static final Item PRIMARY_CL = null;
        @ObjectHolder(SecondaryCLBlock.ID) public static final Item SECONDARY_CL = null;
    }
    public static final MOD_ITEMS ITEMS = new MOD_ITEMS();

    @ObjectHolder(JackalMod.MODID)
    public static final class MOD_BLOCKS {
        @ObjectHolder(PrimaryCLBlock.ID) public static final PrimaryCLBlock PRIMARY_CL = null;
        @ObjectHolder(SecondaryCLBlock.ID) public static final SecondaryCLBlock SECONDARY_CL = null;
    }
    public static final MOD_BLOCKS BLOCKS = new MOD_BLOCKS();

    @ObjectHolder(JackalMod.MODID)
    public static final class MOD_BLOCK_ENTITIES {
        @ObjectHolder(PrimaryCLBlock.ID) public static final BlockEntityType<PrimaryCLBlockEnt> PRIMARY_CL = null;
        @ObjectHolder(SecondaryCLBlock.ID) public static final BlockEntityType<SecondaryCLBlockEnt> SECONDARY_CL = null;
    }

    public static final MOD_BLOCK_ENTITIES BLOCK_ENTITIES = new MOD_BLOCK_ENTITIES();
}