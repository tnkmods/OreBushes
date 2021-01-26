package com.thenatekirby.orebushes.data;

import com.thenatekirby.orebushes.OreBushes;
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
                RandomChance.builder(0.1f).build(),
                Inverted.builder(MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS))).build(),
                BlockStateProperty.builder(Blocks.GRASS).build()
        };

        ILootCondition[] tallGrassConditions = {
                RandomChance.builder(0.1f).build(),
                Inverted.builder(MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS))).build(),
                BlockStateProperty.builder(Blocks.TALL_GRASS).build()
        };

        add("seeds_from_grass", new GrassLootModifier.Serializer(), new GrassLootModifier(grassConditions));
        add("seeds_from_tall_grass", new GrassLootModifier.Serializer(), new GrassLootModifier(tallGrassConditions));
    }
}
