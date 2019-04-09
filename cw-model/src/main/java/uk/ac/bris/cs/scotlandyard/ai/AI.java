package uk.ac.bris.cs.scotlandyard.ai;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI.VisualiserType;

/**
 * <b> Not a public API, do not use!</b> <br>
 * Internal representation of an AI
 */
public final class AI {

	private final String name;
	private final VisualiserType type;
	private final Class<? extends PlayerFactory> factoryClass;

	private AI(ManagedAI ai, Class<? extends PlayerFactory> factoryClass) {
		requireNonNull(ai);
		this.name = ai.value();
		this.type = ai.visualiserType();
		this.factoryClass = requireNonNull(factoryClass);
	}

	private AI(String name, VisualiserType type, Class<? extends PlayerFactory> factoryClass) {
		this.name = requireNonNull(name);
		this.type = requireNonNull(type);
		this.factoryClass = requireNonNull(factoryClass);
	}

	public static AI fromName(String name, VisualiserType type,
			Class<? extends PlayerFactory> factory) {
		return new AI(name, type, factory);
	}

	public String getName() {
		return name;
	}

	public VisualiserType getVisualiserType() {
		return type;
	}

	public PlayerFactory instantiate() {
		try {
			return factoryClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException
					| IllegalAccessException
					| NoSuchMethodException
					| InvocationTargetException e) {
			throw new RuntimeException(
					"Unable to create AI " + name + "; instantiation of " + factoryClass
							+ " failed, the class must be top-level and have a public no-arg constructor",
					e);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AI ai1 = (AI) o;
		return Objects.equals(getName(), ai1.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				       .add("name", name)
				       .add("type", type)
				       .add("factoryClass", factoryClass)
				       .toString();
	}

	@SuppressWarnings("unchecked")
	public static List<AI> scanClasspath() {
		List<String> annotated = new FastClasspathScanner().scan()
				.getNamesOfClassesWithAnnotation(ManagedAI.class);
		List<AI> ais = annotated.stream().map(c -> {
			try {
				Class<?> clazz = Class.forName(c);
				if (!PlayerFactory.class.isAssignableFrom(clazz))
					throw new IllegalArgumentException(
							c + " does not implement " + PlayerFactory.class);
				return new AI(clazz.getAnnotation(ManagedAI.class), (Class<PlayerFactory>) clazz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}).collect(toList());
		Map<AI, Long> map = ais.stream().collect(groupingBy(identity(), counting()));
		Map<AI, Long> histogram = map.entrySet().stream().filter(e -> e.getValue() > 1)
				.collect(toMap(Entry::getKey, Entry::getValue));
		if (!histogram.isEmpty()) {
			throw new RuntimeException("AIs with same name are not allowed as it becomes "
					+ "ambiguous which AI is selected during runtime. Occurrence is  " + histogram);
		}
		return ais;
	}

}
