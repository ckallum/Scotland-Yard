package uk.ac.bris.cs.scotlandyard.model;


import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;



public class ScotlandYardModel implements ScotlandYardGame, Consumer<Move>, MoveVisitor {
    private Graph<Integer, Transport> graph;
    private List<Boolean> rounds;
    private List<ScotlandYardPlayer> players = new CopyOnWriteArrayList<>();
    private List<Spectator> spectators = new CopyOnWriteArrayList<>();
    private ScotlandYardPlayer mrX;
    private int mrXLastLocation;
    private int currentPlayer=0;
    private int currentRound = ScotlandYardView.NOT_STARTED;

    public ScotlandYardModel(List<Boolean> rounds, Graph<Integer, Transport> graph,
                             PlayerConfiguration mrX, PlayerConfiguration firstDetective,
                             PlayerConfiguration... restOfTheDetectives) {
        this.rounds = requireNonNull(rounds);
        this.graph = requireNonNull(graph);

        //Checking if rounds and graphs are empty
        if (rounds.isEmpty()) throw new IllegalArgumentException("Empty rounds.");
        if (graph.isEmpty()) throw new IllegalArgumentException("Empty graph.");
        //Check MrX should be Black
        if (mrX.colour.isDetective()) {
            throw new IllegalArgumentException("MrX should be Black");
        }

        //Check Players are Non Null
        ArrayList<PlayerConfiguration> configurations = new ArrayList<>();
        for (PlayerConfiguration player : restOfTheDetectives) {
            configurations.add(requireNonNull(player));
        }
        configurations.add(0, requireNonNull(firstDetective));
        configurations.add(0, requireNonNull(mrX));
        for (PlayerConfiguration x : configurations) {
            players.add(new ScotlandYardPlayer(x.player, x.colour, x.location, x.tickets));
        }

        this.mrX = players.get(0);


        //No Duplicate Locations or Colours
        Set<Integer> locations = new HashSet<>();
        Set<Colour> colours = new HashSet<>();
        for (PlayerConfiguration configuration : configurations) {
            if (locations.contains(configuration.location)) throw new IllegalArgumentException("Duplicate location");
            locations.add(configuration.location);
            if (colours.contains(configuration.colour)) throw new IllegalArgumentException("Duplicate colour");
            colours.add(configuration.colour);
        }

        //Checks Ticket Validity
        for (ScotlandYardPlayer player : players) {
            for (Ticket t : Ticket.values()) {
                if (!player.tickets().containsKey(t)) throw new IllegalArgumentException("Player Missing Ticket");
            }
            if (player.isDetective()) {
                if ((player.hasTickets(Ticket.SECRET)) || (player.hasTickets(Ticket.DOUBLE))) {
                    throw new IllegalArgumentException("Detective has Double or Secret Ticket");
                }
            }
        }

    }

    @Override
    public void registerSpectator(Spectator spectator) {
        if (spectators.contains(spectator)) throw new IllegalArgumentException("Duplicate Spectator");
        spectators.add(requireNonNull(spectator));
    }

    @Override

    public void unregisterSpectator(Spectator spectator) {
        requireNonNull(spectator);
        if (spectators.isEmpty()) throw new IllegalArgumentException("No Spectators to remove");
        else spectators.remove(requireNonNull(spectator));
    }

    @Override
    public void startRotate() {
        if (currentPlayer == 0 && isGameOver()) throw new IllegalStateException("Game is already over");
        else {
            mrX.player().makeMove(this, mrX.location(), validMoves(mrX), this);
        }
    }

    private boolean NoDetectiveAtLocation(int destination) {
        for (ScotlandYardPlayer player : players){
            if(player.isDetective() && (player.location() == destination)) return false;
        }
        return true;
    }

