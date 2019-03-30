package io.github.cottonmc.cotton.behavior;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface CauldronBehavior {

	Map<Predicate<CauldronContext>, CauldronBehavior> BEHAVIORS = new HashMap<>();

	static void registerBehavior(Predicate<CauldronContext> ctx, CauldronBehavior behavior) {
		BEHAVIORS.put(ctx, behavior);
	}

	/**
	 * Interact with a cauldron, given the specific context
	 * @param ctx the {@link CauldronContext} to interact using
	 */
	void interact(CauldronContext ctx);

}
