package com.thenatekirby.orebushes.data.bushes;

import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.core.RecipeIngredient;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.registry.material.MaterialRarity;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import com.thenatekirby.orebushes.registry.material.MaterialType;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class OreBushData {
    private final ResourceLocation materialId;
    private final ResourceLocation berryId;
    private final ResourceLocation seedId;
    private final ResourceLocation groundsId;
    private final MaterialType type;
    private final MaterialTier tier;
    private final MaterialRarity rarity;
    private final String color;
    private final RecipeIngredient smeltingResult;
    private final List<String> requiredMods;
    private final String name;
    private final boolean grindable;
    private final RecipeIngredient seedInput;

    private OreBushData(ResourceLocation materialId,
                        String name,
                        boolean grindable,
                        MaterialType type,
                        MaterialTier tier,
                        MaterialRarity rarity,
                        String color,
                        RecipeIngredient seedInput,
                        RecipeIngredient smeltingResult,
                        List<String> requiredMods) {
        this.materialId = materialId;
        this.name = name;
        this.grindable = grindable;
        this.berryId = makeBerry(materialId.getPath());
        this.seedId = makeSeed(materialId.getPath());
        this.groundsId = makeGrounds(materialId.getPath());
        this.type = type;
        this.tier = tier;
        this.rarity = rarity;
        this.color = color;
        this.seedInput = seedInput;
        this.smeltingResult = smeltingResult;
        this.requiredMods = requiredMods;
    }

    public ResourceLocation getMaterialId() {
        return materialId;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getBerryId() {
        return berryId;
    }

    public ResourceLocation getSeedId() {
        return seedId;
    }

    public ResourceLocation getGroundsId() {
        return groundsId;
    }

    public MaterialType getType() {
        return type;
    }

    public MaterialTier getTier() {
        return tier;
    }

    public MaterialRarity getRarity() {
        return rarity;
    }

    public String getColor() {
        return color;
    }

    public RecipeIngredient getSmeltingResult() {
        return smeltingResult;
    }

    public List<String> getRequiredMods() {
        return requiredMods;
    }

    public RecipeIngredient getSeedInput() {
        return seedInput;
    }

    public boolean isGrindable() {
        return grindable;
    }

    private static ResourceLocation makeBerry(String name) {
        return ORE_BUSHES.withPath("berry_" + name);
    }

    private static ResourceLocation makeSeed(String name) {
        return ORE_BUSHES.withPath("seed_" + name);
    }

    private static ResourceLocation makeGrounds(String name) {
        return ORE_BUSHES.withPath("grounds_" + name);
    }

    public static class Builder {
        private final ResourceLocation materialId;

        private MaterialType type;

        private MaterialTier tier;

        private MaterialRarity rarity;

        private String color;

        private RecipeIngredient smeltingResult;

        private final List<String> requiredMods = new ArrayList<>();

        private String name;

        private boolean grindable = true;

        private RecipeIngredient seedIngredient;

        private Builder(@Nonnull ResourceLocation materialId) {
            this.materialId = materialId;
        }

        public static Builder builder(@Nonnull ResourceLocation materialId) {
            return new Builder(materialId);
        }

        Builder withType(@Nonnull MaterialType type) {
            this.type = type;
            return this;
        }

        Builder withTier(@Nonnull MaterialTier tier) {
            this.tier = tier;
            return this;
        }

        Builder withRarity(@Nonnull MaterialRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        Builder withColor(@Nonnull String color) {
            this.color = color;
            return this;
        }

        Builder withSmeltingResult(@Nonnull RecipeIngredient smeltingResult) {
            this.smeltingResult = smeltingResult;
            return this;
        }

        Builder withName(@Nonnull String name) {
            this.name = name;
            return this;
        }

        @SuppressWarnings("SameParameterValue")
        Builder isGrindable(boolean isGrindable) {
            this.grindable = isGrindable;
            return this;
        }

        Builder withSeedIngredient(@Nonnull RecipeIngredient seedIngredient) {
            this.seedIngredient = seedIngredient;
            return this;
        }

        Builder withIngredient(@Nonnull RecipeIngredient ingredient) {
            this.seedIngredient = ingredient;
            this.smeltingResult = ingredient;
            return this;
        }

        Builder withRequiredMod(@Nonnull String modId) {
            this.requiredMods.add(modId);
            return this;
        }

        Builder withRequiredMod(@Nonnull MutableResourceLocation resourceLocation) {
            return withRequiredMod(resourceLocation.getRoot());
        }

        OreBushData build() {
            return new OreBushData(materialId, name, grindable, type, tier, rarity, color, seedIngredient, smeltingResult, requiredMods);
        }

        void buildInto(@Nonnull List<OreBushData> list) {
            list.add(this.build());
        }
    }


    private static MutableResourceLocation ORE_BUSHES = OreBushes.MOD;
    private static MutableResourceLocation FORGE = new MutableResourceLocation("forge");

    public static List<OreBushData> all() {
        List<OreBushData> oreBushes = new ArrayList<>();

        OreBushData.Builder.builder(ORE_BUSHES.withPath("essence"))
                .withName("Essence")
                .isGrindable(false)
                .withType(MaterialType.NATURAL)
                .withTier(MaterialTier.TIER_ZERO)
                .withRarity(MaterialRarity.COMMON)
                .withColor("910091")
                .withSmeltingResult(RecipeIngredient.fromItem(ORE_BUSHES.withPath("tier_one_dust")))
                .buildInto(oreBushes);

        // Dusts
        OreBushData.Builder.builder(ORE_BUSHES.withPath("glowstone"))
                .withName("Glowstone")
                .withType(MaterialType.DUST)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("e8dc00")
                .withIngredient(RecipeIngredient.fromItem(Items.GLOWSTONE_DUST))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("redstone"))
                .withName("Redstone")
                .withType(MaterialType.DUST)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.COMMON)
                .withColor("FFC70000")
                .withIngredient(RecipeIngredient.fromItem(Items.REDSTONE))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("vinteum"))
                .withName("Vinteum")
                .withType(MaterialType.DUST)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("1c5dad")
                .withIngredient(RecipeIngredient.fromItem(Mods.MANA_AND_ARTIFICE.withPath("vinteum_dust")))
                .withRequiredMod(Mods.MANA_AND_ARTIFICE)
                .buildInto(oreBushes);

        // Gems
        OreBushData.Builder.builder(ORE_BUSHES.withPath("apatite"))
                .withName("Apatite")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.COMMON)
                .withColor("0066ff")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/apatite")))
                .withRequiredMod(Mods.THERMAL)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("aquamarine"))
                .withName("Aquamarine")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("29c9ff")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/aquamarine")))
                .withRequiredMod(Mods.ASTRAL_SORCERY)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("certus_quartz"))
                .withName("Certus Quartz")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("a8c7ed")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/certus_quartz")))
                .withRequiredMod(Mods.APPLIED_ENERGISTICS)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("cinnabar"))
                .withName("Cinnabar")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.RARE)
                .withColor("e30000")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/cinnabar")))
                .withRequiredMod(Mods.THERMAL)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("coal"))
                .withName("Coal")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.COMMON)
                .withColor("66222222")
                .withIngredient(RecipeIngredient.fromItem(Items.COAL))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("diamond"))
                .withName("Diamond")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_THREE)
                .withRarity(MaterialRarity.EPIC)
                .withColor("FF00F8F9")
                .withIngredient(RecipeIngredient.fromItem(Items.DIAMOND))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("emerald"))
                .withName("Emerald")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_THREE)
                .withRarity(MaterialRarity.EPIC)
                .withColor("FF218400")
                .withIngredient(RecipeIngredient.fromItem(Items.EMERALD))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("fluorite"))
                .withName("Fluorite")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("FFD8FEFC")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/fluorite")))
                .withRequiredMod(Mods.MEKANISM)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("lapis"))
                .withName("Lapis")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("2b00c4")
                .withIngredient(RecipeIngredient.fromItem(Items.LAPIS_LAZULI))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("mana_gem"))
                .withName("Mana Gem")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.RARE)
                .withColor("b808c4")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/mana")))
                .withRequiredMod(Mods.ARS_NOUVEAU)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("niter"))
                .withName("Niter")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.RARE)
                .withColor("feffeb")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/niter")))
                .withRequiredMod(Mods.THERMAL)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("quartz"))
                .withName("Quartz")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.COMMON)
                .withColor("ffffff")
                .withIngredient(RecipeIngredient.fromItem(Items.QUARTZ))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("sulfur"))
                .withName("Sulfur")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("ccc500")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("gems/sulfur")))
                .withRequiredMod(Mods.THERMAL)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("uraninite"))
                .withName("Uraninite")
                .withType(MaterialType.GEM)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("FF00E137")
                .withSeedIngredient(RecipeIngredient.fromItem(Mods.POWAH.withPath("uraninite")))
                .withSmeltingResult(RecipeIngredient.fromItem(Mods.POWAH.withPath("uraninite_raw_poor")))
                .withRequiredMod(Mods.POWAH)
                .buildInto(oreBushes);

        // Metals
        OreBushData.Builder.builder(ORE_BUSHES.withPath("aluminum"))
                .withName("Aluminum")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("ededed")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/aluminum")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/aluminum")))
                .withRequiredMod(Mods.IMMERSIVE_ENGINEERING)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("copper"))
                .withName("Copper")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.COMMON)
                .withColor("FFF67401")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/copper")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/copper")))
                .withRequiredMod(Mods.THERMAL)
                .withRequiredMod(Mods.MEKANISM)
                .withRequiredMod(Mods.CREATE)
                .withRequiredMod(Mods.IMMERSIVE_ENGINEERING)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("gold"))
                .withName("Gold")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.RARE)
                .withColor("FFFFD700")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/gold")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/gold")))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("iron"))
                .withName("Iron")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.COMMON)
                .withColor("AAFAFAFA")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/iron")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/iron")))
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("lead"))
                .withName("Lead")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("FF5100A6")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/lead")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/lead")))
                .withRequiredMod(Mods.THERMAL)
                .withRequiredMod(Mods.MEKANISM)
                .withRequiredMod(Mods.IMMERSIVE_ENGINEERING)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("nickel"))
                .withName("Nickel")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.RARE)
                .withColor("f2ff7a")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/nickel")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/nickel")))
                .withRequiredMod(Mods.THERMAL)
                .withRequiredMod(Mods.IMMERSIVE_ENGINEERING)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("osmium"))
                .withName("Osmium")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("FF83ECE5")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/osmium")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/osmium")))
                .withRequiredMod(Mods.MEKANISM)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("silver"))
                .withName("Silver")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.RARE)
                .withColor("d1e9ed")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/silver")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/silver")))
                .withRequiredMod(Mods.THERMAL)
                .withRequiredMod(Mods.IMMERSIVE_ENGINEERING)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("tin"))
                .withName("Tin")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_ONE)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("FFBCEDFF")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/tin")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/tin")))
                .withRequiredMod(Mods.THERMAL)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("uranium"))
                .withName("Uranium")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_THREE)
                .withRarity(MaterialRarity.RARE)
                .withColor("FF00E137")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/uranium")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/uranium")))
                .withRequiredMod(Mods.MEKANISM)
                .withRequiredMod(Mods.IMMERSIVE_ENGINEERING)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("zinc"))
                .withName("Zinc")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_TWO)
                .withRarity(MaterialRarity.UNCOMMON)
                .withColor("d5f2de")
                .withSeedIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/zinc")))
                .withSmeltingResult(RecipeIngredient.fromTag(FORGE.withPath("nuggets/zinc")))
                .withRequiredMod(Mods.CREATE)
                .buildInto(oreBushes);

        OreBushData.Builder.builder(ORE_BUSHES.withPath("yellorium"))
                .withName("Yellorium")
                .withType(MaterialType.METAL)
                .withTier(MaterialTier.TIER_THREE)
                .withRarity(MaterialRarity.EPIC)
                .withColor("eef200")
                .withIngredient(RecipeIngredient.fromTag(FORGE.withPath("ingots/yellorium")))
                .withRequiredMod(Mods.EXTREME_REACTORS)
                .buildInto(oreBushes);

        return oreBushes;
    }
}
