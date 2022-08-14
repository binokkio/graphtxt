package b.nana.technology.graphtxt;

import java.util.Arrays;
import java.util.List;

public class SimpleNode implements Node {

    public static SimpleNode of(String id, String... edges) {
        return new SimpleNode(id, Arrays.stream(edges).map(SimpleEdge::new).toArray(SimpleEdge[]::new));
    }

    public static SimpleNode of(String id, List<String> edges) {
        return new SimpleNode(id, edges.stream().map(SimpleEdge::new).toArray(SimpleEdge[]::new));
    }

    private final String id;
    private final List<Edge> edges;

    public SimpleNode(String id, Edge... edges) {
        this.id = id;
        this.edges = Arrays.asList(edges);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return id;
    }
}
