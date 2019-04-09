package uk.ac.bris.cs.scotlandyard.harness;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.ac.bris.cs.scotlandyard.harness.CodeGenRecorder.CodeGen;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.PassMove;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Spectator;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.GREEN;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;
import static uk.ac.bris.cs.scotlandyard.model.Colour.WHITE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.YELLOW;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
public class GameModelSequencePUMLCodeGen implements CodeGen {

	private int maxRound = 0;
	private List<String> interactions = new ArrayList<>();

	@Override public String name() { return "GameModelSequencePUMLCodeGen"; }
	@Override
	public void movePicked(ImmutableScotlandYardView seed,
	                       ScotlandYardView view, int location, Set<Move> moves, Move picked) {

		ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);

		String current = named(now.getCurrentPlayer());


		if (picked.colour().isMrX()) {
			interactions.add("UI -> ScotlandYardModel : startRotate()");
			interactions.add("group rotation");
			interactions.add(format("rnote over ScotlandYardModel: Round=%s, CurrentPlayer=%s",
					now.currentRound, current));
		}

		interactions.add(
				format("ScotlandYardModel -> %s: makeMove(V,int,Set<Move>,Consumer<Move>)",
						current));
		interactions.add(format("activate %s", current));
		interactions.add(format("\t%s-->ScotlandYardModel : Consumer.accept(%s)",
				current, ticketName(picked)));
		interactions.add(format("deactivate %s", current));


	}
	@Override public Spectator mkSpectator(ImmutableScotlandYardView seed) {
		return new Spectator() {
			@Override public void onMoveMade(ScotlandYardView view, Move move) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				if (move.colour().isDetective() || move instanceof DoubleMove)
					appendViewRNote(now, "");
				interactions.add(format(
						"ScotlandYardModel->Spectator: onMoveMade(V, %s)", ticketName(move)));

			}
			@Override public void onRoundStarted(ScotlandYardView view, int round) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				if (previousPlayer(now).isMrX()) appendViewRNote(now, "");
				interactions.add(format(
						"ScotlandYardModel->Spectator: onRoundStarted(V, %d)", round));
				maxRound = round;
			}
			@Override public void onRotationComplete(ScotlandYardView view) {
				interactions.add("ScotlandYardModel->Spectator: onRotationComplete(V)");
				interactions.add("end");
			}
			@Override public void onGameOver(ScotlandYardView view, Set<Colour> winningPlayers) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				interactions.add(format("ScotlandYardModel->Spectator: onGameOver(V, [%s])",
						now.players.stream().map(v -> named(v.colour)).collect(joining(","))));
				appendViewRNote(now, ", GameOver=true");
				interactions.add("end");
			}

			private void appendViewRNote(ImmutableScotlandYardView now, String extra) {
				interactions.add(format("rnote over ScotlandYardModel: " +
								"Round=%s, " +
								"CurrentPlayer=%s" +
								"%s",
						now.currentRound, named(now.getCurrentPlayer()), extra));
			}
			private Colour previousPlayer(ImmutableScotlandYardView view) {
				int index = view.getPlayers().indexOf(view.getCurrentPlayer());
				System.out.println(index);
				if (index < 0) throw new AssertionError();
				if (index == 0) return view.getPlayers().get(view.getPlayers().size());
				return view.getPlayers().get(index - 1);
			}
		};
	}
	@Override public String readOut(ImmutableScotlandYardView seed,
	                                ImmutableMap<Colour, PlayerConfiguration> configs,
	                                String graphMethod) {

		List<String> lines = new ArrayList<>();

		lines.add("@startuml\n" +
				"\n" +
				"skinparam monochrome true");
		lines.add(format("title Sample game sequence(%d player, %d rounds)",
				seed.players.size(), maxRound));
		lines.add("legend right\n" +
				"Type legend\n" +
				"In package: uk.ac.bris.cs.scotlandyard.model\n" +
				"\tS - interface Spectator\n" +
				"\tM - class ScotlandYardModel\n" +
				"\tV - class ScotlandYardView\n" +
				"\tP - interface Player\n" +
				"endlegend\n" +
				"\n" +
				"skinparam ParticipantPadding 0");
		lines.add("actor UI\n" +
				"participant Spectator <<(S, #BBBBBB)>>\n" +
				"participant ScotlandYardModel <<(M, #BBBBBB) " +
				"java.util.function.Consumer<Move> >>");
		lines.add("box \"Players\" #LightBlue\n");
		lines.add(seed.players.stream()
				.map(v -> format("\tparticipant %s <<(P, #BBBBBB)>>", named(v.colour)))
				.collect(joining("\n")));
		lines.add("end box\n");
		lines.add("autonumber\n");


		lines.addAll(interactions);

		lines.add("@enduml");

		return lines.stream().collect(joining("\n"));
	}


	private static String ticketName(Move move) {
		if (move instanceof TicketMove) return "TicketMove";
		if (move instanceof DoubleMove) return "DoubleMove";
		if (move instanceof PassMove) return "PassMove";
		throw new AssertionError();
	}


	private static final ImmutableMap<Colour, String> COLOUR_LOOK_UP =
			ImmutableMap.<Colour, String>builder()
					.put(BLACK, "MrX")
					.put(BLUE, "Blue")
					.put(GREEN, "Green")
					.put(RED, "Red")
					.put(WHITE, "White")
					.put(YELLOW, "Yellow").build();
	private static String named(Colour colour) { return COLOUR_LOOK_UP.get(colour); }


}
