package com.thenatekirby.orebushes.item;

import com.thenatekirby.orebushes.Localization;
import com.thenatekirby.orebushes.registration.OreBushesItemGroup;
import com.thenatekirby.orebushes.registry.OreBush;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

import net.minecraft.item.Item.Properties;

public class GroundsItem extends Item {
    private static final Properties ITEM_PROPERTIES = new Properties().tab(OreBushesItemGroup.getItemGroup());

    private OreBush oreBush;

    public GroundsItem(OreBush oreBush) {
        super(ITEM_PROPERTIES);
        this.oreBush = oreBush;
    }

    @Override
    public void fillItemCategory(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.oreBush.isEnabled()) {
            super.fillItemCategory(group, items);
        }
    }

    @Override
    @Nonnull
    public ITextComponent getName(@Nonnull ItemStack stack) {
        return Localization.GROUNDS.withReplacement("MATERIAL", oreBush.getName()).makeTextComponent();
    }
}
