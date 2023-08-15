package hama.industries.jackal;

import hama.industries.jackal.block.PrimaryRealitySpike;
import hama.industries.jackal.block.SecondaryRealitySpike;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

class ModObjects {
    @ObjectHolder(JackalMod.MODID)
    public static final class ITEMS {
        @ObjectHolder(PrimaryRealitySpike.ID) public static final Item PRS = null;
        @ObjectHolder(SecondaryRealitySpike.ID) public static final Item SRS = null;
    }

    @ObjectHolder(JackalMod.MODID)
    public static final class BLOCKS {
        @ObjectHolder(PrimaryRealitySpike.ID) public static final PrimaryRealitySpike PRS = null;
        @ObjectHolder(SecondaryRealitySpike.ID) public static final SecondaryRealitySpike SRS = null;
    }

    @ObjectHolder(JackalMod.MODID)
    public static final class BLOCK_ENTITIES {
        @ObjectHolder(PrimaryRealitySpike.ID) public static final BlockEntityType<PrimaryRealitySpike.BE> PRS = null;
        @ObjectHolder(SecondaryRealitySpike.ID) public static final BlockEntityType<SecondaryRealitySpike.BE> SRS = null;
    }
}