package hama.industries.jackal.data;

import java.util.function.Consumer;

import hama.industries.jackal.JackalMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags.Items;

public class RecipeProviders extends RecipeProvider {

    public RecipeProviders(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consoomer){
        ShapedRecipeBuilder.shaped(JackalMod.ITEMS.PRIMARY_CL)
            .pattern("sas")
            .pattern("sds")
            .pattern("sss")
            .define('s', ItemTags.SAND)
            .define('a', Items.GEMS_AMETHYST)
            .define('d', Items.GEMS_DIAMOND)
            .unlockedBy("has_diamond", has(Items.GEMS_DIAMOND))
            .save(consoomer); 

        ShapedRecipeBuilder.shaped(JackalMod.ITEMS.SECONDARY_CL)
            .pattern("sas")
            .pattern("sps")
            .pattern("sss")
            .define('s', ItemTags.SAND)
            .define('a', Items.GEMS_AMETHYST)
            .define('p', Items.ENDER_PEARLS)
            .unlockedBy("has_cl", has(JackalMod.ITEMS.PRIMARY_CL))
            .save(consoomer); 
    }
    
}
