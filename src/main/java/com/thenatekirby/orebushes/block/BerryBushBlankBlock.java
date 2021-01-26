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
/*
A placeholder block for registration purposes. These should never actually exist in the game
 */
public class BerryBushBlankBlock extends Block {
    private static final Block.Properties BLOCK_PROPERTIES = Block.Properties.from(Blocks.WHEAT);
    private static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;

    public BerryBushBlankBlock() {
        super(BLOCK_PROPERTIES);
        setDefaultState(getStateContainer().getBaseState().with(AGE, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    }
}
