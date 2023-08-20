package hama.industries.jackal;

import hama.industries.jackal.block.PrimaryRealitySpike;
import hama.industries.jackal.block.SecondaryRealitySpike;
import hama.industries.jackal.lib.JackalTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

class ModObjects {
    @ObjectHolder(JackalMod.MODID)
    public static final class MOD_ITEMS {
        @ObjectHolder(PrimaryRealitySpike.ID) public static final Item PRS = null;
        @ObjectHolder(SecondaryRealitySpike.ID) public static final Item SRS = null;
    }
    public static final MOD_ITEMS ITEMS = new MOD_ITEMS();

    @ObjectHolder(JackalMod.MODID)
    public static final class MOD_BLOCKS {
        @ObjectHolder(PrimaryRealitySpike.ID) public static final PrimaryRealitySpike PRS = null;
        @ObjectHolder(SecondaryRealitySpike.ID) public static final SecondaryRealitySpike SRS = null;
    }
    public static final MOD_BLOCKS BLOCKS = new MOD_BLOCKS();

    @ObjectHolder(JackalMod.MODID)
    public static final class MOD_BLOCK_ENTITIES {
        @ObjectHolder(PrimaryRealitySpike.ID) public static final BlockEntityType<PrimaryRealitySpike.BE> PRS = null;
        @ObjectHolder(SecondaryRealitySpike.ID) public static final BlockEntityType<SecondaryRealitySpike.BE> SRS = null;
    }

    public static final MOD_BLOCK_ENTITIES BLOCK_ENTITIES = new MOD_BLOCK_ENTITIES();
}