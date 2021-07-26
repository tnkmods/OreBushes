package com.thenatekirby.orebushes.item;

import com.thenatekirby.orebushes.block.OreBushBlock;
import com.thenatekirby.orebushes.registration.OreBushesItemGroup;
import com.thenatekirby.orebushes.registration.OreBushesItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class FertilizerItem extends Item {
    private static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(OreBushesItemGroup.getItemGroup());

    public FertilizerItem() {
        super(ITEM_PROPERTIES);
    }

    // ====---------------------------------------------------------------------------====
    // region Interaction

    @Override
    @Nonnull
    public ActionResultType useOn(@Nonnull ItemUseContext context) {
        World world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        ItemStack itemStack = context.getItemInHand();

        if (FertilizerItem.applyBonemeal(itemStack, world, blockPos)) {
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    private static boolean applyBonemeal(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof OreBushBlock) {
            OreBushBlock oreBushBlock = (OreBushBlock) blockState.getBlock();
            if (!world.isClientSide) {
                if (oreBushBlock.onEnrichedBonemeal((ServerWorld) world, world.random, blockPos, blockState)) {
                    world.levelEvent(2005, blockPos, 0);
                    itemStack.shrink(1);
                }
            }

            return true;
        }

        return false;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Dispenser

    public static void registerDispenserBehavior() {
        DispenserBlock.registerBehavior(OreBushesItems.ENRICHED_BONE_MEAL, new OptionalDispenseBehavior() {
            @Nonnull
            @Override
            protected ItemStack execute(IBlockSource source, ItemStack stack) {
                setSuccess(true);
                World world = source.getLevel();
                BlockPos blockPos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

                if (!FertilizerItem.applyBonemeal(stack, world, blockPos)) {
                    setSuccess(false);
                } else if (!world.isClientSide) {
                    world.levelEvent(2005, blockPos, 0);
                }

                return stack;
            }
        });
    }

    // endregion
}