    private Set<Move> validMoves(ScotlandYardPlayer player) {
        Set<Move> moves = new HashSet<>();
        if(player.isDetective())moves.addAll(findMoves(player,player.location()));
        else{
            Set<TicketMove> firstMoves = new HashSet<>(findMoves(player, player.location()));
            moves.addAll(firstMoves);
            if(player.hasTickets(Ticket.DOUBLE)&&(currentRound < rounds.size()-1)){
                for(TicketMove firstMove:firstMoves){
                    Set<TicketMove> secondMoves = new HashSet<>(findMoves(player, firstMove.destination()));
                    for(TicketMove secondMove : secondMoves){
                        if(player.hasTickets(secondMove.ticket())) moves.add(new DoubleMove(mrX.colour(),firstMove,secondMove));
                        if(firstMove.ticket()==secondMove.ticket() && (!player.hasTickets(firstMove.ticket(),2))) moves.remove(new DoubleMove(mrX.colour(),firstMove,secondMove));
                    }
                }
            }
        }
        if(moves.isEmpty() && player.isDetective()){
            moves.add(new PassMove(player.colour()));
        }
        return moves;
    }

    private Set<TicketMove> findMoves(ScotlandYardPlayer player, int location){
        Set<TicketMove> tempMoves = new HashSet<>();
        Collection<Edge<Integer,Transport>> edges = graph.getEdgesFrom(graph.getNode(location));
        for(Edge<Integer, Transport> e : edges){
            Ticket t = Ticket.fromTransport(e.data());
            int destination = e.destination().value();
            if (player.hasTickets(t) && NoDetectiveAtLocation(destination)){
                tempMoves.add(new TicketMove(player.colour(), t, destination));
            }
            if (player.hasTickets(Ticket.SECRET) && NoDetectiveAtLocation(destination)){
                tempMoves.add(new TicketMove(player.colour(), Ticket.SECRET, destination));
            }
        }
        return tempMoves;
    }

    private void incrementPlayer(){
        if (currentPlayer == players.size()-1){currentPlayer = 0;}
        else currentPlayer++;
    }

    private void nextMove(){
        ScotlandYardPlayer player = players.get(currentPlayer);
        player.player().makeMove(this, player.location(), validMoves(player), this);
    }

    private boolean isRevealRound(int round){
        return (rounds.get(round));
    }

    private void notifySpectatorsOnMoveMade(ScotlandYardView v, Move move){
        for (Spectator s : spectators){
            s.onMoveMade(v, move);
        }
    }

    private void notifySpectatorsOnRotationComplete(){
        for(Spectator s : spectators){
            s.onRotationComplete(this);
        }
    }

    private void notifySpectatorsOnRoundStarted() {
        currentRound++;
        for (Spectator s : spectators){
            s.onRoundStarted(this, currentRound);
        }
    }

    public void accept(Move move) {
        requireNonNull(move);
        if (!validMoves(players.get(currentPlayer)).contains(move)) throw new IllegalArgumentException("invalid move");
        move.visit(this);
        if (isGameOver()) {
            for (Spectator s : spectators) {
                s.onGameOver(this, getWinningPlayers());
            }
        } else {
            if (players.get(currentPlayer).isDetective()) nextMove();
            else notifySpectatorsOnRotationComplete();
        }
    }

    public void visit(PassMove move){
        incrementPlayer();
        notifySpectatorsOnMoveMade(this, move);
    }

    public void visit(TicketMove move){
        ScotlandYardPlayer player = players.get(currentPlayer);
        player.location(move.destination());
        player.removeTicket(move.ticket());
        if(player.isDetective()) {
            mrX.addTicket(move.ticket());
            incrementPlayer();
        }
        else {
            if (isRevealRound(currentRound)) {
                mrXLastLocation = move.destination();
            } else {
                move = new TicketMove(mrX.colour(), move.ticket(), mrXLastLocation);
            }
            incrementPlayer();
            notifySpectatorsOnRoundStarted();
        }
        notifySpectatorsOnMoveMade(this, move);
    }

