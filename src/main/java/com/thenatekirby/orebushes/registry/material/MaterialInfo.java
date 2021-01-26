package com.thenatekirby.orebushes.registry.material;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class MaterialInfo {
    @Nonnull
    private final MaterialType type;

    @Nonnull
    private final MaterialTier tier;

    @Nonnull
    private final MaterialRarity rarity;

    private final int color;

    private final boolean grindable;

    // ====---------------------------------------------------------------------------====

    private MaterialInfo(@Nonnull MaterialType type, @Nonnull MaterialTier tier, @Nonnull MaterialRarity rarity, int color, boolean grindable) {
        this.type = type;
        this.tier = tier;
        this.rarity = rarity;
        this.color = color;
        this.grindable = grindable;
    }

    public static MaterialInfo make(@Nonnull MaterialType type, @Nonnull MaterialTier tier, @Nonnull MaterialRarity rarity, int color, boolean grindable) {
        return new MaterialInfo(type, tier, rarity, color, grindable);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    @Nonnull
    public MaterialType getType() {
        return type;
    }

    @Nonnull
    public MaterialRarity getRarity() {
        return rarity;
    }

    @Nonnull
    public MaterialTier getTier() {
        return tier;
    }

    public int getColor() {
        return color;
    }

    public boolean isGrindable() {
        return grindable;
    }

    // endregion
}
