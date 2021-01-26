package com.thenatekirby.orebushes.item;

import com.thenatekirby.orebushes.registration.OreBushItemGroup;
import net.minecraft.item.Item;

// ====---------------------------------------------------------------------------====

public class DustItem extends Item {
    private static final Item.Properties ITEM_PROPERTIES = new Item.Properties().group(OreBushItemGroup.getItemGroup());

    public DustItem() {
        super(ITEM_PROPERTIES);
    }
}
