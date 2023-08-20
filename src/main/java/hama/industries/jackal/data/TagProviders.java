package hama.industries.jackal.data;

import org.jetbrains.annotations.Nullable;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.lib.JackalTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TagProviders {
    public static void provideAll(DataGenerator gen, ExistingFileHelper fileHelper){
        var blockTags = new BlockTagsProvider(gen, JackalMod.MODID, fileHelper);
        gen.addProvider(blockTags);
        gen.addProvider(new JackalItemTagsProvider(gen, blockTags, JackalMod.MODID, fileHelper));
    }

    public static class JackalBlockTagsProvider extends BlockTagsProvider {
        public JackalBlockTagsProvider(DataGenerator p_126511_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_126511_, modId, existingFileHelper);
        }

        @Override
        public String getName() {
            return toString();
        }

        @Override
        protected void addTags() {
            tag(JackalTags.BLOCK.REALITY_SPIKES).add(
                JackalMod.BLOCKS.PRS, JackalMod.BLOCKS.SRS
            );
        }

    }

    public static class JackalItemTagsProvider extends ItemTagsProvider {

        public JackalItemTagsProvider(DataGenerator p_126530_, net.minecraft.data.tags.BlockTagsProvider p_126531_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_126530_, p_126531_, modId, existingFileHelper);
        }

        @Override
        public String getName() {
            return toString();
        }

        @Override
        protected void addTags() {
            this.copy(JackalTags.BLOCK.REALITY_SPIKES, JackalTags.ITEM.REALITY_SPIKES);
            
            tag(JackalTags.ITEM.REALITY_SPIKES).add(
                JackalMod.ITEMS.PRS, JackalMod.ITEMS.SRS
            );
        }

    }
}
