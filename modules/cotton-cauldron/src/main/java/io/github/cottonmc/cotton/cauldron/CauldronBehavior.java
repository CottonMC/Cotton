package io.github.cottonmc.cotton.cauldron;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface CauldronBehavior {

	Map<Predicate<CauldronContext>, CauldronBehavior> BEHAVIORS = new HashMap<>();

	/**
	 * Register a new cauldron behavior.
	 * Use lambdas for these. Each are passed a {@link CauldronContext}.
	 * @param ctx A predicate for under what conditions the behavior should be performed.
	 * @param behavior The behavior to perform when the conditions are met.
	 */
	static void registerBehavior(Predicate<CauldronContext> ctx, CauldronBehavior behavior) {
		BEHAVIORS.put(ctx, behavior);
	}

	/**
	 * Perform a reaction, given the specific context
	 * @param ctx the {@link CauldronContext} to react using
	 */
	void react(CauldronContext ctx);

}
