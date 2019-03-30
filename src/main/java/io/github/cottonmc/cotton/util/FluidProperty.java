package io.github.cottonmc.cotton.util;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.AbstractProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A block state property for fluids.
 */
public final class FluidProperty extends AbstractProperty<FluidProperty.Wrapper> {
	public static final Wrapper EMPTY = new Wrapper(() -> Fluids.EMPTY);
	public static final Wrapper WATER = new Wrapper(() -> Fluids.WATER);
	public static final Wrapper LAVA = new Wrapper(() -> Fluids.LAVA);
	private static final Set<Wrapper> VANILLA_FLUID_SET = Sets.newHashSet(FluidProperty.WATER, FluidProperty.LAVA, FluidProperty.EMPTY);
	public static final FluidProperty VANILLA_FLUIDS = new FluidProperty("fluid", () -> VANILLA_FLUID_SET);
	public static final FluidProperty ANY_FLUID = new FluidProperty("fluid");

	private final Supplier<Collection<Wrapper>> fluids;

	public FluidProperty(String var1, Supplier<Collection<Wrapper>> fluids) {
		super(var1, Wrapper.class);
		this.fluids = fluids;
	}

	public FluidProperty(String var1) {
		this(var1, () -> Registry.FLUID.stream().map(Wrapper::new).collect(Collectors.toList()));
	}

	@Override
	public Collection<Wrapper> getValues() {
		return fluids.get();
	}

	@Override
	public Optional<Wrapper> getValue(String str) {
		if (Strings.isNullOrEmpty(str))
			throw new IllegalArgumentException("input must not be empty or null!");

		try {
			Identifier id = propToId(str);
			if (Registry.FLUID.containsId(id)) {
				Wrapper fluid = new Wrapper(Registry.FLUID.get(id));
				if (getValues().contains(fluid))
					return Optional.of(fluid);
			}
		} catch (Exception e) {
		}

		return Optional.empty();
	}

	public static Identifier propToId(String str) {
		int underscoreIndex = str.lastIndexOf('_');
		if (underscoreIndex == -1) return new Identifier("minecraft", "empty");
		int namespaceLength = 0;
		try {
			namespaceLength = Integer.parseInt(str.substring(underscoreIndex + 1));
		} catch (NumberFormatException e) {
			return new Identifier("minecraft", "empty");
		}
		return new Identifier(str.substring(0, namespaceLength), str.substring(namespaceLength+1, underscoreIndex));
	}

	public static String idToProp(Identifier id) {
		return id.toString().replace(':', '_') + "_" + id.getNamespace().length();
	}

	@Override
	public String getValueAsString(Wrapper var1) {
		Identifier id = Registry.FLUID.getId(var1.get());
		return idToProp(id);
	}

	@Override
	public boolean equals(Object other) {
		return this == other;
	}

	public static final class Wrapper extends Lazy<Fluid> implements Comparable<Wrapper> {
		public Wrapper(Fluid fluid) {
			this(() -> fluid);
		}

		public Wrapper(Supplier<Fluid> fluid) {
			super(fluid);
		}

		public Fluid getFluid() {
			return get();
		}

		@Override
		public int compareTo(Wrapper o) {
			return Integer.compare(getId(get()), getId(o.get()));
		}

		private static int getId(Fluid fluid) {
			return fluid != null ? Registry.FLUID.getRawId(fluid) : -1;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(get());
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Wrapper && Objects.equals(((Wrapper) obj).get(), get());
		}

		@Override
		public String toString() {
			Identifier id = Registry.FLUID.getId(get());
			return idToProp(id);
		}
	}
}
