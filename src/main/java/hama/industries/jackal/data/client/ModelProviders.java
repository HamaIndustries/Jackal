package hama.industries.jackal.data.client;

import hama.industries.jackal.JackalMod;
import hama.industries.jackal.block.PrimaryRealitySpike;
import hama.industries.jackal.block.SecondaryRealitySpike;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelProviders {
    public static class RealitySpikeBlockStateProvider extends BlockStateProvider{
        // private static final ModelTemplate ITEM_MODEL = new ModelTemplate(Optional.of(new ResourceLocation("item/generated")), Optional.empty(), null)

        public RealitySpikeBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
            super(gen, modid, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            // registerBlockAndItem(PrimaryRealitySpike.ID, JackalMod.BLOCKS.PRS);
            registerBlockAndItem(SecondaryRealitySpike.ID, JackalMod.BLOCKS.SRS);
            registerBlockAndItem(PrimaryRealitySpike.ID, JackalMod.BLOCKS.PRS);

            // var prs = models().getExistingFile(modLoc("block/" + PrimaryRealitySpike.ID));
            
            // getVariantBuilder(JackalMod.BLOCKS.PRS).forAllStates(state -> 
            //     ConfiguredModel.builder().modelFile(prs).build()
            // );
            // simpleBlockItem(JackalMod.BLOCKS.PRS, prs);
            
        }

        private ModelFile registerBlockAndItem(String id, Block block){
            var baseModel = models().cubeAll(id, modLoc("block/" + id));
            var poweredModel = models().cubeAll(id + "_powered", modLoc("block/" + id + "_powered"));

            getVariantBuilder(block)
                .partialState().with(BlockStateProperties.POWERED, false)
                    .modelForState()
                    .modelFile(baseModel)
                    .addModel()
                .partialState().with(BlockStateProperties.POWERED, true)
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