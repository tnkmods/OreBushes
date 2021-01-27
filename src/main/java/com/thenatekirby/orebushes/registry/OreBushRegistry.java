package com.thenatekirby.orebushes.registry;

import com.google.common.base.Stopwatch;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.util.ModUtil;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.block.OreBushBlock;
import com.thenatekirby.orebushes.config.OreBushesConfig;
import com.thenatekirby.orebushes.item.BerryItem;
import com.thenatekirby.orebushes.item.GroundsItem;
import com.thenatekirby.orebushes.item.SeedItem;
import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

// ====---------------------------------------------------------------------------====

public class OreBushRegistry {
    public static final OreBushRegistry INSTANCE = new OreBushRegistry();
    private static final String MATERIALS_LOCATION = "orebushes_materials";
    private boolean isSetup = false;

    private Map<ResourceLocation, OreBush> registry = new HashMap<>();

    private OreBushRegistry() {
    }

    // ====---------------------------------------------------------------------------====

    private void init() {
        if (isSetup) {
            return;
        }

        Stopwatch timer = Stopwatch.createStarted();
        List<ModInfo> mods = ModList.get().getMods();
        Map<ModInfo, List<Path>> materialJsons = new HashMap<>();
        Map<ResourceLocation, OreBushMaterial> materials = new HashMap<>();

        mods.forEach(modInfo -> {
            String modId = modInfo.getModId();
            List<Path> materialJsonPaths = getMaterialJsonPaths(modInfo, String.format("data/%s/%s", modId, MATERIALS_LOCATION));
            if (!materialJsonPaths.isEmpty()) {
                materialJsons.put(modInfo, materialJsonPaths);
            }
        });

        for (Map.Entry<ModInfo, List<Path>> entries : materialJsons.entrySet()) {
            List<Path> paths = entries.getValue();
            for (Path path : paths) {
                OreBushMaterial material = new OreBushMaterialDeserializer(path).deserialize();
                if (material != null) {
                    materials.put(material.getMaterialId(), material);
                }
            }
        }

        Path configPath = FMLPaths.CONFIGDIR.get().resolve("OreBushes/materials");
        List<Path> configMaterialPaths = getJsonPathsInDirectory(configPath);
        for (Path path : configMaterialPaths) {
            OreBushMaterial material = new OreBushMaterialDeserializer(path).deserialize();
            if (material != null) {
                materials.put(material.getMaterialId(), material);
            }
        }

        for (Map.Entry<ResourceLocation, OreBushMaterial> entry : materials.entrySet()) {
            registry.put(entry.getKey(), new OreBush(entry.getKey(),  entry.getValue()));
        }

        OreBushes.getLogger().info("Loaded {} OreBushes in {} ms", materials.values().size(), timer.elapsed(TimeUnit.MILLISECONDS));
        timer.stop();
        isSetup = true;
    }

    private List<Path> getMaterialJsonPaths(ModInfo mod, String basePath) {
        if (mod.getModId().equals("minecraft") || mod.getModId().equals("forge")) {
            return Collections.emptyList();
        }

        ModFileInfo modFileInfo = mod.getOwningFile();
        Path source =  modFileInfo.getFile().getFilePath();
        Path materialsPath;

        boolean isModJar = Files.isRegularFile(source);
        if (isModJar) {
            try {
                FileSystem fs = FileSystems.newFileSystem(source, null);
                materialsPath = fs.getPath("/", basePath);

            } catch (IOException e) {
                return Collections.emptyList();
            }
        } else {
            materialsPath = source.resolve(basePath);
        }

        boolean directoryExists = Files.exists(materialsPath) && Files.isDirectory(materialsPath);
        if (!directoryExists) {
            return Collections.emptyList();
        }

        return getJsonPathsInDirectory(materialsPath);
    }

    private List<Path> getJsonPathsInDirectory(Path directoryPath) {
        List<Path> paths = new ArrayList<>();
        try {
            Files.list(directoryPath)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(paths::add);

            Files.list(directoryPath)
                    .filter(path -> Files.isDirectory(path))
                    .forEach(dir -> paths.addAll(getJsonPathsInDirectory(dir)));

        } catch (IOException e) {
            return Collections.emptyList();
        }

        return paths;
    }

    public void onRegisterBlocks(IForgeRegistry<Block> registry) {
        this.init();

        getAllOreBushes().forEach(oreBush -> {
            OreBushBlock bush = new OreBushBlock(oreBush);
            ResourceLocation bushId = OreBushes.MOD.withPath("bush_" + oreBush.getMaterialId().getPath());
            bush.setRegistryName(bushId);

            registry.register(bush);
            oreBush.setBushBlock(bush);
        });
    }

    public void onRegisterItems(IForgeRegistry<Item> registry) {
        this.init();

        getAllOreBushes().forEach(bush -> {
            // Seed
            SeedItem seed = new SeedItem(bush);
            ResourceLocation seedId = OreBushes.MOD.withPath("seed_" + bush.getMaterialId().getPath());
            seed.setRegistryName(seedId);

            registry.register(seed);
            bush.setSeedItem(seed);

            // Berry
            BerryItem berry = new BerryItem(bush);
            ResourceLocation berryId = OreBushes.MOD.withPath("berry_" + bush.getMaterialId().getPath());
            berry.setRegistryName(berryId);

            registry.register(berry);
            bush.setBerryItem(berry);

            ComposterBlock.CHANCES.putIfAbsent(seed, 0.3f);
            ComposterBlock.CHANCES.putIfAbsent(berry, 0.3f);

            // Grounds
            if (isGrindingAvailable()) {
                GroundsItem grounds = new GroundsItem(bush);
                ResourceLocation mulchId = OreBushes.MOD.withPath("grounds_" + bush.getMaterialId().getPath());
                grounds.setRegistryName(mulchId);
                registry.register(grounds);
                bush.setGroundsItem(grounds);

                ComposterBlock.CHANCES.putIfAbsent(grounds, 0.3f);
            }
        });
    }

    @Nullable
    public OreBush getOreBushById(@Nonnull ResourceLocation materialId) {
        return registry.get(materialId);
    }

    public Collection<OreBush> getAllOreBushes() {
        return registry.values();
    }

    public boolean isGrindingAvailable() {
        if (!OreBushesConfig.GRINDING.get()) {
            return false;
        }

        return ModUtil.isAnyModLoaded(Mods.MEKANISM, Mods.THERMAL, Mods.CREATE);
    }
}
