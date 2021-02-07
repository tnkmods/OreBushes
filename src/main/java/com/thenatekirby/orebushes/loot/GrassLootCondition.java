package com.thenatekirby.orebushes.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.config.OreBushesConfig;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;

public class GrassLootCondition implements ILootCondition {
    // TODO: Move To Babel
    @SuppressWarnings("SameParameterValue")
    private static LootConditionType register(ResourceLocation id, ILootSerializer<? extends ILootCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, id, new LootConditionType(serializer));
    }

    private static LootConditionType GRASS_LOOT = register(OreBushes.MOD.withPath("drop_from_grass"), Serializer.INSTANCE);

    @Nonnull
    @Override
    public LootConditionType func_230419_b_() {
        return GRASS_LOOT;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return OreBushesConfig.DROP_FROM_GRASS.get();
    }

    public static void register() {
    }

    public static class Serializer implements ILootSerializer<GrassLootCondition> {
        static final Serializer INSTANCE = new Serializer();

        @Override
        public void serialize(@Nonnull JsonObject jsonObject, @Nonnull GrassLootCondition condition, @Nonnull JsonSerializationContext context) {
        }

        @Nonnull
        @Override
        public GrassLootCondition deserialize(@Nonnull JsonObject jsonObject, @Nonnull JsonDeserializationContext context) {
            return new GrassLootCondition();
        }
    }
}
