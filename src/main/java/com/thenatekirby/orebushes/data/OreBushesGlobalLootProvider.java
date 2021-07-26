package com.thenatekirby.orebushes.data;

import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.loot.GrassLootCondition;
import com.thenatekirby.orebushes.loot.GrassLootModifier;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.conditions.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

// ====---------------------------------------------------------------------------====

public class OreBushesGlobalLootProvider extends GlobalLootModifierProvider {
    OreBushesGlobalLootProvider(DataGenerator gen) {
        super(gen, OreBushes.MOD_ID);
    }

    @Override
    protected void start() {
        ILootCondition[] grassConditions = {
                new GrassLootCondition(),
                RandomChance.randomChance(0.1f).build(),
                Inverted.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))).build(),
                BlockStateProperty.hasBlockStateProperties(Blocks.GRASS).build()
        };

        ILootCondition[] tallGrassConditions = {
                new GrassLootCondition(),
                RandomChance.randomChance(0.1f).build(),
                Inverted.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))).build(),
                BlockStateProperty.hasBlockStateProperties(Blocks.TALL_GRASS).build()
        };

        add("seeds_from_grass", new GrassLootModifier.Serializer(), new GrassLootModifier(grassConditions));
        add("seeds_from_tall_grass", new GrassLootModifier.Serializer(), new GrassLootModifier(tallGrassConditions));
    }
}
