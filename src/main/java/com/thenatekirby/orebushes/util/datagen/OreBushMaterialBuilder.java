package com.thenatekirby.orebushes.util.datagen;

import com.google.gson.JsonObject;
import com.thenatekirby.orebushes.registry.material.MaterialRarity;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import com.thenatekirby.orebushes.registry.material.MaterialType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class OreBushMaterialBuilder {
    private final ResourceLocation materialId;

    private String requiredMod;

    private String name;

    private MaterialRarity rarity;

    private MaterialType type;

    private MaterialTier tier;

    private String color;

    private boolean grindable;

    private OreBushMaterialBuilder(@Nonnull ResourceLocation materialId) {
        this.materialId = materialId;
    }

    public static OreBushMaterialBuilder builder(@Nonnull ResourceLocation materialId) {
        return new OreBushMaterialBuilder(materialId);
    }

    // ====---------------------------------------------------------------------------====
    // region Builder

    public OreBushMaterialBuilder withRequiredMod(@Nonnull String modId) {
        this.requiredMod = modId;
        return this;
    }

    public OreBushMaterialBuilder withName(@Nonnull String name) {
        this.name = name;
        return this;
    }

    public OreBushMaterialBuilder withRarity(@Nonnull MaterialRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public OreBushMaterialBuilder withTier(@Nonnull MaterialTier tier) {
        this.tier = tier;
        return this;
    }

    public OreBushMaterialBuilder withType(@Nonnull MaterialType type) {
        this.type = type;
        return this;
    }

    public OreBushMaterialBuilder withColor(@Nonnull String color) {
        this.color = color;
        return this;
    }

    public OreBushMaterialBuilder isGrindable(boolean isGrindable) {
        this.grindable = isGrindable;
        return this;
    }

    public void build(Consumer<IFinishedMaterial> consumer) {
        this.build(consumer, materialId);
    }

    public void build(Consumer<IFinishedMaterial> consumer, @Nonnull ResourceLocation path) {
        consumer.accept(new Result(path, materialId, requiredMod, name, grindable, rarity, tier, type, color));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Result

    private static class Result implements IFinishedMaterial {
        private final ResourceLocation path;

        private final ResourceLocation materialId;

        private final String name;

        private final String requiresMod;

        private final boolean grindable;

        private final MaterialRarity rarity;

        private final MaterialTier tier;

        private final MaterialType type;

        private final String color;

        Result(ResourceLocation path, ResourceLocation materialId, String requiredMod, String name, boolean grindable, MaterialRarity rarity, MaterialTier tier, MaterialType type, String color) {
            this.path = path;
            this.materialId = materialId;
            this.requiresMod = requiredMod;
            this.grindable = grindable;
            this.name = name;
            this.rarity = rarity;
            this.tier = tier;
            this.type = type;
            this.color = color;
        }

        @Override
        public void serializeJson(@Nonnull JsonObject jsonObject) {
            if (this.requiresMod != null) {
                jsonObject.addProperty("requires_mod", this.requiresMod);
            }

            jsonObject.addProperty("id", materialId.toString());
            jsonObject.addProperty("name", name);

            JsonObject infoJson = new JsonObject();
            infoJson.addProperty("color", color);
            infoJson.addProperty("rarity", rarity.serializeJson());
            infoJson.addProperty("tier", tier.serializeJson());
            infoJson.addProperty("type", type.serializeJson());
            infoJson.addProperty("grindable", grindable);

            jsonObject.add("info", infoJson);
            jsonObject.addProperty("enabled", true);
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return path;
        }
    }

    // endregion
}
