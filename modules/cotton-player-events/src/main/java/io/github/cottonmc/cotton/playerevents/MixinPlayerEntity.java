package io.github.cottonmc.cotton.playerevents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tickPlayer(CallbackInfo ci) {
		PlayerTickCallback.EVENT.invoker().tick((PlayerEntity)(Object)this);
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropShoulderEntities()V"))
	public void damagePlayer(DamageSource source, float amount, CallbackInfoReturnable cir) {
		ActionResult damageAttempt = PlayerDamageCallback.EVENT.invoker().attemptDamage((PlayerEntity)(Object)this, source, amount);
		if (source.bypassesArmor() && source.isUnblockable()) return;
		if (damageAttempt == ActionResult.FAIL) cir.setReturnValue(false);
	}
}
