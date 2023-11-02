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
        var blockTags = new JackalBlockTagsProvider(gen, JackalMod.MODID, fileHelper);
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
            tag(JackalTags.BLOCK.CHUNK_LOADERS).add(
                JackalMod.BLOCKS.PRIMARY_CL, JackalMod.BLOCKS.SECONDARY_CL
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
            this.copy(JackalTags.BLOCK.CHUNK_LOADERS, JackalTags.ITEM.CHUNK_LOADERS);
            
            tag(JackalTags.ITEM.CHUNK_LOADERS).add(
                JackalMod.ITEMS.PRIMARY_CL, JackalMod.ITEMS.SECONDARY_CL
            );
        }

    }
}
