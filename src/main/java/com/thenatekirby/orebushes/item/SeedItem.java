package com.thenatekirby.orebushes.item;

import com.thenatekirby.orebushes.Localization;
import com.thenatekirby.orebushes.registration.OreBushesItemGroup;
import com.thenatekirby.orebushes.registry.OreBush;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class SeedItem extends BlockNamedItem {
    private static Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(OreBushesItemGroup.getItemGroup());
    private OreBush oreBush;

    public SeedItem(OreBush oreBush) {
        super(oreBush.getBushBlock().orElseThrow(() -> new IllegalStateException("Missing Block For " + oreBush.getMaterialId().toString())).getBlock(), ITEM_PROPERTIES);
        this.oreBush = oreBush;
    }

    // ====---------------------------------------------------------------------------====
    // region Info

    @Override
    @Nonnull
    public ITextComponent getName(@Nonnull ItemStack stack) {
        return Localization.SEEDS.withReplacement("MATERIAL", oreBush.getName()).makeTextComponent();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        MaterialTier tier = oreBush.getMaterial().getInfo().getTier();
        if (!tier.isMinTier()) {
            String displayName = tier.getDisplayName();
            if (tier.isMaxTier()) {
                tooltip.add(Localization.SEEDS_DESC_SINGLE.withReplacement("TIER", displayName).makeTextComponent().withStyle(TextFormatting.YELLOW));
            } else {
                tooltip.add(Localization.SEEDS_DESC_MULTI.withReplacement("TIER", displayName).makeTextComponent().withStyle(TextFormatting.YELLOW));
            }
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Item Group

    @Override
    public void fillItemCategory(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.oreBush.isEnabled()) {
            super.fillItemCategory(group, items);
        }
    }

    // endregion
}

