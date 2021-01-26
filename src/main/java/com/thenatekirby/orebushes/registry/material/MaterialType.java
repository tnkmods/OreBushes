package com.thenatekirby.orebushes.registry.material;

import com.thenatekirby.orebushes.OreBushes;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class MaterialType {
    private final ResourceLocation typeId;

    private MaterialType(@Nonnull ResourceLocation typeId) {
        this.typeId = typeId;
    }

    // ====---------------------------------------------------------------------------====

    public static final MaterialType NATURAL = new MaterialType(OreBushes.MOD.withPath("natural"));
    public static final MaterialType GEM = new MaterialType(OreBushes.MOD.withPath("gem"));
    public static final MaterialType DUST = new MaterialType(OreBushes.MOD.withPath("dust"));
    public static final MaterialType METAL = new MaterialType(OreBushes.MOD.withPath("metal"));

    private static List<MaterialType> materialTypes = Arrays.asList(NATURAL, GEM, DUST, METAL);

    @SuppressWarnings("WeakerAccess")
    public static List<MaterialType> getAllMaterialTypes() {
        return materialTypes;
    }

    // ====---------------------------------------------------------------------------====
    // region Serialization

    @Nullable
    public static MaterialType fromJson(@Nonnull String json) {
        for (MaterialType materialType : getAllMaterialTypes()) {
            if (materialType.typeId.getPath().equals(json.toLowerCase())) {
                return materialType;
            }
        }

        return null;
    }

    @Nonnull
    public String serializeJson() {
        return typeId.getPath();
    }

    // endregion
}
