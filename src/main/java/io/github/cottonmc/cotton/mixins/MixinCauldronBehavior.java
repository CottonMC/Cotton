package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.cauldron.Cauldron;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(CauldronBlock.class)
public class MixinCauldronBehavior implements Cauldron {

	@Inject(at = @At("HEAD"), method = "activate", cancellable = true)
	private void onActivateHead(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult var6, CallbackInfoReturnable<Boolean> cir) {
		// Run Cauldron Behaviors - this is the code to imitate when implementing on your own block
		CauldronContext ctx = new CauldronContext(world, pos, state, state.get(CauldronBlock.LEVEL), state.get(CauldronBlock.LEVEL) == 0 ? Fluids.EMPTY : Fluids.WATER, DefaultedList.create(ItemStack.EMPTY), player, hand, player.getStackInHand(hand));
		for (Predicate<CauldronContext> pred : CauldronBehavior.BEHAVIORS.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = CauldronBehavior.BEHAVIORS.get(pred);
				behavior.react(ctx);
				cir.setReturnValue(true);
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
}
