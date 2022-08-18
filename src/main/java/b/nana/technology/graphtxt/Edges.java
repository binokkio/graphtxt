package b.nana.technology.graphtxt;

import java.util.List;
import java.util.stream.Stream;

public class Edges {

    private final List<Edge> edges;

    public Edges(List<Edge> edges) {
        this.edges = edges;
    }

    public boolean isEmpty() {
        return edges.isEmpty();
    }

    public boolean linksTo(NodeTxt root) {
        return edges.stream().anyMatch(edge -> edge.getTo().equals(root.getNode().getId()));
    }

    public Stream<Edge> stream() {
        return edges.stream();
    }
}
