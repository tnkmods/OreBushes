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
    private static final Item.Properties ITEM_PROPERTIES = new Item.Properties().group(OreBushesItemGroup.getItemGroup());

    public FertilizerItem() {
        super(ITEM_PROPERTIES);
    }

    // ====---------------------------------------------------------------------------====
    // region Interaction

    @Override
    @Nonnull
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getPos();
        ItemStack itemStack = context.getItem();

        if (FertilizerItem.applyBonemeal(itemStack, world, blockPos, false)) {
            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(context);
    }

    private static boolean applyBonemeal(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull BlockPos blockPos, boolean automated) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof OreBushBlock) {
            OreBushBlock oreBushBlock = (OreBushBlock) blockState.getBlock();
            if (!world.isRemote) {
                if (oreBushBlock.onEnrichedBonemeal((ServerWorld) world, world.rand, blockPos, blockState, automated)) {
                    world.playEvent(2005, blockPos, 0);
                    itemStack.shrink(1);
                    return true;
                }
            }
        }

        return false;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Dispenser

    public static void registerDispenserBehavior() {
        DispenserBlock.registerDispenseBehavior(OreBushesItems.ENRICHED_BONE_MEAL, new OptionalDispenseBehavior() {
            @Nonnull
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                setSuccessful(true);
                World world = source.getWorld();
                BlockPos blockPos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));

                if (!FertilizerItem.applyBonemeal(stack, world, blockPos, true)) {
                    setSuccessful(false);
                } else if (!world.isRemote) {
                    world.playEvent(2005, blockPos, 0);
                }

                return stack;
            }
        });
    }

    // endregion
}
