package uk.ac.bris.cs.scotlandyard.ai;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;

import javafx.scene.layout.Pane;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

/**
 * <b> Not a public API, do not use!</b> <br>
 * Internal pooling mechanism for AIs. AIs that are on the same group will share
 * a common GameFactory.
 * 
 * @param <G> the group, must be stable with proper {@link Object#hashCode()}
 *        and {@link Object#equals(Object)}
 */
public class AIPool<G> {

	private final VisualiserSurface surface;
	private final Consumer<Throwable> exceptionHandler;
	private final Map<G, AIGroup> groups = new HashMap<>();

	public AIPool(VisualiserSurface surface, Consumer<Throwable> exceptionHandler) {
		this.surface = surface;
		this.exceptionHandler = exceptionHandler;
	}

	public void addToGroup(G group, Colour colour, AI ai) {
		groups.computeIfAbsent(group, g -> new AIGroup()).add(colour, ai);
	}

	public void initialise(ResourceManager manager, ScotlandYardGame game) {
		groups.values().forEach(group -> {
			try {
				group.initialise(manager, game);
			} catch (Exception e) {
				e.printStackTrace();
				exceptionHandler.accept(e);
			}
		});
	}

	public Optional<Player> createPlayer(Colour colour) {
		List<Player> created = groups.values()
				.stream()
				.map(group -> group.createPlayer(colour))
				.flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
				.collect(toList());
		if (created.isEmpty()) return Optional.empty();
		if (created.size() != 1)
			throw new IllegalArgumentException(colour + " existed in multiple groups");
		return Optional.of(created.get(0));
	}

	public void terminate() {
		groups.values().forEach((group) -> {
			try {
				group.terminate();
			} catch (Exception e) {
				exceptionHandler.accept(e);
			}
		});
		surface.onDestroy();
	}

	public interface VisualiserSurface {

		Pane onCreate(AI ai);

		void onDestroy();

	}

	class AIGroup {

		private final Map<Colour, AI> ais = new HashMap<>();
		private Map<AI, PlayerFactory> factories = new HashMap<>();

		void add(Colour colour, AI ai) {
			ais.put(colour, ai);
		}

		void initialise(ResourceManager manager, ScotlandYardGame game) throws Exception {
			factories = ais.values().stream()
					.distinct()
					.collect(toMap(Function.identity(), AI::instantiate));
			factories.forEach((ai, factory) -> {
				factory.createSpectators(game).forEach(game::registerSpectator);
				Pane pane = surface.onCreate(ai);
				factory.ready(() -> pane, manager);
			});
		}

		void terminate() throws Exception {
			factories.values().forEach(PlayerFactory::finish);
		}

		public Optional<Player> createPlayer(Colour colour) {
			if (!ais.containsKey(colour)) return Optional.empty();
			return Optional.of(
					new ThreadedPlayer(
							factories.get(ais.get(colour)).createPlayer(colour),
							exceptionHandler));
		}

	}

	static class ThreadedPlayer implements Player {

		final static ExecutorService service = Executors.newWorkStealingPool();

		private final Player player;
		private final Consumer<Throwable> exceptionHandler;

		private ThreadedPlayer(Player player, Consumer<Throwable> exceptionHandler) {
			this.player = player;
			this.exceptionHandler = exceptionHandler;
		}

		@Override
		public void makeMove(ScotlandYardView view,
				int location,
				Set<Move> moves,
				Consumer<Move> callback) {
			service.submit((Callable<Void>) () -> {
				try {
					player.makeMove(view, location, ImmutableSet.copyOf(moves), callback);
				} catch (Throwable e) {
					e.printStackTrace();
					exceptionHandler.accept(e);
				}
				return null;
			});
		}
	}

}
