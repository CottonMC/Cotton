package io.github.cottonmc.cotton.cauldron.mixins;

import io.github.cottonmc.cotton.cauldron.Cauldron;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.cotton.cauldron.tweaker.CauldronTweaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(CauldronBlock.class)
public class MixinCauldronBehavior implements Cauldron {

	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void onActivateHead(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult var6, CallbackInfoReturnable<ActionResult> cir) {
		// Run Cauldron Behaviors - this is the code to imitate when implementing on your own block
		CauldronContext ctx = new CauldronContext(world, pos, state, state.get(CauldronBlock.LEVEL), state.get(CauldronBlock.LEVEL) == 0 ? Fluids.EMPTY : Fluids.WATER, DefaultedList.copyOf(ItemStack.EMPTY), player, hand, player.getStackInHand(hand));
		Map<Predicate<CauldronContext>, CauldronBehavior> allBehaviors = new HashMap<>(CauldronBehavior.BEHAVIORS);
		// This adds support for Tweaker cauldron behaviors
		allBehaviors.putAll(CauldronTweaker.INSTANCE.behaviors);
		for (Predicate<CauldronContext> pred : allBehaviors.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = allBehaviors.get(pred);
				behavior.react(ctx);
				cir.setReturnValue(ActionResult.SUCCESS);
				return;
			}
		}
	}

	@Override
	public boolean fill(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles) {
		if (!FluidTags.WATER.contains(fluid)) return false;
		int newLevel = state.get(CauldronBlock.LEVEL) + bottles;
		if (newLevel > 3) return false;
		world.setBlockState(pos, state.with(CauldronBlock.LEVEL, newLevel));
		return true;
	}

	@Override
	public boolean drain(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles) {
		if (!FluidTags.WATER.contains(fluid)) return false;
		int newLevel = state.get(CauldronBlock.LEVEL) - bottles;
		if (newLevel < 0) return false;
		world.setBlockState(pos, state.with(CauldronBlock.LEVEL, newLevel));
		return true;
	}

	@Override
	public boolean canAcceptFluid(World world, BlockPos pos, BlockState state, Fluid fluid) {
		return FluidTags.WATER.contains(fluid);
	}

	@Override
	public CauldronContext createContext(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
		Hand hand;
		if (player == null) hand = null;
		else hand = player.getActiveHand();
		BlockState state = world.getBlockState(pos);
		return new CauldronContext(world, pos, state, state.get(CauldronBlock.LEVEL), state.get(CauldronBlock.LEVEL) == 0 ? Fluids.EMPTY : Fluids.WATER, DefaultedList.copyOf(ItemStack.EMPTY), player, hand, stack) ;
	}
}
