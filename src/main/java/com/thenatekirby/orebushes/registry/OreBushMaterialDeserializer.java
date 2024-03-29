package com.thenatekirby.orebushes.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.registry.material.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

// ====---------------------------------------------------------------------------====

public class OreBushMaterialDeserializer {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final Path path;

    OreBushMaterialDeserializer(@Nonnull Path path) {
        this.path = path;
    }

    @Nullable
    OreBushMaterial deserialize() {
        try {
            InputStream inputStream = Files.newInputStream(path);
            Reader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            JsonObject json = GSON.fromJson(fileReader, JsonObject.class);

            if (json.has("requires_mod")) {
                String modId = JSONUtils.getAsString(json, "requires_mod");
                if (!ModList.get().isLoaded(modId)) {
                    return null;
                }
            }

            ResourceLocation materialId = new ResourceLocation(JSONUtils.getAsString(json, "id"));

            JsonObject infoJson = JSONUtils.getAsJsonObject(json, "info");
            MaterialRarity rarity = MaterialRarity.fromJson(JSONUtils.getAsString(infoJson, "rarity", "unknown"));
            if (rarity == null) {
                OreBushes.getLogger().info("OreBush id: {} has unknown rarity {}, skipping.", materialId, JSONUtils.getAsString(infoJson, "rarity"));
                return null;
            }

            MaterialType type = MaterialType.fromJson(JSONUtils.getAsString(infoJson, "type", "unknown"));
            if (type == null) {
                OreBushes.getLogger().info("OreBush id: {} has unknown type {}, skipping.", materialId, JSONUtils.getAsString(infoJson, "type"));
                return null;
            }

            MaterialTier tier = MaterialTier.fromJson(JSONUtils.getAsInt(infoJson, "tier", -1));
            if (tier == null) {
                OreBushes.getLogger().info("OreBush id: {} has unknown tier {}, skipping.", materialId, JSONUtils.getAsInt(infoJson, "tier"));
                return null;
            }

            long color = Long.parseLong(JSONUtils.getAsString(infoJson, "color"), 16);
            boolean mulchable = JSONUtils.getAsBoolean(infoJson, "grindable", true);

            MaterialInfo materialInfo = MaterialInfo.make(type, tier, rarity, (int) color, mulchable);

            String name = JSONUtils.getAsString(json, "name");
            boolean isEnabled = JSONUtils.getAsBoolean(json, "enabled", true);

            return new OreBushMaterial(materialId, name, materialInfo, isEnabled);

        } catch (IOException e) {
            return null;
        }
    }
}
