package com.thenatekirby.orebushes.registry;

import com.thenatekirby.orebushes.config.OreBushesConfig;
import com.thenatekirby.orebushes.registration.OreBushesTags;
import com.thenatekirby.orebushes.registry.material.*;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
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
        if (tier == MaterialTier.TIER_ZERO) {
            return Tags.Blocks.DIRT;
        } else if (tier == MaterialTier.TIER_ONE) {
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
            return OreBushesConfig.CHANCE_COMMON.get();
        } else if (rarity == MaterialRarity.UNCOMMON) {
            return OreBushesConfig.CHANCE_UNCOMMON.get();
        } else if (rarity == MaterialRarity.RARE) {
            return OreBushesConfig.CHANCE_RARE.get();
        } else if (rarity == MaterialRarity.EPIC) {
            return OreBushesConfig.CHANCE_EPIC.get();
        }

        return OreBushesConfig.CHANCE_EPIC.get();
    }

    public int getCropResultCount() {
        MaterialType type = getInfo().getType();
        if (type == MaterialType.DUST) {
            return OreBushesConfig.DROPS_DUST.get();
        } else if (type == MaterialType.NATURAL) {
            return OreBushesConfig.DROPS_NATURAL.get();
        } else if (type == MaterialType.METAL) {
            return OreBushesConfig.DROPS_METAL.get();
        } else if (type == MaterialType.GEM) {
            return OreBushesConfig.DROPS_GEM.get();
        }

        return OreBushesConfig.DROPS_GEM.get();
    }

    // endregion
}
