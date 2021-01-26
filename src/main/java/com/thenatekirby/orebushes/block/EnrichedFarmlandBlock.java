package com.thenatekirby.orebushes.block;

import com.thenatekirby.orebushes.Localization;
import com.thenatekirby.orebushes.registry.material.MaterialTier;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

// ====---------------------------------------------------------------------------====

public class EnrichedFarmlandBlock extends FarmlandBlock {
    private MaterialTier tier;

    public EnrichedFarmlandBlock(MaterialTier tier) {
        super(Block.Properties.from(Blocks.FARMLAND));
        this.tier = tier;
        setDefaultState(getDefaultState().with(MOISTURE, 7));
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction facing, IPlantable plantable) {
        return plantable.getPlant(world, pos).getBlock() instanceof OreBushBlock;
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, Random rand) {
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
    }

    @Override
    public void onFallenUpon(World worldIn, @Nonnull BlockPos pos, Entity entityIn, float fallDistance) {
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (tier == MaterialTier.TIER_ONE) {
            tooltip.add(Localization.TIER_ONE_FARMLAND_DESC.mergeStyle(TextFormatting.GRAY));
        } else if (tier == MaterialTier.TIER_TWO) {
            tooltip.add(Localization.TIER_TWO_FARMLAND_DESC.mergeStyle(TextFormatting.GRAY));
        } else if (tier == MaterialTier.TIER_THREE) {
            tooltip.add(Localization.TIER_THREE_FARMLAND_DESC.mergeStyle(TextFormatting.GRAY));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
