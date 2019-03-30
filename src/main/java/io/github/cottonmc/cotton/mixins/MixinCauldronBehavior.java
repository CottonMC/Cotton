package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.behavior.CauldronBehavior;
import io.github.cottonmc.cotton.behavior.CauldronContext;
import io.github.cottonmc.cotton.behavior.CauldronUtils;
import io.github.cottonmc.cotton.impl.BucketFluidAccessor;
import io.github.cottonmc.cotton.util.FluidProperty;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntegerProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

/* TODO Make this work for any fluid
 * This currently requires separate model definitions for each fluid
 * (that's why this is using only vanilla fluids). We should be able to use
 * the fluid renderer to render any fluid in the future. */
@Mixin(CauldronBlock.class)
public abstract class MixinCauldronBehavior extends Block implements FluidDrainable, FluidFillable {
	@Shadow
	@Final
	public static IntegerProperty LEVEL;
	private static final FluidProperty FLUID = FluidProperty.VANILLA_FLUIDS;

	MixinCauldronBehavior(Settings settings) {
		super(settings);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	private void onConstruct(Settings var1, CallbackInfo info) {
		setDefaultState(getDefaultState().with(FLUID, FluidProperty.EMPTY));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		FluidState state = context.getWorld().getFluidState(context.getBlockPos());

		if (state.getFluid() instanceof BaseFluid) {
			BaseFluid baseFluid = (BaseFluid) state.getFluid();
			Fluid still = baseFluid.getStill();
			BlockState superState = super.getPlacementState(context);

			if (superState != null) {
				return superState.with(LEVEL, 3).with(FLUID, new FluidProperty.Wrapper(still));
			}
		}

		return super.getPlacementState(context);
	}

	@Inject(at = @At("RETURN"), method = "appendProperties", cancellable = true)
	private void onAppendProperties(StateFactory.Builder<Block, BlockState> var1, CallbackInfo info) {
		var1.with(FLUID);
	}

	@Inject(method = "setLevel", at = @At("HEAD"), cancellable = true)
	private void setLevel(World world, BlockPos pos, BlockState state, int level, CallbackInfo info) {
		if (!CauldronUtils.canPlaceFluid(state, FluidProperty.WATER))
			info.cancel();
	}

	@ModifyArg(method = "setLevel", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private BlockState setWaterLevel(BlockState state) {
		return state.with(FLUID, FluidProperty.WATER);
	}

	@Inject(at = @At("HEAD"), method = "activate", cancellable = true)
	private void onActivateHead(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult var6, CallbackInfoReturnable<Boolean> cir) {
		ItemStack stack = player.getStackInHand(hand);

		// Skip the vanilla bucket code
		if (stack.getItem() == Items.BUCKET) {
			cir.setReturnValue(false);
			cir.cancel();
			return;
		}

		// Run CauldronBehaviors
		CauldronContext ctx = new CauldronContext(world, pos, state, player, hand, player.getStackInHand(hand));
		for (Predicate<CauldronContext> pred : CauldronBehavior.BEHAVIORS.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = CauldronBehavior.BEHAVIORS.get(pred);
				behavior.interact(ctx);
				cir.setReturnValue(true);
				return;
			}
		}

		// Item destruction with lava
		// Must be on HEAD, otherwise item cleaning etc is done and lava turns into water
		// Must also not be a registered cauldron behavior, otherwise it'd override anything anyone else would wanna do with lava
		int lavaLevel = state.get(FLUID).getFluid().matches(FluidTags.LAVA) ? state.get(LEVEL) : 0;

		if (lavaLevel == 0 || stack.getItem() instanceof BucketItem || !Cotton.config.cauldronTrashCan)
			return;

		stack.subtractAmount(1);
		world.playSound(player, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCK, 1f, 1f);
		world.setBlockState(pos, state.with(LEVEL, lavaLevel - 1));
		CauldronUtils.setFluidFromLevel(world, pos);
		cir.setReturnValue(true);
		cir.cancel();
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
	public boolean canFillWithFluid(BlockView view, BlockPos pos, BlockState state, Fluid fluid) {
		//TODO: remove once we've got non-vanilla fluid compat
		if (FluidProperty.VANILLA_FLUIDS.getValues().contains(new FluidProperty.Wrapper(fluid))) return false;
		return CauldronUtils.canPlaceFluid(state, new FluidProperty.Wrapper(fluid));
	}

	@Override
	public boolean tryFillWithFluid(IWorld world, BlockPos pos, BlockState state, FluidState fluidState) {
		CauldronUtils.placeFluid(world.getWorld(), pos, state, 3, new FluidProperty.Wrapper(fluidState.getFluid()));
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

	@Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
	private void onEntityCollision(BlockState state, World world_1, BlockPos blockPos_1, Entity entity_1, CallbackInfo info) {
		if (state.get(FLUID).getFluid() == Fluids.LAVA)
			info.cancel();
	}
}
