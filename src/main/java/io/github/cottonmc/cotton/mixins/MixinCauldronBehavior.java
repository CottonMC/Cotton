package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.behavior.CauldronBehavior;
import io.github.cottonmc.cotton.behavior.CauldronContext;
import io.github.cottonmc.cotton.impl.BucketFluidAccessor;
import io.github.cottonmc.cotton.util.FluidProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.FluidDrainable;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
public abstract class MixinCauldronBehavior extends Block implements FluidDrainable {
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
		if (!canPlaceFluid(state, FluidProperty.WATER))
			info.cancel();
	}

	@ModifyArg(method = "setLevel", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private BlockState setWaterLevel(BlockState state) {
		return state.with(FLUID, FluidProperty.WATER);
	}

	@Inject(at = @At("HEAD"), method = "activate", cancellable = true)
	private void onActivateHead(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult var6, CallbackInfoReturnable<Boolean> info) {
		ItemStack stack = player.getStackInHand(hand);

		// Skip the vanilla bucket code
		if (stack.getItem() == Items.BUCKET) {
			info.setReturnValue(false);
			info.cancel();
			return;
		}

		// Run CauldronBehaviors
		CauldronContext ctx = new CauldronContext(world, pos, state, player, hand, player.getStackInHand(hand));
		for (Predicate<CauldronContext> pred : CauldronBehavior.BEHAVIORS.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = CauldronBehavior.BEHAVIORS.get(pred);
				behavior.interact(ctx);
				info.setReturnValue(true);
				return;
			}
		}

		// Item destruction with lava
		// Must be on HEAD, otherwise item cleaning etc is done and lava turns into water
		int lavaLevel = state.get(FLUID).getFluid().matches(FluidTags.LAVA) ? state.get(LEVEL) : 0;

		if (lavaLevel == 0 || stack.getItem() instanceof BucketItem)
			return;

		stack.subtractAmount(1);
		world.playSound(player, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCK, 1f, 1f);
		world.setBlockState(pos, state.with(LEVEL, lavaLevel - 1));
		setFluidFromLevel(world, pos);
		info.setReturnValue(true);
		info.cancel();
	}

	@Inject(at = @At("RETURN"), method = "activate", cancellable = true)
	private void onActivateReturn(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult var6, CallbackInfoReturnable<Boolean> info) {
		ItemStack stack = player.getStackInHand(hand);
		int var11 = state.get(LEVEL);
		Item item = stack.getItem();
		if (!info.getReturnValue() && item instanceof BucketItem && ((BucketFluidAccessor) item).cotton_getFluid() != Fluids.EMPTY &&
				canPlaceFluid(state, new FluidProperty.Wrapper(((BucketFluidAccessor) item).cotton_getFluid()))) {
			if (var11 < 3 && !world.isClient) {
				if (!player.abilities.creativeMode) {
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}

				player.increaseStat(Stats.FILL_CAULDRON);
				placeFluid(world, pos, state, 3, new FluidProperty.Wrapper(((BucketFluidAccessor) item).cotton_getFluid()));
				if (((BucketFluidAccessor) item).cotton_getFluid().matches(FluidTags.LAVA)) {
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCK, 1.0F, 1.0F);
				} else {
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCK, 1.0F, 1.0F);
				}
			}

			info.setReturnValue(true);
		}
	}

	@Unique
	private boolean canPlaceFluid(BlockState state, FluidProperty.Wrapper fluid) {
		Fluid blockFluid = state.get(FLUID).getFluid();
		Fluid bucketFluid = fluid.getFluid();

		return blockFluid.matchesType(bucketFluid) || blockFluid == Fluids.EMPTY;
	}

	@Unique
	private void placeFluid(World var1, BlockPos var2, BlockState var3, int var4, FluidProperty.Wrapper fluid) {
		var1.setBlockState(var2, var3.with(LEVEL, MathHelper.clamp(var4, 0, 3)).with(FLUID, fluid), 2);
		var1.updateHorizontalAdjacent(var2, (Block) (Object) this);
	}

	@Unique
	private void setFluidFromLevel(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.with(FLUID, state.get(LEVEL) == 0 ? FluidProperty.EMPTY : state.get(FLUID)), 3);
	}

	@Override
	public Fluid tryDrainFluid(IWorld world, BlockPos pos, BlockState state) {
		int level = state.get(LEVEL);
		Fluid fluid = state.get(FLUID).getFluid();

		if (level == 3) {
			world.setBlockState(pos, state.with(LEVEL, 0), 3);
			setFluidFromLevel(world, pos);
			return fluid;
		}

		return Fluids.EMPTY;
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
