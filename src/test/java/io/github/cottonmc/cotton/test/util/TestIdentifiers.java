package io.github.cottonmc.cotton.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.cottonmc.cotton.util.Identifiers;
import net.minecraft.util.Identifier;

public class TestIdentifiers {

	@Test
	void withNameSpace() {
		Identifier base = new Identifier("foo:baz/qux");
		assertEquals(new Identifier("bar:baz/qux"), Identifiers.withNamespace(base, "bar"));
	}

	@Test
	void withPath() {
		Identifier base = new Identifier("foo:baz/qux");
		assertEquals(new Identifier("foo:bar"), Identifiers.withPath(base, "bar"));
		assertEquals(new Identifier("foo:bar/baz"), Identifiers.withPath(base, "bar/baz"),
			"Multilevel path test failed");
	}

	@Test
	void prefixPath() {
		Identifier base = new Identifier("foo:baz/qux");
		assertEquals(new Identifier("foo:bar/baz/qux"), Identifiers.prefixPath(base, "bar"));
		assertEquals(new Identifier("foo:bar/baz/baz/qux"), Identifiers.prefixPath(base, "bar/baz"),
			"Multilevel path test failed");
	}

	@Test
	void suffixPath() {
		Identifier base = new Identifier("foo:baz/qux");
		assertEquals(new Identifier("foo:baz/qux/bar"), Identifiers.suffixPath(base, "bar"));
		assertEquals(new Identifier("foo:baz/qux/bar/baz"), Identifiers.suffixPath(base, "bar/baz"),
			"Multilevel path test failed");
	}

	@Test
	void truncatePathStartOnly() {
		Identifier base = new Identifier("foo:bar/baz/qux");
		assertEquals(new Identifier("foo:baz/qux"), Identifiers.truncatePath(base, 1));

		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.truncatePath(base, -1),
			"Negative start index did not cause exception!");
		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.truncatePath(base, 3),
			"Start index > segment count did not cause exception!");
	}

	@Test
	void truncatePathStartEnd() {
		Identifier base = new Identifier("foo:bar/baz/qux");
		assertEquals(new Identifier("foo:baz"), Identifiers.truncatePath(base, 1, 2));

		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.truncatePath(base, -1, 2),
			"Negative start index did not cause exception!");
		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.truncatePath(base, 1, 4),
			"End index > segment count did not cause exception!");
		assertThrows(IllegalArgumentException.class, () -> Identifiers.truncatePath(base, 2, 1),
			"Start index > end index did not cause exception!");
	}

	@Test
	void replacePathSegments() {
		Identifier base = new Identifier("foo:bar/baz/qux");
		assertEquals(new Identifier("foo:bar/quuz"), Identifiers.replacePathSegments(base, 1, 3, "quuz"));

		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.replacePathSegments(base, -1, 2, "quuz"),
			"Negative start index did not cause exception!");
		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.replacePathSegments(base, 1, 4, "quuz"),
			"End index > segment count did not cause exception!");
		assertThrows(IllegalArgumentException.class, () -> Identifiers.replacePathSegments(base, 2, 1, "quuz"),
			"Start index > end index did not cause exception!");
	}

	@Test
	void remove() {
		Identifier base = new Identifier("foo:bar/baz/qux");
		assertEquals(new Identifier("foo:bar"), Identifiers.remove(base, 1, 3));

		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.remove(base, -1, 2),
			"Negative start index did not cause exception!");
		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.remove(base, 1, 4),
			"End index > segment count did not cause exception!");
		assertThrows(IllegalArgumentException.class, () -> Identifiers.remove(base, 2, 1),
			"Start index > end index did not cause exception!");
	}

	@Test
	void getPathSegment() {
		Identifier base = new Identifier("foo:bar/baz/qux");
		assertEquals("bar", Identifiers.transformer(base).getPathSegment(0));
		assertEquals("baz", Identifiers.transformer(base).getPathSegment(1));
		assertEquals("qux", Identifiers.transformer(base).getPathSegment(2));

		assertThrows(IndexOutOfBoundsException.class, () -> Identifiers.transformer(base).getPathSegment(-1));
	}

	@Test
	void segmentIndexOf() {
		Identifier base = new Identifier("foo:bar/baz/bar");
		assertEquals(0, Identifiers.transformer(base).segmentIndexOf("bar"));
		assertEquals(1, Identifiers.transformer(base).segmentIndexOf("baz"));
		assertEquals(-1, Identifiers.transformer(base).segmentIndexOf("qux"));
	}

	@Test
	void lastSegmentIndexOf() {
		Identifier base = new Identifier("foo:bar/baz/bar");
		assertEquals(2, Identifiers.transformer(base).lastSegmentIndexOf("bar"));
		assertEquals(1, Identifiers.transformer(base).lastSegmentIndexOf("baz"));
		assertEquals(-1, Identifiers.transformer(base).lastSegmentIndexOf("qux"));
	}
}
