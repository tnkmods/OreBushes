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

public class OreBushGroundsEnabledCondition implements IRecipeCondition {
    private static final ResourceLocation CONDITION_ID = OreBushes.MOD.withPath("grounds_enabled");

    @Nonnull
    private final ResourceLocation oreBush;

    // ====---------------------------------------------------------------------------====

    public OreBushGroundsEnabledCondition(@Nonnull ResourceLocation oreBush) {
        this.oreBush = oreBush;
    }

    @Override
    public ResourceLocation getID() {
        return CONDITION_ID;
    }

    @Override
    public boolean test() {
        if (!OreBushRegistry.INSTANCE.isGrindingAvailable()) {
            return false;
        }

        OreBush oreBushInfo = OreBushRegistry.INSTANCE.getOreBushById(oreBush);
        if (oreBushInfo == null) {
            return false;
        }

        return oreBushInfo.isEnabled() && oreBushInfo.getMaterial().getInfo().isGrindable();
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

    public static class Serializer implements IConditionSerializer<OreBushGroundsEnabledCondition> {
        public static Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, OreBushGroundsEnabledCondition value) {
            json.addProperty("bush", value.oreBush.toString());
        }

        @Override
        public OreBushGroundsEnabledCondition read(JsonObject json) {
            String bush = JSONUtils.getString(json, "bush");
            return new OreBushGroundsEnabledCondition(new ResourceLocation(bush));
        }

        @Override
        public ResourceLocation getID() {
            return CONDITION_ID;
        }
    }

    // endregion
}
