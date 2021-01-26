package com.thenatekirby.orebushes.registration;

import com.thenatekirby.orebushes.OreBushes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

// ====---------------------------------------------------------------------------====

public class OreBushesTags {
    public static void init() {
        Blocks.init();
    }

    public static class Blocks {
        private static void init() {}

        public static final Tags.IOptionalNamedTag<Block> TIER_ONE_FARMLAND = tag("tier_one_farmland");
        public static final Tags.IOptionalNamedTag<Block> TIER_TWO_FARMLAND = tag("tier_two_farmland");
        public static final Tags.IOptionalNamedTag<Block> TIER_THREE_FARMLAND = tag("tier_three_farmland");

        private static Tags.IOptionalNamedTag<Block> tag(String name) {
            return BlockTags.createOptional(OreBushes.MOD.withPath(name));
        }
    }

    public static class Items {
        private static void init() {}

        public static final Tags.IOptionalNamedTag<Item> TIER_ONE_FARMLAND = tag("tier_one_farmland");
        public static final Tags.IOptionalNamedTag<Item> TIER_TWO_FARMLAND = tag("tier_two_farmland");
        public static final Tags.IOptionalNamedTag<Item> TIER_THREE_FARMLAND = tag("tier_three_farmland");

        private static Tags.IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(OreBushes.MOD.withPath(name));
        }
    }
}
