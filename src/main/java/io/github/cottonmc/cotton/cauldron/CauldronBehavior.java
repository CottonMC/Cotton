package io.github.cottonmc.cotton.cauldron;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface CauldronBehavior {

	Map<Predicate<CauldronContext>, CauldronBehavior> BEHAVIORS = new HashMap<>();

	static void registerBehavior(Predicate<CauldronContext> ctx, CauldronBehavior behavior) {
		BEHAVIORS.put(ctx, behavior);
	}

	/**
	 * Perform a reaction, given the specific context
	 * @param ctx the {@link CauldronContext} to react using
	 */
	void react(CauldronContext ctx);

}
