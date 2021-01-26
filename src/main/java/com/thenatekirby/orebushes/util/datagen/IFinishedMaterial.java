package com.thenatekirby.orebushes.util.datagen;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface IFinishedMaterial {
    void serializeJson(@Nonnull JsonObject jsonObject);

    @Nonnull
    ResourceLocation getId();
}
