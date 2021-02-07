package com.thenatekirby.orebushes.loot;

import com.google.gson.JsonObject;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.config.OreBushesConfig;
import com.thenatekirby.orebushes.registry.OreBush;
import com.thenatekirby.orebushes.registry.OreBushRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GrassLootModifier extends LootModifier {
    private static final ResourceLocation MODIFIER_ID = OreBushes.MOD.withPath("seed_drops");

    public GrassLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        OreBush oreBush = OreBushRegistry.INSTANCE.getOreBushById(OreBushes.MOD.withPath("essence"));
        if (oreBush != null) {
            oreBush.getSeedItem().ifPresent(seedItem -> generatedLoot.add(new ItemStack(seedItem)));
        }
        return generatedLoot;
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer extends GlobalLootModifierSerializer<GrassLootModifier> {
        public Serializer() {
            setRegistryName(MODIFIER_ID);
        }

        @Override
        public GrassLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new GrassLootModifier(conditions);
        }

        @Override
        public JsonObject write(GrassLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("type", MODIFIER_ID.toString());
            return json;
        }
    }

    // endregion
}
