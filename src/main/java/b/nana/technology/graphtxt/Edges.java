package b.nana.technology.graphtxt;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Edges implements Iterable<Edge> {

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

    @Override
    public Iterator<Edge> iterator() {
        return edges.iterator();
    }
}
