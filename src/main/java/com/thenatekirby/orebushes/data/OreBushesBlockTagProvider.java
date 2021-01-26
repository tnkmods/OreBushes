package com.thenatekirby.orebushes.data;

import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.registration.OreBushesBlocks;
import com.thenatekirby.orebushes.registration.OreBushesTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class OreBushesBlockTagProvider extends BlockTagsProvider {
    OreBushesBlockTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, OreBushes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(OreBushesTags.Blocks.TIER_ONE_FARMLAND).add(
                OreBushesBlocks.TIER_ONE_FARMLAND.asBlock(),
                OreBushesBlocks.TIER_TWO_FARMLAND.asBlock(),
                OreBushesBlocks.TIER_THREE_FARMLAND.asBlock()
        );

        getOrCreateBuilder(OreBushesTags.Blocks.TIER_TWO_FARMLAND).add(
                OreBushesBlocks.TIER_TWO_FARMLAND.asBlock(),
                OreBushesBlocks.TIER_THREE_FARMLAND.asBlock()
        );

        getOrCreateBuilder(OreBushesTags.Blocks.TIER_THREE_FARMLAND).add(
                OreBushesBlocks.TIER_THREE_FARMLAND.asBlock()
        );
    }

    @Override
    @Nonnull
    public String getName() {
        return "orebushes:block_tags";
    }
}
