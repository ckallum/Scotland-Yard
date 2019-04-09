package uk.ac.bris.cs.fxkit;

import java.util.function.Function;

import javafx.util.StringConverter;

public class LambdaStringConverter<T> extends StringConverter<T> {

	private final Function<? super T, String> toString;
	private final Function<String, T> fromString;

	private LambdaStringConverter(Function<? super T, String> toString,
			Function<String, T> fromString) {
		this.toString = toString;
		this.fromString = fromString;
	}

	public static <T> LambdaStringConverter<T> of(Function<? super T, String> toString,
			Function<String, T> fromString) {
		return new LambdaStringConverter<>(toString, fromString);
	}

	public static <T> LambdaStringConverter<T> forwardOnly(Function<? super T, String> toString) {
		return new LambdaStringConverter<>(toString, throwUnsupportedForNonForward());
	}

	public static <T> LambdaStringConverter<T> forwardOnly(String defaultValue,
			Function<? super T, String> toString) {
		return new LambdaStringConverter<>(t -> t == null ? defaultValue : toString.apply(t),
				throwUnsupportedForNonForward());
	}

	private static <T> Function<String, T> throwUnsupportedForNonForward() {
		return s -> {
			throw new UnsupportedOperationException("Forward only conversion, cannot convert from "
					+ (s == null ? "null" : s.getClass().toString()) + " to String");
		};
	}

	@Override
	public String toString(T object) {
		return toString.apply(object);
	}

	@Override
	public T fromString(String string) {
		return fromString.apply(string);
	}
}
