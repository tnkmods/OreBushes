package com.thenatekirby.orebushes.data;

import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.recipe.builder.TaggedSmeltingRecipeBuilder;
import com.thenatekirby.babel.core.RecipeIngredient;
import com.thenatekirby.babel.recipe.builder.ShapedRecipeBuilder;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.data.bushes.OreBushData;
import com.thenatekirby.orebushes.registration.OreBushesBlocks;
import com.thenatekirby.orebushes.registration.OreBushesItems;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import com.thenatekirby.orebushes.util.condition.OreBushEnabledCondition;
import com.thenatekirby.orebushes.util.condition.OreBushGroundsEnabledCondition;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class OreBushesRecipeProvider extends RecipeProvider {
    OreBushesRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        registerBaseRecipes(consumer);
        registerOreBushRecipes(consumer);
    }

    private void registerOreBushRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        for (OreBushData data : OreBushData.all()) {
            if (data.getSmeltingResult() != null) {
                registerOreBushSmeltingRecipes(consumer, data);
            }

            if (data.getSeedInput() != null) {
                registerOreBushSeedRecipe(consumer, data);
            }
        }
    }

    private void registerOreBushSmeltingRecipes(@Nonnull Consumer<IFinishedRecipe> consumer, OreBushData data) {
        RecipeIngredient resultData = data.getSmeltingResult();

        TaggedSmeltingRecipeBuilder.builder(OreBushes.MOD.withPath("smelting/" + data.getMaterialId().getPath()))
                .withInput(RecipeIngredient.fromItem(data.getBerryId().toString()))
                .withOutput(resultData)
                .withCondition(new OreBushEnabledCondition(data.getMaterialId()))
                .build(consumer);

        TaggedSmeltingRecipeBuilder.builder(OreBushes.MOD.withPath("smelting/" + data.getMaterialId().getPath() + "_grounds"))
                .withInput(RecipeIngredient.fromItem(data.getGroundsId().toString()))
                .withOutput(resultData)
                .withCondition(new OreBushGroundsEnabledCondition(data.getMaterialId()))
                .build(consumer);
    }

    private void registerOreBushSeedRecipe(@Nonnull Consumer<IFinishedRecipe> consumer, OreBushData data) {
        IItemProvider dustProvider;
        if (data.getTier() == MaterialTier.TIER_ONE) {
            dustProvider = OreBushesItems.TIER_ONE_DUST;
        } else if (data.getTier() == MaterialTier.TIER_TWO) {
            dustProvider = OreBushesItems.TIER_TWO_DUST;
        } else {
            dustProvider = OreBushesItems.TIER_THREE_DUST;
        }

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("seeds/" + data.getMaterialId().getPath()))
                .withResult(data.getSeedId().toString())
                .withPatternLine("XDX")
                .withPatternLine("DSD")
                .withPatternLine("XDX")
                .withKey('X', data.getSeedInput())
                .withKey('D', RecipeIngredient.fromItem(dustProvider))
                .withKey('S', RecipeIngredient.fromTag(Mods.FORGE.withPath("seeds")))
                .withCondition(new OreBushEnabledCondition(data.getMaterialId()))
                .build(consumer);
    }

    private void registerBaseRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/tier_one_farmland"))
                .withResult(OreBushesBlocks.TIER_ONE_FARMLAND, 4)
                .withPatternLine("XDX")
                .withPatternLine("DWD")
                .withPatternLine("XDX")
                .withKey('X', RecipeIngredient.fromItem(OreBushesItems.TIER_ONE_DUST))
                .withKey('D', RecipeIngredient.fromItem(Blocks.DIRT))
                .withKey('W', RecipeIngredient.fromItem(Items.WATER_BUCKET))
                .build(consumer);

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/tier_two_farmland"))
                .withResult(OreBushesBlocks.TIER_TWO_FARMLAND)
                .withPatternLine("XXX")
                .withPatternLine("XDX")
                .withPatternLine("XXX")
                .withKey('X', RecipeIngredient.fromItem(OreBushesItems.TIER_TWO_DUST))
                .withKey('D', RecipeIngredient.fromItem(OreBushesBlocks.TIER_ONE_FARMLAND))
                .build(consumer);

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/tier_three_farmland"))
                .withResult(OreBushesBlocks.TIER_THREE_FARMLAND)
                .withPatternLine("XXX")
                .withPatternLine("XDX")
                .withPatternLine("XXX")
                .withKey('X', RecipeIngredient.fromItem(OreBushesItems.TIER_THREE_DUST))
                .withKey('D', RecipeIngredient.fromItem(OreBushesBlocks.TIER_TWO_FARMLAND))
                .build(consumer);

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/essence_seed"))
                .withResult(OreBushes.MOD.withPath("seed_essence").toString())
                .withPatternLine(" X ")
                .withPatternLine("XSX")
                .withPatternLine(" X ")
                .withKey('X', RecipeIngredient.fromItem(OreBushesItems.TIER_ONE_DUST))
                .withKey('S', RecipeIngredient.fromItem(Items.WHEAT_SEEDS))
                .build(consumer);

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/tier_two_essence"))
                .withResult(OreBushesItems.TIER_TWO_DUST)
                .withPatternLine("XXX")
                .withPatternLine("X X")
                .withPatternLine("XXX")
                .withKey('X', RecipeIngredient.fromItem(OreBushesItems.TIER_ONE_DUST))
                .build(consumer);

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/tier_three_essence"))
                .withResult(OreBushesItems.TIER_THREE_DUST)
                .withPatternLine("XXX")
                .withPatternLine("X X")
                .withPatternLine("XXX")
                .withKey('X', RecipeIngredient.fromItem(OreBushesItems.TIER_TWO_DUST))
                .build(consumer);

        ShapedRecipeBuilder.builder(OreBushes.MOD.withPath("crafting/enriched_bone_meal"))
                .withResult(OreBushesItems.ENRICHED_BONE_MEAL, 8)
                .withPatternLine("XXX")
                .withPatternLine("XDX")
                .withPatternLine("XXX")
                .withKey('X', RecipeIngredient.fromItem(Items.BONE_MEAL))
                .withKey('D', RecipeIngredient.fromItem(OreBushesItems.TIER_THREE_DUST))
                .build(consumer);
    }

    @Override
    @Nonnull
    public String getName() {
        return OreBushes.MOD_ID + ":recipes";
    }
}
