package com.thenatekirby.orebushes.registration;

import com.thenatekirby.babel.registration.DeferredItem;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.item.DustItem;
import com.thenatekirby.orebushes.item.FertilizerItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// ====---------------------------------------------------------------------------====

public class OreBushesItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OreBushes.MOD_ID);

    public static final DeferredItem<DustItem> TIER_ONE_DUST = DeferredItem.create("tier_one_dust", DustItem::new, ITEMS);
    public static final DeferredItem<DustItem> TIER_TWO_DUST = DeferredItem.create("tier_two_dust", DustItem::new, ITEMS);
    public static final DeferredItem<DustItem> TIER_THREE_DUST = DeferredItem.create("tier_three_dust", DustItem::new, ITEMS);

    public static final DeferredItem<FertilizerItem> ENRICHED_BONE_MEAL = DeferredItem.create("enriched_bone_meal", FertilizerItem::new, ITEMS);

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
