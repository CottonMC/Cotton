package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.behavior.CauldronBehavior;
import io.github.cottonmc.cotton.behavior.CauldronContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
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
public abstract class MixinCauldronBehavior  {

	@Inject(method = "activate", at = @At("HEAD"))
	public void useCauldronBehavior(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result, CallbackInfoReturnable cir) {
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
