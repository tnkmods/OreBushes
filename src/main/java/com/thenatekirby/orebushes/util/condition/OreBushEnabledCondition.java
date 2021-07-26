package com.thenatekirby.orebushes.util.condition;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.registry.OreBush;
import com.thenatekirby.orebushes.registry.OreBushRegistry;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class OreBushEnabledCondition implements IRecipeCondition {
    private static final ResourceLocation CONDITION_ID = OreBushes.MOD.withPath("bush_enabled");

    @Nonnull
    private final ResourceLocation oreBush;

    // ====---------------------------------------------------------------------------====

    public OreBushEnabledCondition(@Nonnull ResourceLocation oreBush) {
        this.oreBush = oreBush;
    }

    @Override
    public ResourceLocation getID() {
        return CONDITION_ID;
    }

    @Override
    public boolean test() {
        OreBush oreBushInfo = OreBushRegistry.INSTANCE.getOreBushById(oreBush);
        if (oreBushInfo == null) {
            return false;
        }

        return oreBushInfo.isEnabled();
    }

    @Nonnull
    @Override
    public JsonObject serializeJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", CONDITION_ID.toString());
        object.addProperty("bush", oreBush.toString());
        return object;
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer implements IConditionSerializer<OreBushEnabledCondition> {
        public static Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, OreBushEnabledCondition value) {
            json.addProperty("bush", value.oreBush.toString());
        }

        @Override
        public OreBushEnabledCondition read(JsonObject json) {
            String bush = JSONUtils.getAsString(json, "bush");
            return new OreBushEnabledCondition(new ResourceLocation(bush));
        }

        @Override
        public ResourceLocation getID() {
            return CONDITION_ID;
        }
    }

    // endregion
}
