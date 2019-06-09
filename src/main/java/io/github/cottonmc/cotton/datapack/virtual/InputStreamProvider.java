package io.github.cottonmc.cotton.datapack.virtual;

import com.google.common.base.Charsets;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.*;
import java.util.function.Supplier;

/**
 * A functional interface for providing {@code InputStream} instances.
 */
@FunctionalInterface
public interface InputStreamProvider {
	/**
	 * Creates a new input stream. Streams should not be cached.
	 *
	 * @return an input stream
	 */
	InputStream create() throws IOException;

	/**
	 * Creates an {@code InputStreamProvider} from a {@code Supplier<String>}.
	 *
	 * @param stringSupplier the string supplier
	 * @return an input stream provider
	 */
	static InputStreamProvider of(Supplier<String> stringSupplier) {
		return () -> new ReaderInputStream(new StringReader(stringSupplier.get()), Charsets.UTF_8);
	}

	/**
	 * Creates an {@code InputStreamProvider} from an {@code IOConsumer<OutputStream>}.
	 *
	 * @param outputConsumer the output stream consumer
	 * @return an input stream provider
	 */
	static InputStreamProvider ofOutput(IOConsumer<OutputStream> outputConsumer) {
		return () -> {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			outputConsumer.accept(out);
			return new ByteArrayInputStream(out.toByteArray());
		};
	}

	/**
	 * A consumer interface that can throw an {@code IOException}.
	 * @param <T> the consumed type
	 */
	@FunctionalInterface
	interface IOConsumer<T> {
		void accept(T value) throws IOException;
	}
}
