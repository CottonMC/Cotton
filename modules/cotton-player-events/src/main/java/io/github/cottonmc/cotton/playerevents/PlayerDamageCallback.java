package io.github.cottonmc.cotton.playerevents;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.profiler.Profiler;

public interface PlayerDamageCallback {
	//TODO: priority system instead, voting is bad
	public static final Event<PlayerDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerDamageCallback.class,
			(listeners) -> {
//				if (EventFactory.isProfilingEnabled()) {
//					return (player, source, amount) -> {
//						int cancelVotes = 0;
//						if (player.getServer() != null) {
//							Profiler profiler = player.getServer().getProfiler();
//							profiler.push("cottonPlayerDamage");
//							for (PlayerDamageCallback event : listeners) {
//								profiler.push(EventFactory.getHandlerName(event));
//								ActionResult result = event.attemptDamage(player, source, amount);
//								if (result == ActionResult.SUCCESS) {
//									cancelVotes--;
//								} else if (result == ActionResult.FAIL) cancelVotes++;
//								profiler.pop();
//							}
//							profiler.pop();
//						}
//						else {
//							for (PlayerDamageCallback event : listeners) {
//								ActionResult result = event.attemptDamage(player, source, amount);
//								if (result == ActionResult.SUCCESS) {
//									cancelVotes--;
//								} else if (result == ActionResult.FAIL) cancelVotes++;
//							}
//						}
//						return cancelVotes > 0? ActionResult.FAIL : ActionResult.SUCCESS;
//					};
//				} else {
					return (player, source, amount) -> {
						int cancelVotes = 0;
						for (PlayerDamageCallback event : listeners) {
							ActionResult result = event.attemptDamage(player, source, amount);
							if (result == ActionResult.SUCCESS) {
								cancelVotes--;
							} else if (result == ActionResult.FAIL) cancelVotes++;
						}
						return cancelVotes > 0? ActionResult.FAIL : ActionResult.SUCCESS;
					};
//				}
			}
	);

	/**
	 * Perform anything needed when a player is damaged, and vote whether to keep or cancel the damage attempt.
	 * A cancellation needs >50% of votes to cancel.
	 * @param player The player being damaged.
	 * @param source The source of attemptDamage.
	 * @param amount How much damage is being done.
	 * @return SUCCESS to keep the attemptDamage, FAIL to cancel the attemptDamage, PASS to abstain from voting (will not affect vote in either direction)
	 */
	ActionResult attemptDamage(PlayerEntity player, DamageSource source, float amount);
}
