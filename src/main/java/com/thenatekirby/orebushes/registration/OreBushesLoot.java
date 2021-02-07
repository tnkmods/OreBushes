package com.thenatekirby.orebushes.registration;

import com.thenatekirby.orebushes.loot.GrassLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;

// ====---------------------------------------------------------------------------====

public class OreBushesLoot {
    public static void onRegisterGlobalLoot(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new GrassLootModifier.Serializer());
    }

}
