package com.thenatekirby.orebushes.registry.material;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class MaterialTier {
    private final int tier;

    private MaterialTier(int tier) {
        this.tier = tier;
    }

    // ====---------------------------------------------------------------------------====

    public static final MaterialTier TIER_ZERO = new MaterialTier(0);
    public static final MaterialTier TIER_ONE = new MaterialTier(1);
    public static final MaterialTier TIER_TWO = new MaterialTier(2);
    public static final MaterialTier TIER_THREE = new MaterialTier(3);

    private static List<MaterialTier> tiers = Arrays.asList(TIER_ZERO, TIER_ONE, TIER_TWO, TIER_THREE);

    @SuppressWarnings("WeakerAccess")
    public static List<MaterialTier> getAllTiers() {
        return tiers;
    }

    // ====---------------------------------------------------------------------------====
    // region Info

    @SuppressWarnings("unused")
    public int getRawTier() {
        return tier;
    }

    public boolean isMaxTier() {
        for (MaterialTier materialTier : tiers) {
            if (materialTier.tier > tier) {
                return false;
            }
        }

        return true;
    }

    public boolean isMinTier() {
        for (MaterialTier materialTier : tiers) {
            if (materialTier.tier < tier) {
                return false;
            }
        }

        return true;
    }

    public String getDisplayName() {
        switch (tier) {
            case 1: {
                return "Tier I";
            }
            case 2: {
                return "Tier II";
            }
            case 3: {
                return "Tier III";
            }
            default: {
                return "";
            }
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Serialization

    @Nullable
    public static MaterialTier fromJson(int json) {
        for (MaterialTier tier : getAllTiers()) {
            if (tier.tier == json) {
                return tier;
            }
        }

        return null;
    }

    public int serializeJson() {
        return tier;
    }
}
