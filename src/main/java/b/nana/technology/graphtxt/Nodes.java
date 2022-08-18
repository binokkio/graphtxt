package b.nana.technology.graphtxt;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Nodes {

    private final GraphTxt graphTxt;
    private final Map<Object, NodeTxt> nodes;

    public Nodes(GraphTxt graphTxt, Collection<Node> nodes) {
        if (nodes.isEmpty()) throw new IllegalArgumentException("empty nodes");
        this.graphTxt = graphTxt;
        this.nodes = nodes.stream().collect(Collectors.toMap(
                Node::getId,
                node -> new NodeTxt(graphTxt, node),
                (a, b) -> a,
                LinkedHashMap::new));
    }

    public NodeTxt get(Object id) {
        return nodes.get(id);
    }

    Integer getIndex(NodeTxt node) {
        int index = 0;
        for (NodeTxt test : nodes.values()) {
            if (test == node) return index;
            else index++;
        }
        throw new IllegalStateException("Node not found");
    }

    public Collection<NodeTxt> values() {
        return nodes.values();
    }

    public Stream<NodeTxt> stream() {
        return values().stream();
    }
}
