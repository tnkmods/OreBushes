package com.thenatekirby.orebushes.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.thenatekirby.orebushes.registration.OreBushesBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class OreBushesLootTablesProvider extends LootTableProvider {
    OreBushesLootTablesProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
    }

    @Override
    @Nonnull
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(OreBushesBlockLootTables::new, LootParameterSets.BLOCK));
    }

    @Override
    @Nonnull
    public String getName() {
        return "orebushes:loot_tables";
    }

    private static class OreBushesBlockLootTables extends BlockLootTables {
        @Override
        protected void addTables() {
            dropSelf(OreBushesBlocks.TIER_ONE_FARMLAND.asBlock());
            dropSelf(OreBushesBlocks.TIER_TWO_FARMLAND.asBlock());
            dropSelf(OreBushesBlocks.TIER_THREE_FARMLAND.asBlock());
        }

        @Override
        @Nonnull
        protected Iterable<Block> getKnownBlocks() {
            return Arrays.asList(
                    OreBushesBlocks.TIER_ONE_FARMLAND.asBlock(),
                    OreBushesBlocks.TIER_TWO_FARMLAND.asBlock(),
                    OreBushesBlocks.TIER_THREE_FARMLAND.asBlock()
            );
        }
    }
}
