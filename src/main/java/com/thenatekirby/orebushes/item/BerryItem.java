package com.thenatekirby.orebushes.item;

import com.thenatekirby.orebushes.Localization;
import com.thenatekirby.orebushes.registration.OreBushesItemGroup;
import com.thenatekirby.orebushes.registry.OreBush;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class BerryItem extends Item {
    private static final Food ORE_BERRY = (new Food.Builder()).nutrition(2).saturationMod(0.8F).effect(new EffectInstance(Effects.POISON, 50, 0), 1.0F).build();
    private static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(OreBushesItemGroup.getItemGroup()).food(ORE_BERRY);

    private final OreBush oreBush;

    public BerryItem(OreBush oreBush) {
        super(ITEM_PROPERTIES);
        this.oreBush = oreBush;
    }

    // ====---------------------------------------------------------------------------====
    // region Info

    @Override
    @Nonnull
    public ITextComponent getName(@Nonnull ItemStack stack) {
        return Localization.BERRIES.withReplacement("MATERIAL", oreBush.getName()).makeTextComponent();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        oreBush.getBushBlock().ifPresent(bushBlock -> {
            String bushBlockName = bushBlock.getName().getString();
            tooltip.add(Localization.BERRY_DESC.withReplacement("BUSH", bushBlockName).makeTextComponent().withStyle(TextFormatting.GRAY));
        });

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
