package com.thenatekirby.orebushes.registry;

import com.thenatekirby.orebushes.registration.OreBushesTags;
import com.thenatekirby.orebushes.registry.material.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class OreBushMaterial {
    @Nonnull
    private final MaterialInfo info;

    @Nonnull
    private final String name;

    @Nonnull
    private final ResourceLocation materialId;

    private final boolean enabled;

    // ====---------------------------------------------------------------------------====

    OreBushMaterial(@Nonnull ResourceLocation materialId, @Nonnull String name, @Nonnull MaterialInfo info, boolean enabled) {
        this.materialId = materialId;
        this.name = name;
        this.info = info;
        this.enabled = enabled;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public ResourceLocation getMaterialId() {
        return materialId;
    }

    @Nonnull
    public MaterialInfo getInfo() {
        return info;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Info

    public Tags.IOptionalNamedTag<Block> getFarmlandTag() {
        MaterialTier tier = getInfo().getTier();
        if (tier == MaterialTier.TIER_ONE) {
            return OreBushesTags.Blocks.TIER_ONE_FARMLAND;
        } else if (tier == MaterialTier.TIER_TWO) {
            return OreBushesTags.Blocks.TIER_TWO_FARMLAND;
        } else {
            return OreBushesTags.Blocks.TIER_THREE_FARMLAND;
        }
    }

    public int getCropGrowthChance() {
        MaterialRarity rarity = getInfo().getRarity();
        if (rarity == MaterialRarity.COMMON) {
            return 20;
        } else if (rarity == MaterialRarity.UNCOMMON) {
            return 15;
        } else if (rarity == MaterialRarity.RARE) {
            return 10;
        } else if (rarity == MaterialRarity.EPIC) {
            return 5;
        }

        return 5;
    }

    public int getCropResultCount() {
        MaterialType type = getInfo().getType();
        if (type == MaterialType.DUST) {
            return 4;
        } else if (type == MaterialType.NATURAL) {
            return 4;
        } else if (type == MaterialType.METAL) {
            return 2;
        } else if (type == MaterialType.GEM) {
            return 1;
        }

        return 1;
    }

    // endregion
}
