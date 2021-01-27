package com.thenatekirby.orebushes.registration;

import com.thenatekirby.orebushes.OreBushes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

// ====---------------------------------------------------------------------------====

public class OreBushesItemGroup {
    // TODO: If someone disables iron berries, then we've got no icon...
    private static final ItemGroup itemGroup = new ItemGroup(OreBushes.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(OreBushes.MOD.withPath("berry_iron")));
        }
    };

    public static ItemGroup getItemGroup() {
        return itemGroup;
    }

    static Item.Properties getDefaultBlockItemProperties() {
        return new Item.Properties().group(itemGroup);
    }
}
