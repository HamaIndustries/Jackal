package hama.industries.jackal.data.client;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.block.PrimaryCLBlock;
import hama.industries.jackal.block.SecondaryCLBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelProviders {
    public static class CLBlockStateProvider extends BlockStateProvider{
        // private static final ModelTemplate ITEM_MODEL = new ModelTemplate(Optional.of(new ResourceLocation("item/generated")), Optional.empty(), null)

        public CLBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
            super(gen, modid, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            // registerBlockAndItem(PrimaryCLBlock.ID, JackalMod.BLOCKS.PRIMARY_CL);
            // registerBlockAndItem(SecondaryCLBlock.ID, JackalMod.BLOCKS.SECONDARY_CL);

            // var prs = models().getExistingFile(modLoc("block/" + PrimaryRealitySpike.ID));
            
            // getVariantBuilder(JackalMod.BLOCKS.PRS).forAllStates(state -> 
            //     ConfiguredModel.builder().modelFile(prs).build()
            // );
            // simpleBlockItem(JackalMod.BLOCKS.PRS, prs);
            
            getVariantBuilder(JackalMod.BLOCKS.PRIMARY_CL)
                .partialState().with(BlockStateProperties.ENABLED,false)
                    .modelForState()
                    .modelFile(getSimpleModel(modLoc("block/pcl_disabled")))
                    .addModel()
                .partialState().with(BlockStateProperties.ENABLED, true).with(BlockStateProperties.POWERED, false)
                    .modelForState()
                    .modelFile(getSimpleModel(modLoc("block/pcl_enabled")))
                    .addModel()
                .partialState().with(BlockStateProperties.ENABLED, true).with(BlockStateProperties.POWERED, true)
                    .modelForState()
                    .modelFile(getSimpleModel(modLoc("block/pcl_enabled_active")))
                    .addModel();

            getVariantBuilder(JackalMod.BLOCKS.SECONDARY_CL)
                .partialState().with(BlockStateProperties.ENABLED,false)
                    .modelForState()
                    .modelFile(getSimpleModel(modLoc("block/scl_disabled")))
                    .addModel()
                .partialState().with(BlockStateProperties.ENABLED, true)
                    .modelForState()
                    .modelFile(getSimpleModel(modLoc("block/scl_enabled_active")))
                    .addModel();
        }

        private ExistingModelFile getSimpleModel(ResourceLocation location) {
            return models().getExistingFile(location);
        }

        private ModelFile registerBlockAndItem(String id, Block block){
            var baseModel = models().cubeAll(id, modLoc("block/" + id));
            var poweredModel = models().cubeAll(id + "_powered", modLoc("block/" + id + "_powered"));

            getVariantBuilder(block)
                .partialState().with(BlockStateProperties.ENABLED, false)
                    .modelForState()
                    .modelFile(baseModel)
                    .addModel()
                .partialState()
                .with(BlockStateProperties.ENABLED, true)
                .with(null, null)
                    .modelForState()
                    .modelFile(poweredModel)
                    .addModel();
            simpleBlockItem(block, baseModel);
            return baseModel;
        


            // getVariantBuilder(block)
            //     .partialState().with(BlockStateProperties.POWERED, false)
            //         .modelForState()
            //         .modelFile(baseModel)
            //         .addModel()
            //     .partialState().with(BlockStateProperties.POWERED, true)
            //         .modelForState()
            //         .modelFile(poweredModel)
            //         .addModel();
            // simpleBlockItem(block, baseModel);
            // return baseModel;
        }
    }
}