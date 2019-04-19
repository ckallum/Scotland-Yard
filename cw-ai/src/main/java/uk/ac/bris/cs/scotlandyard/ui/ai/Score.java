package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.*;
import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Score {
    private ScotlandYardView view;
    private DGraph graph;
    private int source;

    public Score(ScotlandYardView view, int mrxLocation, DGraph graph) {
        this.view = view;
        this.graph = graph;
        this.source = mrxLocation;
    }

}
