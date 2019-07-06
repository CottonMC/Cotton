package io.github.cottonmc.cotton.datapack.util;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

/**
 * @author Daomephsta Static utility methods related to {@link Identifier}.
 */
public class Identifiers {

	private static final String SEGMENT_SEPARATOR = "/";
	private static final char SEGMENT_SEPARATOR_CHAR = '/';
	private static final Splitter PATH_SPLITTER = Splitter.on(SEGMENT_SEPARATOR_CHAR);
	private static final Pattern VALID_NAMESPACE_CHARS = Pattern.compile("[a-z0-9_.-]+");
	private static final Pattern VALID_PATH_CHARS = Pattern.compile("[a-z0-9/._-]+");

	private Identifiers() {}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#namespace(String)}. If chaining multiple
	 * operations, an {@link IdentifierTransformer} should be instantiated via
	 * {@link #transformer(Identifier)}, and the methods on it used instead.
	 * 
	 * @param base
	 *            the Identifier to copy the path from.
	 * @param namespace
	 *            the new namespace.
	 * @return a copy of {@code base}, with a namespace of {@code namespace}.
	 */
	public static Identifier withNamespace(Identifier base, String namespace) {
		return transformer(base).namespace(namespace).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#path(String)}. If chaining multiple
	 * operations, an {@link IdentifierTransformer} should be instantiated via
	 * {@link #transformer(Identifier)}, and the methods on it used instead.
	 * 
	 * @param base
	 *            the Identifier to copy the namespace from.
	 * @param path
	 *            the new path.
	 * @return a copy of {@code base}, with a path of {@code path}.
	 */
	public static Identifier withPath(Identifier base, String path) {
		return transformer(base).path(path).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#prefixPath(String)}. If chaining multiple
	 * operations, an {@link IdentifierTransformer} should be instantiated via
	 * {@link #transformer(Identifier)}, and the methods on it used instead.
	 * 
	 * @param base
	 *            an Identifier to base the returned Identifier on.
	 * @param prefix
	 *            the {@code String} to prepend to the path.
	 * @return a copy of {@code base}, with {@code prefix} prepended to the
	 *         path.
	 */
	public static Identifier prefixPath(Identifier base, String prefix) {
		return transformer(base).prefixPath(prefix).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#suffixPath(String)}. If chaining multiple
	 * operations, an {@link IdentifierTransformer} should be instantiated via
	 * {@link #transformer(Identifier)}, and the methods on it used instead.
	 * 
	 * @param base
	 *            an Identifier to base the returned Identifier on.
	 * @param suffix
	 *            the {@code String} to append to the path.
	 * @return a copy of {@code base}, with {@code suffix} appended to the path.
	 */
	public static Identifier suffixPath(Identifier base, String suffix) {
		return transformer(base).suffixPath(suffix).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#truncatePath(int)}. If chaining multiple
	 * operations, an {@link IdentifierTransformer} should be instantiated via
	 * {@link #transformer(Identifier)}, and the methods on it used instead.
	 * 
	 * @param base
	 *            an Identifier to base the returned Identifier on.
	 * @param start
	 *            the inclusive index of the first kept path segment.
	 * @return a copy of {@code base}, with all path segments with an index less
	 *         than {@code index} removed.
	 */
	public static Identifier truncatePath(Identifier base, int start) {
		return transformer(base).truncatePath(start).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#truncatePath(int, int)}. If chaining
	 * multiple operations, an {@link IdentifierTransformer} should be
	 * instantiated via {@link #transformer(Identifier)}, and the methods on it
	 * used instead.
	 * 
	 * @param base
	 *            an Identifier to base the returned Identifier on.
	 * @param start
	 *            the inclusive index of the first kept path segment.
	 * @param end
	 *            the exclusive index of the last kept path segment.
	 * @return a copy of {@code base}, with all path segments with the path
	 *         segments <b>not</b> between {@code start} (inclusive) and
	 *         {@code end} (exclusive) removed.
	 */
	public static Identifier truncatePath(Identifier base, int start, int end) {
		return transformer(base).truncatePath(start, end).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#replacePathSegments(int, int, String)}. If
	 * chaining multiple operations, an {@link IdentifierTransformer} should be
	 * instantiated via {@link #transformer(Identifier)}, and the methods on it
	 * used instead.
	 * 
	 * @param base
	 *            an Identifier to base the returned Identifier on.
	 * @param start
	 *            the inclusive start index of the replacement operation.
	 * @param end
	 *            the exclusive end index of the replacement operation.
	 * @param replacement
	 *            a sequence of path segments to replace the sequence of path
	 *            segments between {@code start} (inclusive) and {@code end}
	 *            (exclusive) with.
	 * @return a copy of {@code base}, with the path segments between
	 *         {@code start} (inclusive) and {@code end} (exclusive) replaced by
	 *         the path segments of {@code replacement}.
	 */
	public static Identifier replacePathSegments(Identifier base, int start, int end, String replacement) {
		return transformer(base).replacePathSegments(start, end, replacement).apply();
	}

	/**
	 * Static convenience method version of
	 * {@link IdentifierTransformer#remove(int, int)}. If chaining multiple
	 * operations, an {@link IdentifierTransformer} should be instantiated via
	 * {@link #transformer(Identifier)}, and the methods on it used instead.
	 * 
	 * @param base
	 *            an Identifier to base the returned Identifier on.
	 * @param start
	 *            the inclusive start index of the removal operation.
	 * @param end
	 *            the exclusive end index of the removal operation.
	 * @return a copy of {@code base}, with all path segments with the path
	 *         segments between {@code start} (inclusive) and {@code end}
	 *         (exclusive) removed.
	 */
	public static Identifier remove(Identifier base, int start, int end) {
		return transformer(base).remove(start, end).apply();
	}

	/**
	 * @param base
	 *            the Identifier to transform.
	 * @return a new instance of IdentifierTransformer.
	 */
	public static IdentifierTransformer transformer(Identifier base) {
		return new IdentifierTransformer(base);
	}

	/**
	 * @author Daomephsta Provides a semi-fluent API for transforming a base
	 *         {@link Identifier} efficiently.
	 */
	public static class IdentifierTransformer {

		private String namespace;
		private List<String> pathSegments;

		private IdentifierTransformer(Identifier base) {
			this.namespace(base.getNamespace()).path(base.getPath());
		}

		/**
		 * Returns the path segment at the specified index.
		 * 
		 * @param index
		 *            index of the path segment to return.
		 * @return the path segment at the specified index.
		 */
		public String getPathSegment(int index) {
			return pathSegments.get(index);
		}

		/**
		 * Returns the index of the first path segment exactly matching
		 * {@code test}
		 * 
		 * @param test
		 *            the String to match against
		 * @return the index of the first path segment exactly matching
		 *         {@code test}, or -1 if no such path segment exists.
		 */
		public int segmentIndexOf(String test) {
			return pathSegments.indexOf(test);
		}

		/**
		 * Returns the index of the last path segment exactly matching
		 * {@code test}
		 * 
		 * @param test
		 *            the String to match against
		 * @return the index of the last path segment exactly matching
		 *         {@code test}, or -1 if no such path segment exists.
		 */
		public int lastSegmentIndexOf(String test) {
			return pathSegments.lastIndexOf(test);
		}

		/**
		 * Sets the namespace.
		 * 
		 * @param namespace
		 *            the new namespace
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer namespace(String namespace) {
			validateNamespaceChars(namespace, "namespace");
			this.namespace = namespace;
			return this;
		}

		/**
		 * Sets the path.
		 * 
		 * @param path
		 *            the new path
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer path(String path) {
			validatePathChars(path, "path");
			this.pathSegments = splitPathMutable(path);
			return this;
		}

		/**
		 * Adds a prefix to the path.
		 * 
		 * @param prefix
		 *            the prefix to prepend
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer prefixPath(String prefix) {
			validatePathChars(prefix, "prefix");
			this.pathSegments.addAll(0, splitPath(prefix));
			return this;
		}

		/**
		 * Adds a suffix to the path.
		 * 
		 * @param suffix
		 *            the suffix to append.
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer suffixPath(String suffix) {
			validatePathChars(suffix, "suffix");
			this.pathSegments.addAll(splitPath(suffix));
			return this;
		}

		/**
		 * Removes all path segments with an index less than {@code index} from
		 * the path.
		 * 
		 * @param start
		 *            the inclusive index of the first kept path segment.
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer truncatePath(int start) {
			truncatePath(start, -1);
			return this;
		}

		/**
		 * Removes all path segments b>not</b> between {@code start} (inclusive)
		 * and {@code end} (exclusive).
		 * 
		 * @param start
		 *            the inclusive index of the first kept path segment.
		 * @param end
		 *            the exclusive index of the last kept path segment.
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer truncatePath(int start, int end) {
			if (end == -1) end = pathSegments.size();
			validateBounds(start, end, pathSegments.size());
			/* Copy to avoid keeping around the list that backs the sublist, and
			 * hence all its elements */
			this.pathSegments = createSegmentsList(pathSegments.subList(start, end));
			return this;
		}

		/**
		 * Replaces the path segments between {@code start} (inclusive) and
		 * {@code end} (exclusive) with the path segments of
		 * {@code replacement}.
		 * 
		 * @param start
		 *            the inclusive start index of the replacement operation.
		 * @param end
		 *            the exclusive end index of the replacement operation.
		 * @param replacement
		 *            a sequence of path segments to replace the sequence of
		 *            path segments between {@code start} (inclusive) and
		 *            {@code end} (exclusive) with.
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer replacePathSegments(int start, int end, String replacement) {
			validateBounds(start, end, pathSegments.size());
			validatePathChars(replacement, "replacement");
			replacePathSegmentsInternal(start, end, replacement);
			return this;
		}

		private void replacePathSegmentsInternal(int start, int end, String replacement) {
			List<String> replacementSegments = splitPath(replacement);
			pathSegments.subList(start, end).clear();
			pathSegments.addAll(start, replacementSegments);
		}

		/**
		 * Removes all path segments between {@code start} (inclusive) and
		 * {@code end} (exclusive).
		 * 
		 * @param start
		 *            the inclusive start index of the removal operation.
		 * @param end
		 *            the exclusive end index of the removal operation.
		 * @return this {@code IdentifierTransformer}
		 */
		public IdentifierTransformer remove(int start, int end) {
			validateBounds(start, end, pathSegments.size());
			pathSegments.subList(start, end).clear();
			return this;
		}

		/**
		 * Creates a new {@code Identifier} from the path and namespace of this
		 * transformer.
		 * 
		 * @return a new {@code Identifier} with the path and namespace of this
		 *         transformer.
		 */
		public Identifier apply() {
			return new Identifier(namespace, String.join(SEGMENT_SEPARATOR, pathSegments));
		}

		private List<String> splitPath(String path) {
			return PATH_SPLITTER.splitToList(path);
		}

		private List<String> splitPathMutable(String path) {
			return createSegmentsList(PATH_SPLITTER.split(path));
		}

		private List<String> createSegmentsList(Iterable<String> elements) {
			return Lists.newLinkedList(elements);
		}
	}

	private static void validateNamespaceChars(String string, String fieldName) {
		if (!VALID_NAMESPACE_CHARS.matcher(string).matches())
			throw new InvalidIdentifierException("Illegal characters in " + fieldName
				+ ". Failed to match against pattern [a-z0-9_.-]+");
	}

	private static void validatePathChars(String string, String fieldName) {
		if (!VALID_PATH_CHARS.matcher(string).matches())
			throw new InvalidIdentifierException("Illegal characters in " + fieldName
				+ ". Failed to match against pattern [a-z0-9/._-]+");
	}

	private static void validateBounds(int start, int end, int segmentCount) {
		if (start < 0 || start >= segmentCount)
			throw new IndexOutOfBoundsException(String.format("Start index value %d is outside of the range [0, %d)",
				start, segmentCount));
		if (end < 0 || end > segmentCount)
			throw new IndexOutOfBoundsException(String.format("End index value %d is outside of the range [0, %d]", end,
				segmentCount));
		if (start > end)
			throw new IllegalArgumentException(String.format("Start index %d is greater than end index %d", start,
				end));
	}
}
