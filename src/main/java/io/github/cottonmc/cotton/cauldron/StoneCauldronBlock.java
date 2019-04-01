package io.github.cottonmc.cotton.cauldron;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.util.FluidProperty;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.FabricItemTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntegerProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class StoneCauldronBlock extends CauldronBlock implements FluidFillable, FluidDrainable {

	public static final IntegerProperty LEVEL = CauldronBlock.LEVEL;
	private static final FluidProperty FLUID = FluidProperty.ANY_FLUID;

	public StoneCauldronBlock() {
		super(FabricBlockSettings.of(Material.STONE).breakByTool(FabricItemTags.PICKAXES).build());
		this.setDefaultState(this.getStateFactory().getDefaultState().with(LEVEL, 0).with(FLUID, FluidProperty.EMPTY));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState state = ctx.getWorld().getFluidState(ctx.getBlockPos());

		if (state.getFluid() instanceof BaseFluid) {
			BaseFluid baseFluid = (BaseFluid) state.getFluid();
			Fluid still = baseFluid.getStill();
			BlockState superState = super.getPlacementState(ctx);

			if (superState != null) {
				return superState.with(LEVEL, 3).with(FLUID, new FluidProperty.Wrapper(still));
			}
		}

		return super.getPlacementState(ctx);
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.with(LEVEL, FLUID);
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() == Items.BUCKET) return false;

		CauldronContext ctx = new CauldronContext(world, pos, state, player, hand, player.getStackInHand(hand));
		for (Predicate<CauldronContext> pred : CauldronBehavior.BEHAVIORS.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = CauldronBehavior.BEHAVIORS.get(pred);
				behavior.interact(ctx);
				return true;
			}
		}

		if (stack.getItem() instanceof BucketItem) return false;

		// Item destruction with lava
		// Must not be a registered cauldron behavior, otherwise it'd override anything anyone else would wanna do with lava
		int lavaLevel = state.get(FLUID).getFluid().matches(FluidTags.LAVA) ? state.get(LEVEL) : 0;

		if (lavaLevel == 0) {
			boolean ret = super.activate(state, world, pos, player, hand, result);
			CauldronUtils.setFluidFromLevel(world, pos);
			return ret;
		}

		if (Cotton.config.cauldronTrashCan) {
			stack.subtractAmount(1);
			world.playSound(player, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCK, 1f, 1f);
			world.setBlockState(pos, state.with(LEVEL, lavaLevel - 1));
			CauldronUtils.setFluidFromLevel(world, pos);
		}
		return true;
	}

	@Override
	public Fluid tryDrainFluid(IWorld world, BlockPos pos, BlockState state) {
		int level = state.get(LEVEL);
		Fluid fluid = state.get(FLUID).getFluid();

		if (level == 3) {
			world.setBlockState(pos, state.with(LEVEL, 0), 3);
			CauldronUtils.setFluidFromLevel(world, pos);
			return fluid;
		}

		return Fluids.EMPTY;
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, BlockView view, BlockPos pos) {
		return createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	}

	@Override
	public boolean canFillWithFluid(BlockView view, BlockPos pos, BlockState state, Fluid fluid) {
		System.out.println(state.get(FLUID).getFluid().toString() + fluid.toString());
		//TODO: remove once we've got non-vanilla fluid compat
		if (!FluidProperty.VANILLA_FLUIDS.getValues().contains(new FluidProperty.Wrapper(fluid))) return false;
		return CauldronUtils.canPlaceFluid(state, new FluidProperty.Wrapper(fluid));
	}

	@Override
	public boolean tryFillWithFluid(IWorld world, BlockPos pos, BlockState state, FluidState fluidState) {
		setFluidLevel(world.getWorld(), pos, state, fluidState.getFluid(), 3);
		return true;
	}

	@Override
	public int getLuminance(BlockState state) {
		// TODO: Remove hardcoding when we switch to the fluid renderer
		if (state.get(FLUID).getFluid().matches(FluidTags.LAVA)) {
			return 15;
		} else {
			return super.getLuminance(state);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (state.get(FLUID).getFluid() ==  Fluids.WATER) {
			int level = state.get(LEVEL);
			float float_1 = pos.getY() + (6.0F + (float)(3 * level)) / 16.0F;
			if (!world.isClient && entity.isOnFire() && level > 0 && entity.getBoundingBox().minY <= (double)float_1) {
				entity.extinguish();
				this.setLevel(world, pos, state, level - 1);
			}
		}
	}

	public void setLevel(World world, BlockPos pos, BlockState state, int level) {
		setFluidLevel(world, pos, state, state.get(FLUID).getFluid(), level);
	}

	public void setFluidLevel(World world, BlockPos pos, BlockState state, Fluid fluid, int level) {
		world.setBlockState(pos, state.with(LEVEL, MathHelper.clamp(level, 0, 3)).with(FLUID, new FluidProperty.Wrapper(fluid)), 2);
		world.updateHorizontalAdjacent(pos, this);
	}

	public void onRainTick(World world, BlockPos pos) {
		if (world.random.nextInt(20) == 1) {
			float temp = world.getBiome(pos).getTemperature(pos);
			if (temp >= 0.15F) {
				BlockState state = world.getBlockState(pos);
				if (state.get(LEVEL) < 3 && (state.get(FLUID) == FluidProperty.WATER || state.get(FLUID) == FluidProperty.EMPTY)) {
					world.setBlockState(pos, state.method_11572(LEVEL), 2);
				}

			}
		}
	}
}
