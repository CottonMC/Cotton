package io.github.cottonmc.cotton.behavior;

import io.github.cottonmc.cotton.util.FluidProperty;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CauldronUtils {
	private static final FluidProperty FLUID = FluidProperty.ANY_FLUID;
	private static final IntegerProperty LEVEL = CauldronBlock.LEVEL;

	public static boolean canPlaceFluid(BlockState state, FluidProperty.Wrapper fluid) {
		Fluid blockFluid = state.get(FLUID).getFluid();
		Fluid bucketFluid = fluid.getFluid();

		return blockFluid.matchesType(bucketFluid) || blockFluid == Fluids.EMPTY;
	}

	public static void placeFluid(World world, BlockPos pos, BlockState state, int level, FluidProperty.Wrapper fluid) {
		world.setBlockState(pos, state.with(LEVEL, MathHelper.clamp(level, 0, 3)).with(FLUID, fluid), 2);
		world.updateHorizontalAdjacent(pos, state.getBlock());
	}

	public static void setFluidFromLevel(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.with(FLUID, state.get(LEVEL) == 0 ? FluidProperty.EMPTY : state.get(FLUID)), 3);
	}

	public static Fluid tryEmptyFluid(IWorld world, BlockPos pos, BlockState state) {
		int level = state.get(LEVEL);
		Fluid fluid = state.get(FLUID).getFluid();

		if (level == 3) {
			world.setBlockState(pos, state.with(LEVEL, 0), 3);
			setFluidFromLevel(world, pos);
			return fluid;
		}

		return Fluids.EMPTY;
	}

	public static boolean tryDrainFluid(IWorld world, BlockPos pos, BlockState state) {
		int level = state.get(LEVEL);

		if (level > 0) {
			world.setBlockState(pos, state.with(LEVEL, level - 1), 3);
			setFluidFromLevel(world, pos);
			return true;
		}
		return false;
	}
}
