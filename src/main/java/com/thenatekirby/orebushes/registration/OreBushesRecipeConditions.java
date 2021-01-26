package com.thenatekirby.orebushes.registration;

import com.thenatekirby.orebushes.util.condition.OreBushEnabledCondition;
import com.thenatekirby.orebushes.util.condition.OreBushIntegrationEnabledCondition;
import com.thenatekirby.orebushes.util.condition.OreBushGroundsEnabledCondition;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.IForgeRegistry;

// ====---------------------------------------------------------------------------====

public class OreBushesRecipeConditions {
    public static void onRegisterRecipeConditions() {
        CraftingHelper.register(OreBushEnabledCondition.Serializer.INSTANCE);
        CraftingHelper.register(OreBushGroundsEnabledCondition.Serializer.INSTANCE);
        CraftingHelper.register(OreBushIntegrationEnabledCondition.Serializer.INSTANCE);
    }
}
