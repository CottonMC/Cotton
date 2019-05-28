package io.github.cottonmc.cotton.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profiler;

public interface PlayerTickCallback {
	public static final Event<PlayerTickCallback> EVENT = EventFactory.createArrayBacked(PlayerTickCallback.class,
			(listeners) -> {
				if (EventFactory.isProfilingEnabled()) {
					return (player) -> {
						Profiler profiler = player.getServer().getProfiler();
						profiler.push("fabricPlayerTick");
						for (PlayerTickCallback event : listeners) {
							profiler.push(EventFactory.getHandlerName(event));
							event.tick(player);
							profiler.pop();
						}
						profiler.pop();
					};
				} else {
					return (player) -> {
						for (PlayerTickCallback event : listeners) {
							event.tick(player);
						}
					};
				}
			}
	);

	void tick(PlayerEntity player);
}
