package com.thenatekirby.orebushes.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.thenatekirby.orebushes.OreBushes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// ====---------------------------------------------------------------------------====

@Mod.EventBusSubscriber
public class OreBushesConfig {
    private static final String CATEGORY_GENERAL = "Integrations";

    private static ForgeConfigSpec COMMON_CONFIG;

    // Integrations
    private static ForgeConfigSpec.BooleanValue MEKANISM;
    private static ForgeConfigSpec.BooleanValue THERMAL;
    private static ForgeConfigSpec.BooleanValue BOTANY_POTS;
    private static ForgeConfigSpec.BooleanValue CREATE;

    // Setup
    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Integration Config");
        COMMON_BUILDER.push(CATEGORY_GENERAL);

        BOTANY_POTS = COMMON_BUILDER.comment("Whether or not to allow Ore Bushes to be grown in Botany Pots")
                .define("integration_botany_pots", true);

        CREATE = COMMON_BUILDER.comment("Whether or not to include Create Machine Recipes for Ore Bush Berries")
                .define("integration_create", true);

        MEKANISM = COMMON_BUILDER.comment("Whether or not to include Mekanism Machine Recipes for Ore Bush Berries")
                .define("integration_mekanism", true);

        THERMAL = COMMON_BUILDER.comment("Whether or not to include Thermal Machine Recipes for Ore Bush Seeds & Berries")
                .define("integration_thermal", true);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static boolean isIntegrationEnabled(@Nonnull String integration) {
        switch (integration) {
            case "botanypots": {
                return BOTANY_POTS.get();
            }
            case "create": {
                return CREATE.get();
            }
            case "mekanism": {
                return MEKANISM.get();
            }
            case "thermal": {
                return THERMAL.get();
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }

    public static void init() {
        OreBushesConfig.makeConfigDir(FMLPaths.CONFIGDIR.get().resolve("OreBushes"));
        OreBushesConfig.makeConfigDir(FMLPaths.CONFIGDIR.get().resolve("OreBushes/materials"));
        OreBushesConfig.loadConfig(OreBushesConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("OreBushes/common.toml"));
    }

    private static void makeConfigDir(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            OreBushes.getLogger().fatal(e.getMessage());
        }
    }

    private static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