    public void visit(DoubleMove move){
        TicketMove firstMove = move.firstMove();
        TicketMove secondMove = move.secondMove();
        incrementPlayer();
        mrX.removeTicket(Ticket.DOUBLE);
        if(!isRevealRound(currentRound)){
            if(isRevealRound(currentRound+1)){
                notifySpectatorsOnMoveMade(this, new DoubleMove(mrX.colour(), firstMove.ticket(),mrXLastLocation,secondMove.ticket(),secondMove.destination()));
            }
            else{
                notifySpectatorsOnMoveMade(this, new DoubleMove(mrX.colour(), firstMove.ticket(),mrXLastLocation,secondMove.ticket(),mrXLastLocation));
            }
        }
        else {
            if(isRevealRound(currentRound+1)){
                notifySpectatorsOnMoveMade(this, move);}
            else notifySpectatorsOnMoveMade(this, new DoubleMove(mrX.colour(), firstMove.ticket(),firstMove.destination(),secondMove.ticket(),firstMove.destination()));

        }
        UpdateDoubleMove(firstMove);
        UpdateDoubleMove(secondMove);

    }

    private void UpdateDoubleMove(TicketMove Move) {
        mrX.location(Move.destination());
        mrX.removeTicket(Move.ticket());
        TicketMove m = Move;
        if(!isRevealRound(currentRound)){
            m = new TicketMove(mrX.colour(), Move.ticket(), mrXLastLocation);
        }
        else mrXLastLocation= Move.destination();
        notifySpectatorsOnRoundStarted();
        notifySpectatorsOnMoveMade(this, m);
    }

    @Override
    public Collection<Spectator> getSpectators() {
        return Collections.unmodifiableCollection(spectators);
    }

    @Override
    public List<Colour> getPlayers() {
        List<Colour> playerColours = new ArrayList<>();
        for (ScotlandYardPlayer p: players){
            playerColours.add(p.colour());
        }
        return Collections.unmodifiableList(playerColours);
    }

    @Override
    public Set<Colour> getWinningPlayers() {
        Set<Colour> winningPlayers = new HashSet<>();
        if (mrXStuck() || mrXCaught()) {
            for (ScotlandYardPlayer p : players) if (p.isDetective()) winningPlayers.add(p.colour());
        }
        else if (detectivesStuck() || noMoreRounds()) winningPlayers.add(mrX.colour());
        return Collections.unmodifiableSet(winningPlayers);
    }

    private boolean noMoreRounds(){
        return (currentRound == rounds.size() && currentPlayer == 0);
    }

    private boolean detectivesStuck() {
        for(ScotlandYardPlayer p : players){
            if(p.isDetective()){
                if(!validMoves(p).contains(new PassMove(p.colour()))) return false;
            }
        }
        return true;
    }

    private boolean mrXStuck() {
        return (players.get(currentPlayer).isMrX() && validMoves(mrX).isEmpty());
    }

    private boolean mrXCaught() {
        for (ScotlandYardPlayer player : players) {
            if (player.isDetective() && (player.location() == mrX.location())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Integer> getPlayerLocation(Colour colour) {
        for (ScotlandYardPlayer player : players) {
            if (player.colour() == colour) {
                if(player.isMrX()) return Optional.of(mrXLastLocation);
                else return Optional.of(player.location());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getPlayerTickets(Colour colour, Ticket ticket) {
        for (ScotlandYardPlayer player : players) {
            if (player.colour() == colour) return Optional.of(player.tickets().get(ticket));
        }
        return Optional.empty();
    }

    @Override
    public boolean isGameOver() {
        return (!getWinningPlayers().isEmpty());
    }

    @Override
    public Colour getCurrentPlayer() {
        return (players.get(currentPlayer).colour());
    }

    @Override
    public int getCurrentRound() {
        return currentRound;
    }

    @Override
    public List<Boolean> getRounds() {
        return Collections.unmodifiableList(rounds);
    }

    @Override
    public Graph<Integer, Transport> getGraph() {
        return new ImmutableGraph<>(graph);
    }

}
