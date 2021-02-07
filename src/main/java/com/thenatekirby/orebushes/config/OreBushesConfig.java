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
    private static final String CATEGORY_GENERAL = "General";
    private static final String CATEGORY_INTEGRATIONS = "Integrations";
    private static final String CATEGORY_GROWTH = "Growth";
    private static final String CATEGORY_DROPS = "Drops";

    private static ForgeConfigSpec COMMON_CONFIG;

    // Bushes
    public static ForgeConfigSpec.IntValue CHANCE_COMMON;
    public static ForgeConfigSpec.IntValue CHANCE_UNCOMMON;
    public static ForgeConfigSpec.IntValue CHANCE_RARE;
    public static ForgeConfigSpec.IntValue CHANCE_EPIC;

    // Drops
    public static ForgeConfigSpec.IntValue DROPS_NATURAL;
    public static ForgeConfigSpec.IntValue DROPS_DUST;
    public static ForgeConfigSpec.IntValue DROPS_GEM;
    public static ForgeConfigSpec.IntValue DROPS_METAL;

    // Integrations
    private static ForgeConfigSpec.BooleanValue MEKANISM;
    private static ForgeConfigSpec.BooleanValue THERMAL;
    private static ForgeConfigSpec.BooleanValue BOTANY_POTS;
    private static ForgeConfigSpec.BooleanValue CREATE;

    // General
    public static ForgeConfigSpec.BooleanValue GRINDING;
    public static ForgeConfigSpec.BooleanValue DROP_FROM_GRASS;

    // Setup
    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Growth Config");
        COMMON_BUILDER.push(CATEGORY_GROWTH);

        CHANCE_COMMON = COMMON_BUILDER.comment("Chance per growth tick for a common ore bush to grow, default is 20")
                .defineInRange("chance_common", 20, 1, 100);

        CHANCE_UNCOMMON = COMMON_BUILDER.comment("Chance per growth tick for an uncommon ore bush to grow, default is 15")
                .defineInRange("chance_uncommon", 15, 1, 100);

        CHANCE_RARE = COMMON_BUILDER.comment("Chance per growth tick for a rare ore bush to grow, default is 10")
                .defineInRange("chance_rare", 10, 1, 100);

        CHANCE_EPIC = COMMON_BUILDER.comment("Chance per growth tick for a epic ore bush to grow, default is 5")
                .defineInRange("chance_epic", 5, 1, 100);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Berry Drop Config");
        COMMON_BUILDER.push(CATEGORY_DROPS);

        DROPS_NATURAL = COMMON_BUILDER.comment("Number of berries a natural ore bush will drop, default is 4")
                .defineInRange("drops_natural", 4, 1, 10);

        DROPS_DUST = COMMON_BUILDER.comment("Number of berries a dust ore bush will drop, default is 4")
                .defineInRange("drops_dust", 4, 1, 10);

        DROPS_GEM = COMMON_BUILDER.comment("Number of berries a gem ore bush will drop, default is 1")
                .defineInRange("drops_gem", 4, 1, 10);

        DROPS_METAL = COMMON_BUILDER.comment("Number of berries a metal ore bush will drop, default is 2")
                .defineInRange("drops_metal", 4, 2, 10);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Integration Config");
        COMMON_BUILDER.push(CATEGORY_INTEGRATIONS);

        BOTANY_POTS = COMMON_BUILDER.comment("Whether or not to allow Ore Bushes to be grown in Botany Pots")
                .define("integration_botany_pots", true);

        CREATE = COMMON_BUILDER.comment("Whether or not to include Create Machine Recipes for Ore Bush Berries")
                .define("integration_create", true);

        MEKANISM = COMMON_BUILDER.comment("Whether or not to include Mekanism Machine Recipes for Ore Bush Berries")
                .define("integration_mekanism", true);

        THERMAL = COMMON_BUILDER.comment("Whether or not to include Thermal Machine Recipes for Ore Bush Seeds & Berries")
                .define("integration_thermal", true);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("General Config");
        COMMON_BUILDER.push(CATEGORY_GENERAL);

        GRINDING = COMMON_BUILDER.comment("Whether or not to enable berry to grounds recipes")
                .define("grinding", true);

        DROP_FROM_GRASS = COMMON_BUILDER.comment("Whether or not to drop essence berry seeds when breaking grass")
                .define("drop_from_grass", true);

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
