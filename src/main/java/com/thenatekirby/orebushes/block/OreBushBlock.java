package com.thenatekirby.orebushes.block;

import com.thenatekirby.orebushes.registration.OreBushesItems;
import com.thenatekirby.orebushes.registry.OreBush;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("deprecation")
public class OreBushBlock extends BushBlock implements IGrowable, IPlantable {
    private static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;
    private static final VoxelShape BUSHLING_SHAPE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape GROWING_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    private static final Block.Properties BLOCK_PROPERTIES = Block.Properties.from(Blocks.WHEAT);

    private final OreBush oreBush;

    public OreBushBlock(OreBush oreBush) {
        super(BLOCK_PROPERTIES);

        this.setDefaultState(this.getStateContainer().getBaseState().with(AGE, 0));
        this.oreBush = oreBush;
    }

    // ====---------------------------------------------------------------------------====
    // region State

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(AGE) < 3;
    }

    @Nonnull
    public BlockState updatePostPlacement(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(AGE) == 0) {
            return BUSHLING_SHAPE;
        } else {
            return state.get(AGE) < 3 ? GROWING_SHAPE : super.getShape(state, worldIn, pos, context);
        }
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Nonnull
    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(oreBush.getBerryItem().orElse(Items.AIR));
    }

    @Nonnull
    @Override
    public BlockState getPlant(IBlockReader world, @Nonnull BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);

        if (blockState.getBlock() != this) {
            return getDefaultState();
        }

        return blockState;
    }

    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return (state.isIn(oreBush.getMaterial().getFarmlandTag()));
    }

    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return (world.getBlockState(pos.offset(Direction.DOWN)).isIn(oreBush.getMaterial().getFarmlandTag()));
    }

    @SuppressWarnings("unused")
    public static int getBlockColor(BlockState state, @Nullable IBlockReader world, @Nullable BlockPos pos, int tintIndex){
        if (tintIndex == 1) {
            Block block = state.getBlock();
            if (block instanceof OreBushBlock) {
                return ((OreBushBlock) block).oreBush.getMaterial().getInfo().getColor();
            }
        }

        return -1;
    }

    @Override
    @Nonnull
    public IFormattableTextComponent getTranslatedName() {
        return new StringTextComponent(oreBush.getName() + " Berry Bush");
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void randomTick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random random) {
        int age = state.get(AGE);
        if (age >= 3 || worldIn.getLightSubtracted(pos.up(), 0) < 9) {
            return;
        }

        int chance = oreBush.getMaterial().getCropGrowthChance();
        int roll = random.nextInt(100);
        boolean grow = roll <= chance;

        if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, grow)) {
            worldIn.setBlockState(pos, state.with(AGE, age + 1), 2);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    @Override
    public boolean canGrow(@Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return false;
    }

    @Override
    public void grow(@Nonnull ServerWorld worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
    }

    @SuppressWarnings("unused")
    public boolean onEnrichedBonemeal(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        int nextAge = state.get(AGE) + 1;
        if (state.get(AGE) < 3) {
            world.setBlockState(pos, state.with(AGE, nextAge), 2);
            return true;

        } else {
            spawnBerryDrops(world, pos);
            world.setBlockState(pos, state.with(AGE, 1), 2);
            return false;
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Interaction

    private void spawnBerryDrops(@Nonnull World world, @Nonnull BlockPos blockPos) {
        oreBush.getBerryItem().ifPresent(item -> {
            int count = oreBush.getMaterial().getCropResultCount();
            ItemStack itemStack = new ItemStack(item, count);

            spawnAsEntity(world, blockPos, itemStack);
        });
    }

    @Nonnull
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player.getHeldItem(handIn).getItem() == OreBushesItems.ENRICHED_BONE_MEAL.toItem()) {
            return ActionResultType.PASS;
        }

        int age = state.get(AGE);
        if (age == 3) {
            spawnBerryDrops(worldIn, pos);

            worldIn.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
            worldIn.setBlockState(pos, state.with(AGE, 1), 2);

            return ActionResultType.func_233537_a_(worldIn.isRemote);

        } else {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(oreBush.getBerryItem().orElse(Items.AIR));
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder builder) {
        List<ItemStack> itemStacks = new ArrayList<>();
        oreBush.getSeedItem().ifPresent(item -> itemStacks.add(new ItemStack(item)));

        if (state.get(AGE) == 3) {
            oreBush.getBerryItem().ifPresent(item -> itemStacks.add(new ItemStack(item, oreBush.getMaterial().getCropResultCount())));
        }

        return itemStacks;
    }

    // endregion
}
