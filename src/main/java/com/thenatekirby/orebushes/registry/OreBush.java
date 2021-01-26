package com.thenatekirby.orebushes.registry;

import net.minecraft.block.Block;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Optional;

// ====---------------------------------------------------------------------------====

public class OreBush {
    @Nonnull
    private final ResourceLocation materialId;

    @Nonnull
    private final OreBushMaterial material;

    private Block bushBlock;

    private IItemProvider seedItem;

    private IItemProvider berryItem;

    private IItemProvider groundsItem;

    // ====---------------------------------------------------------------------------====

    public OreBush(@Nonnull ResourceLocation materialId, @Nonnull OreBushMaterial material) {
        this.materialId = materialId;
        this.material = material;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    @Nonnull
    public ResourceLocation getMaterialId() {
        return materialId;
    }

    @Nonnull
    public OreBushMaterial getMaterial() {
        return material;
    }

    @Nonnull
    public String getName() {
        return getMaterial().getName();
    }

    @Nonnull
    public Optional<Block> getBushBlock() {
        return Optional.ofNullable(bushBlock);
    }

    @Nonnull
    public Optional<IItemProvider> getSeedItem() {
        return Optional.ofNullable(seedItem);
    }

    @Nonnull
    public Optional<IItemProvider> getBerryItem() {
        return Optional.ofNullable(berryItem);
    }

    @Nonnull
    public Optional<IItemProvider> getGroundsItem() {
        return Optional.ofNullable(groundsItem);
    }

    void setBushBlock(@Nonnull Block bushBlock) {
        this.bushBlock = bushBlock;
    }

    void setSeedItem(@Nonnull IItemProvider seedItem) {
        this.seedItem = seedItem;
    }

    void setBerryItem(@Nonnull IItemProvider berryItem) {
        this.berryItem = berryItem;
    }

    void setGroundsItem(@Nonnull IItemProvider groundsItem) {
        this.groundsItem = groundsItem;
    }

    public boolean isEnabled() {
        return this.material.isEnabled();
    }

    // endregion
}
