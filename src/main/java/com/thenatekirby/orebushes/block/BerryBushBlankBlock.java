package com.thenatekirby.orebushes.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.NonNullList;

// ====---------------------------------------------------------------------------====
import net.minecraft.block.AbstractBlock;

/*
A placeholder block for registration purposes. These should never actually exist in the game
 */
public class BerryBushBlankBlock extends Block {
    private static final AbstractBlock.Properties BLOCK_PROPERTIES = AbstractBlock.Properties.copy(Blocks.WHEAT);
    private static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public BerryBushBlankBlock() {
        super(BLOCK_PROPERTIES);
        registerDefaultState(getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
    }
}
