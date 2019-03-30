package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.util.FluidProperty;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Includes small tweaks to make filled cauldrons nicer.
 */
@Mixin(Entity.class)
public abstract class MixinEntity {
	@Shadow
	public World world;

	@Shadow
	public abstract BlockPos getBlockPos();

	@Inject(method = "isTouchingLava", at = @At("RETURN"), cancellable = true)
	private void isTouchingLava(CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValue()) {
			BlockState state = world.getBlockState(getBlockPos());
			boolean insideLavaCauldron = state.getBlock() instanceof CauldronBlock &&
					state.get(FluidProperty.VANILLA_FLUIDS).getFluid() == Fluids.LAVA;
			boolean insideLavaFluidState = state.getFluidState().getFluid() == Fluids.LAVA;
			info.setReturnValue(insideLavaCauldron || insideLavaFluidState);
		}
	}

	@Redirect(method = "isInsideFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"))
	private FluidState getFluidStateProxy(World world, BlockPos pos) {
		// Can't override Block.getFluidState for CauldronBlock to do this;
		// it would result in a waterlogged-like look
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof CauldronBlock) {
			Fluid fluid = state.get(FluidProperty.VANILLA_FLUIDS).getFluid();

			if (fluid instanceof BaseFluid)
				return ((BaseFluid) fluid).getStill().getDefaultState();
		}

		return world.getFluidState(pos);
	}
}
