package hama.industries.jackal.lib;

import hama.industries.jackal.JackalMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class JackalTags {

    private static final class LABELS {
        public static final String REALITY_SPIKES = "reality_spikes";
    }

    // public static final class BLOCK_ENTITY {
    //     public static final TagKey<BlockEntityType<?>> REALITY_SPIKES = tag(LABELS.REALITY_SPIKES);

    //     private static TagKey<BlockEntityType<?>> tag(String key){
    //         return ForgeRegistries.BLOCK_ENTITIES.tags().createTagKey(new ResourceLocation(JackalMod.MODID, key));
    //     }
    // }

    public static final class ITEM {
        public static final TagKey<Item> REALITY_SPIKES = tag(LABELS.REALITY_SPIKES);

        private static TagKey<Item> tag(String key){
            return ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(JackalMod.MODID, key));
        }
    }

    public static final class BLOCK {
        public static final TagKey<Block> REALITY_SPIKES = tag(LABELS.REALITY_SPIKES);

        private static TagKey<Block> tag(String key){
            return ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(JackalMod.MODID, key));
        }
    }

}
