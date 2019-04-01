package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.cotton.cauldron.CauldronUtils;
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
public abstract class MixinCauldronBehavior extends Block {

	MixinCauldronBehavior(Settings settings) {
		super(settings);
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
	}
}
