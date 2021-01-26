package com.thenatekirby.orebushes.registration;

import com.thenatekirby.babel.registration.DeferredBlock;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.block.BerryBushBlankBlock;
import com.thenatekirby.orebushes.block.EnrichedFarmlandBlock;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// ====---------------------------------------------------------------------------====

public class OreBushesBlocks {
    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OreBushes.MOD_ID);
    private static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OreBushes.MOD_ID);

    public static DeferredBlock<BerryBushBlankBlock> BERRY_BUSH_BLANK = DeferredBlock.createHidden("berry_bush_blank", BerryBushBlankBlock::new, BLOCKS, ITEMS);
    public static DeferredBlock<EnrichedFarmlandBlock> TIER_ONE_FARMLAND = DeferredBlock.create("tier_one_farmland", () -> new EnrichedFarmlandBlock(MaterialTier.TIER_ONE), OreBushItemGroup.getDefaultBlockItemProperties(), BLOCKS, ITEMS);
    public static DeferredBlock<EnrichedFarmlandBlock> TIER_TWO_FARMLAND = DeferredBlock.create("tier_two_farmland", () -> new EnrichedFarmlandBlock(MaterialTier.TIER_TWO), OreBushItemGroup.getDefaultBlockItemProperties(), BLOCKS, ITEMS);
    public static DeferredBlock<EnrichedFarmlandBlock> TIER_THREE_FARMLAND = DeferredBlock.create("tier_three_farmland", () -> new EnrichedFarmlandBlock(MaterialTier.TIER_THREE), OreBushItemGroup.getDefaultBlockItemProperties(), BLOCKS, ITEMS);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
