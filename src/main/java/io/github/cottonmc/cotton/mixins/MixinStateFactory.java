package io.github.cottonmc.cotton.mixins;

import net.minecraft.state.StateFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.regex.Pattern;

/**
 * Used to allow colons in block property values.
 */
@Mixin(StateFactory.class)
public class MixinStateFactory {
	@Shadow @Final @Mutable
	private static Pattern NAME_MATCHER;

	static {
		NAME_MATCHER = Pattern.compile("^[a-z0-9_:]+$");
	}
}
