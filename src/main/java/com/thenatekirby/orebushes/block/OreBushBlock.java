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
    private static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape BUSHLING_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape GROWING_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    private static final AbstractBlock.Properties BLOCK_PROPERTIES = AbstractBlock.Properties.copy(Blocks.WHEAT);

    private final OreBush oreBush;

    public OreBushBlock(OreBush oreBush) {
        super(BLOCK_PROPERTIES);

        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
        this.oreBush = oreBush;
    }

    // ====---------------------------------------------------------------------------====
    // region State

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Nonnull
    public BlockState updateShape(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.getValue(AGE) == 0) {
            return BUSHLING_SHAPE;
        } else {
            return state.getValue(AGE) < 3 ? GROWING_SHAPE : super.getShape(state, worldIn, pos, context);
        }
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Nonnull
    @Override
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(oreBush.getBerryItem().orElse(Items.AIR));
    }

    @Nonnull
    @Override
    public BlockState getPlant(IBlockReader world, @Nonnull BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);

        if (blockState.getBlock() != this) {
            return defaultBlockState();
        }

        return blockState;
    }

    protected boolean mayPlaceOn(BlockState state, IBlockReader world, BlockPos pos) {
        return (state.is(oreBush.getMaterial().getFarmlandTag()));
    }

    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return (world.getBlockState(pos.relative(Direction.DOWN)).is(oreBush.getMaterial().getFarmlandTag()));
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
    public IFormattableTextComponent getName() {
        return new StringTextComponent(oreBush.getName() + " Berry Bush");
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void randomTick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random random) {
        int age = state.getValue(AGE);
        if (age >= 3 || worldIn.getRawBrightness(pos.above(), 0) < 9) {
            return;
        }

        int chance = oreBush.getMaterial().getCropGrowthChance();
        int roll = random.nextInt(100);
        boolean grow = roll <= chance;

        if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, grow)) {
            worldIn.setBlock(pos, state.setValue(AGE, age + 1), 2);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(@Nonnull ServerWorld worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
    }

    @SuppressWarnings("unused")
    public boolean onEnrichedBonemeal(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        int nextAge = state.getValue(AGE) + 1;
        if (state.getValue(AGE) < 3) {
            world.setBlock(pos, state.setValue(AGE, nextAge), 2);
            return true;

        } else {
            spawnBerryDrops(world, pos);
            world.setBlock(pos, state.setValue(AGE, 1), 2);
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

            popResource(world, blockPos, itemStack);
        });
    }

    @Nonnull
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player.getItemInHand(handIn).getItem() == OreBushesItems.ENRICHED_BONE_MEAL.toItem()) {
            return ActionResultType.PASS;
        }

        int age = state.getValue(AGE);
        if (age == 3) {
            spawnBerryDrops(worldIn, pos);

            worldIn.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
            worldIn.setBlock(pos, state.setValue(AGE, 1), 2);

            return ActionResultType.sidedSuccess(worldIn.isClientSide);

        } else {
            return super.use(state, worldIn, pos, player, handIn, hit);
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

        if (state.getValue(AGE) == 3) {
            oreBush.getBerryItem().ifPresent(item -> itemStacks.add(new ItemStack(item, oreBush.getMaterial().getCropResultCount())));
        }

        return itemStacks;
    }

    // endregion
}
