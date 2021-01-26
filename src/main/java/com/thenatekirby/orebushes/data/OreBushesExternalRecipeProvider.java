package com.thenatekirby.orebushes.data;

import com.thenatekirby.babel.core.RecipeOutput;
import com.thenatekirby.babel.datagen.ExternalRecipeProvider;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.integration.botanypots.BotanyPotsCropRecipeBuilder;
import com.thenatekirby.babel.integration.botanypots.BotanyPotsSoilRecipeBuilder;
import com.thenatekirby.babel.integration.create.CreateCrushingRecipeBuilder;
import com.thenatekirby.babel.integration.mekanism.MekanismCrushingRecipeBuilder;
import com.thenatekirby.babel.integration.mekanism.MekanismEnrichingRecipeBuilder;
import com.thenatekirby.babel.integration.thermal.ThermalInsolatingRecipeBuilder;
import com.thenatekirby.babel.integration.thermal.ThermalPulverizingRecipeBuilder;
import com.thenatekirby.babel.recipe.IExternalRecipe;
import com.thenatekirby.babel.core.RecipeIngredient;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.data.bushes.OreBushData;
import com.thenatekirby.orebushes.registration.OreBushesBlocks;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import com.thenatekirby.orebushes.util.condition.OreBushEnabledCondition;
import com.thenatekirby.orebushes.util.condition.OreBushIntegrationEnabledCondition;
import com.thenatekirby.orebushes.util.condition.OreBushGroundsEnabledCondition;
import net.minecraft.data.DataGenerator;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class OreBushesExternalRecipeProvider extends ExternalRecipeProvider {
    OreBushesExternalRecipeProvider(@Nonnull DataGenerator dataGenerator) {
        super(OreBushes.MOD_ID, dataGenerator);
    }

    @Override
    public void registerRecipes(Consumer<IExternalRecipe> consumer) {
        registerOreBushCompatRecipes(consumer);
        registerBotanyPotsSoilRecipes(consumer);
    }

    private void registerOreBushCompatRecipes(Consumer<IExternalRecipe> consumer) {
        for (OreBushData data : OreBushData.all()) {
            // Botany Pots
            String botanyCategory = "dirt";
            if (data.getTier() == MaterialTier.TIER_ONE) {
                botanyCategory = "orebushes_tier1";
            } else if (data.getTier() == MaterialTier.TIER_TWO) {
                botanyCategory = "orebushes_tier2";
            } else if (data.getTier() == MaterialTier.TIER_THREE) {
                botanyCategory = "orebushes_tier3";
            }

            BotanyPotsCropRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/botanypots/crops/crop_" + data.getMaterialId().getPath()))
                    .withSeed(data.getSeedId().toString())
                    .withCategory(botanyCategory)
                    .withDisplayBlock("orebushes:bush_" + data.getMaterialId().getPath())
                    .withGrowthTicks(1200)
                    .withOutput(data.getBerryId().toString())
                    .withChance(0.75f)
                    .withCondition(new OreBushEnabledCondition(data.getMaterialId()))
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.BOTANY_POTS))
                    .build(consumer);

            // Create
            CreateCrushingRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/create/crushing_" + data.getMaterialId().getPath()))
                    .withInput(data.getBerryId().toString())
                    .withOutput(data.getGroundsId().toString(), 2)
                    .withCondition(new OreBushGroundsEnabledCondition(data.getMaterialId()))
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.CREATE))
                    .build(consumer);

            // Mekanism
            MekanismEnrichingRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/mekanism/enriching_" + data.getMaterialId().getPath()))
                    .withInput(data.getBerryId().toString())
                    .withOutput(data.getGroundsId().toString(), 2)
                    .withCondition(new OreBushGroundsEnabledCondition(data.getMaterialId()))
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.MEKANISM))
                    .build(consumer);

            MekanismCrushingRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/mekanism/crushing_" + data.getMaterialId().getPath()))
                    .withInput(data.getGroundsId().toString())
                    .withOutput("mekanism:bio_fuel", 1)
                    .withCondition(new OreBushGroundsEnabledCondition(data.getMaterialId()))
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.MEKANISM))
                    .build(consumer);

            MekanismEnrichingRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/mekanism/crushing_" + data.getMaterialId().getPath() + "_seeds"))
                    .withInput(data.getSeedId().toString())
                    .withOutput("mekanism:bio_fuel", 1)
                    .withCondition(new OreBushEnabledCondition(data.getMaterialId()))
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.MEKANISM))
                    .build(consumer);

            // Thermal
            ThermalPulverizingRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/thermal/pulverizing_" + data.getMaterialId().getPath()))
                    .withInput(data.getBerryId().toString())
                    .withOutput(data.getGroundsId().toString(), 2)
                    .withCondition(new OreBushGroundsEnabledCondition(data.getMaterialId()))
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.THERMAL))
                    .build(consumer);

            ThermalInsolatingRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/thermal/insolating_" + data.getMaterialId().getPath()))
                    .withInput(RecipeIngredient.fromItem(data.getSeedId()))
                    .withOutput(RecipeOutput.fromItem(data.getBerryId()).withChance(0.75f))
                    .withOutput(RecipeOutput.fromItem(data.getSeedId()).withChance(1.0f))
                    .withEnergyMod(3.0f)
                    .withWaterMod(3.0f)
                    .withCondition(new OreBushIntegrationEnabledCondition(Mods.THERMAL))
                    .build(consumer);
        }
    }

    private void registerBotanyPotsSoilRecipes(Consumer<IExternalRecipe> consumer) {
        BotanyPotsSoilRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/botanypots/soils/tier_one"))
                .withInput(RecipeIngredient.fromItem(OreBushesBlocks.TIER_ONE_FARMLAND))
                .withDisplayBlock(OreBushesBlocks.TIER_ONE_FARMLAND.asBlock().getRegistryName().toString())
                .withCategory("orebushes_tier1")
                .withGrowthModifier(0.2f)
                .withCondition(new OreBushIntegrationEnabledCondition(Mods.BOTANY_POTS))
                .build(consumer);

        BotanyPotsSoilRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/botanypots/soils/tier_two"))
                .withInput(RecipeIngredient.fromItem(OreBushesBlocks.TIER_TWO_FARMLAND))
                .withDisplayBlock(OreBushesBlocks.TIER_TWO_FARMLAND.asBlock().getRegistryName().toString())
                .withCategory("orebushes_tier1")
                .withCategory("orebushes_tier2")
                .withGrowthModifier(0.2f)
                .withCondition(new OreBushIntegrationEnabledCondition(Mods.BOTANY_POTS))
                .build(consumer);

        BotanyPotsSoilRecipeBuilder.builder(OreBushes.MOD.withPath("integrations/botanypots/soils/tier_three"))
                .withInput(RecipeIngredient.fromItem(OreBushesBlocks.TIER_THREE_FARMLAND))
                .withDisplayBlock(OreBushesBlocks.TIER_THREE_FARMLAND.asBlock().getRegistryName().toString())
                .withCategory("orebushes_tier1")
                .withCategory("orebushes_tier2")
                .withCategory("orebushes_tier3")
                .withGrowthModifier(0.2f)
                .withCondition(new OreBushIntegrationEnabledCondition(Mods.BOTANY_POTS))
                .build(consumer);
    }
}
