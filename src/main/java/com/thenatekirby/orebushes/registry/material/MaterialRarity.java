package com.thenatekirby.orebushes.registry.material;

import com.thenatekirby.orebushes.OreBushes;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class MaterialRarity {
    private final ResourceLocation rarityId;

    private MaterialRarity(@Nonnull ResourceLocation rarityId) {
        this.rarityId = rarityId;
    }

    // ====---------------------------------------------------------------------------====

    public static final MaterialRarity COMMON = new MaterialRarity(OreBushes.MOD.withPath("common"));
    public static final MaterialRarity UNCOMMON = new MaterialRarity(OreBushes.MOD.withPath("uncommon"));
    public static final MaterialRarity RARE = new MaterialRarity(OreBushes.MOD.withPath("rare"));
    public static final MaterialRarity EPIC = new MaterialRarity(OreBushes.MOD.withPath("epic"));

    private static List<MaterialRarity> rarities = Arrays.asList(COMMON, UNCOMMON, RARE, EPIC);

    public String serializeJson() {
        return rarityId.getPath();
    }

    // ====---------------------------------------------------------------------------====
    // region Serialization

    @Nullable
    public static MaterialRarity fromJson(@Nonnull String json) {
        for (MaterialRarity rarity : getAllRarities()) {
            if (rarity.rarityId.getPath().equals(json.toLowerCase())) {
                return rarity;
            }
        }

        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public static List<MaterialRarity> getAllRarities() {
        return rarities;
    }

    // endregion
}
