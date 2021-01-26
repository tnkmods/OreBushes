package com.thenatekirby.orebushes.util.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thenatekirby.orebushes.OreBushes;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public abstract class OreBushesMaterialsProvider implements IDataProvider {
    private DataGenerator generator;
    private final String  modId;
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public OreBushesMaterialsProvider(@Nonnull String modId, DataGenerator generatorIn) {
        this.modId = modId;
        this.generator = generatorIn;
    }

    public abstract void registerMaterials(Consumer<IFinishedMaterial> consumer);

    @Override
    public void act(@Nonnull DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();

        registerMaterials(material -> {
            if (!set.add(material.getId())) {
                throw new IllegalStateException("Duplicate recipe " + material.getId());

            } else {
                JsonObject jsonObject = new JsonObject();
                material.serializeJson(jsonObject);

                saveMaterial(cache, jsonObject, path.resolve("data/" + material.getId().getNamespace() + "/orebushes_materials/" + material.getId().getPath() + ".json"));
            }
        });
    }

    private static void saveMaterial(DirectoryCache cache, JsonObject recipeJson, Path outputPath) {
        try {
            String json = GSON.toJson(recipeJson);
            String hash = HASH_FUNCTION.hashUnencodedChars(json).toString();
            if (!Objects.equals(cache.getPreviousHash(outputPath), hash) || !Files.exists(outputPath)) {
                Files.createDirectories(outputPath.getParent());

                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(outputPath)) {
                    bufferedwriter.write(json);
                }
            }

            cache.recordHash(outputPath, hash);

        } catch (IOException ioexception) {
            OreBushes.getLogger().error("Couldn't save recipe {}", recipeJson, ioexception);
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return modId + ":orebushes_materials";
    }
}
