package com.thenatekirby.orebushes.util.condition;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.config.OreBushesConfig;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class OreBushIntegrationEnabledCondition implements IRecipeCondition {
    private static final ResourceLocation CONDITION_ID = OreBushes.MOD.withPath("integration_enabled");

    @Nonnull
    private final String integration;

    // ====---------------------------------------------------------------------------====

    public OreBushIntegrationEnabledCondition(@Nonnull MutableResourceLocation integration) {
        this.integration = integration.getRoot();
    }

    private OreBushIntegrationEnabledCondition(@Nonnull String integration) {
        this.integration = integration;
    }

    @Nonnull
    @Override
    public JsonObject serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", CONDITION_ID.toString());
        jsonObject.addProperty("integration", integration);
        return jsonObject;
    }

    @Override
    public ResourceLocation getID() {
        return CONDITION_ID;
    }

    @Override
    public boolean test() {
        return OreBushesConfig.isIntegrationEnabled(integration);
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer implements IConditionSerializer<OreBushIntegrationEnabledCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, OreBushIntegrationEnabledCondition value) {
            json.addProperty("integration", value.integration);
        }

        @Override
        public OreBushIntegrationEnabledCondition read(JsonObject json) {
            String integration = JSONUtils.getAsString(json, "integration");
            return new OreBushIntegrationEnabledCondition(integration);
        }

        @Override
        public ResourceLocation getID() {
            return CONDITION_ID;
        }
    }

    // endregion
}
