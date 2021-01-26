package com.thenatekirby.orebushes.item;

import com.thenatekirby.orebushes.Localization;
import com.thenatekirby.orebushes.registration.OreBushItemGroup;
import com.thenatekirby.orebushes.registry.OreBush;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class SeedItem extends BlockNamedItem {
    private static Item.Properties ITEM_PROPERTIES = new Item.Properties().group(OreBushItemGroup.getItemGroup());
    private OreBush oreBush;

    public SeedItem(OreBush oreBush) {
        super(oreBush.getBushBlock().orElseThrow(() -> new IllegalStateException("Missing Block For " + oreBush.getMaterialId().toString())).getBlock(), ITEM_PROPERTIES);
        this.oreBush = oreBush;
    }

    // ====---------------------------------------------------------------------------====
    // region Info

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return Localization.SEEDS.withReplacement("MATERIAL", oreBush.getName()).makeTextComponent();
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        MaterialTier tier = oreBush.getMaterial().getInfo().getTier();
        if (!tier.isMinTier()) {
            String displayName = tier.getDisplayName();
            if (tier.isMaxTier()) {
                tooltip.add(Localization.SEEDS_DESC_SINGLE.withReplacement("TIER", displayName).makeTextComponent().mergeStyle(TextFormatting.YELLOW));
            } else {
                tooltip.add(Localization.SEEDS_DESC_MULTI.withReplacement("TIER", displayName).makeTextComponent().mergeStyle(TextFormatting.YELLOW));
            }
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Item Group

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.oreBush.isEnabled()) {
            super.fillItemGroup(group, items);
        }
    }

    // endregion
}

