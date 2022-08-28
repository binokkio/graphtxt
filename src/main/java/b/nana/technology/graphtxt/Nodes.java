package b.nana.technology.graphtxt;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Nodes implements Iterable<NodeTxt> {

    private final Map<Object, NodeTxt> nodes;

    public Nodes(Collection<Node> nodes) {
        if (nodes.isEmpty()) throw new IllegalArgumentException("empty nodes");
        this.nodes = nodes.stream().collect(Collectors.toMap(
                Node::getId,
                NodeTxt::new,
                (a, b) -> a,
                LinkedHashMap::new));
    }

    public NodeTxt get(Object id) {
        return nodes.get(id);
    }

    @Override
    public Iterator<NodeTxt> iterator() {
        return nodes.values().iterator();
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
}
