package com.thenatekirby.orebushes.data;

import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.registration.OreBushesBlocks;
import com.thenatekirby.orebushes.registration.OreBushesTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class OreBushesItemTagProvider extends ItemTagsProvider {
    OreBushesItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, OreBushes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(OreBushesTags.Items.TIER_ONE_FARMLAND).add(
                OreBushesBlocks.TIER_ONE_FARMLAND.asBlock().asItem(),
                OreBushesBlocks.TIER_TWO_FARMLAND.asBlock().asItem(),
                OreBushesBlocks.TIER_THREE_FARMLAND.asBlock().asItem()
        );

        getOrCreateBuilder(OreBushesTags.Items.TIER_TWO_FARMLAND).add(
                OreBushesBlocks.TIER_TWO_FARMLAND.asBlock().asItem(),
                OreBushesBlocks.TIER_THREE_FARMLAND.asBlock().asItem()
        );

        getOrCreateBuilder(OreBushesTags.Items.TIER_THREE_FARMLAND).add(
                OreBushesBlocks.TIER_THREE_FARMLAND.asBlock().asItem()
        );
    }

    @Override
    @Nonnull
    public String getName() {
        return "orebushes:item_tags";
    }
}
