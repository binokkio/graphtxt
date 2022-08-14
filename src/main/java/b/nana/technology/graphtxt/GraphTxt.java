package b.nana.technology.graphtxt;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphTxt {

    private final Map<String, Node> nodes;

    public GraphTxt(List<Node> nodes) {
        if (nodes.isEmpty()) throw new IllegalArgumentException("empty nodes");
        this.nodes = nodes.stream().collect(Collectors.toMap(
                Node::getId,
                n -> n,
                (a, b) -> a,
                LinkedHashMap::new));
    }

    private Integer getNodeIndex(Node node) {
        int index = 0;
        for (Node test : nodes.values()) {
            if (test == node) return index;
            else index++;
        }
        throw new IllegalStateException("Node not found");
    }

    public String getText() {

        Set<Node> roots = getRoots();

        // determine rows
        Map<Integer, List<Node>> rows = new HashMap<>();
        Map<Node, Integer> rowAssignment = new LinkedHashMap<>();
        roots.forEach(root ->
            assignRows(root, new ArrayDeque<>(), rowAssignment));
        rowAssignment.forEach((node, row) ->
                rows.computeIfAbsent(row, x -> new ArrayList<>()).add(node));

        // optimize order of nodes within rows
        Comparator<Map.Entry<Score, Node>> comparator = Map.Entry.comparingByKey();
        comparator = comparator.thenComparing(e -> getNodeIndex(e.getValue()));
        for (int i = 1; i < rows.size(); i++) {
            Map<Score, Node> nodeOrder = new HashMap<>();
            for (Node node : rows.get(i)) {
                int weight = 0;
                double score = 0;
                for (int r = 0; r < i; r++) {
                    List<Node> row = rows.get(r);
                    for (int j = 0; j < row.size(); j++) {
                        if (row.get(j).getEdges().stream().map(Edge::getTo).anyMatch(node.getId()::equals)) {
                            score = (score * weight + j) / ++weight;
                        }
                    }
                }
                nodeOrder.put(new Score(score), node);
            }
            rows.put(i, nodeOrder.entrySet().stream().sorted(comparator).map(Map.Entry::getValue).collect(Collectors.toList()));
        }

        // experimental rendering
        AtomicInteger rowOffset = new AtomicInteger();
        Map<Integer, List<NodeTxt>> nodeTxtsByRow = new HashMap<>();
        Map<String, NodeTxt> nodeTxtsById = new HashMap<>();
        rows.forEach((row, nodes) -> {
            int columnOffset = 0;
            for (Node node : nodes) {
                NodeTxt nodeTxt = new NodeTxt(node);
                nodeTxtsByRow.computeIfAbsent(row, x -> new ArrayList<>()).add(nodeTxt);
                nodeTxtsById.put(node.getId(), nodeTxt);
                nodeTxt.setPosition(columnOffset, rowOffset.get());
                columnOffset += nodeTxt.getWidth() + 1;
            }
            rowOffset.addAndGet(5 + (int) nodes.stream().filter(n -> !n.getEdges().isEmpty()).count());
        });

        // calculate canvas size
        int width = nodeTxtsById.values().stream()
                .mapToInt(node -> node.getX() + node.getWidth())
                .max()
                .orElseThrow();

        int height = nodeTxtsById.values().stream()
                .mapToInt(node -> node.getY() + 3)
                .max()
                .orElseThrow();

        Canvas canvas = new Canvas(width, height);

        nodeTxtsByRow.forEach((row, nodeTxts) -> {
            int coast = 1;
            for (NodeTxt nodeTxt : nodeTxts) {
                if (!nodeTxt.getNode().getEdges().isEmpty()) {
                    for (Edge edge : nodeTxt.getNode().getEdges())
                        nodeTxt.renderEdge(canvas, coast, nodeTxtsById.get(edge.getTo()));
                    coast++;
                }
            }
        });

        nodeTxtsByRow.values().stream().flatMap(Collection::stream).forEach(nt -> nt.renderNode(canvas));

        return canvas.getText();
    }

    private Set<Node> getRoots() {
        Set<Node> roots = new LinkedHashSet<>(nodes.values());
        roots.removeIf(root -> nodes.values().stream().anyMatch(node -> node.getEdges().stream().anyMatch(edge -> edge.getTo().equals(root.getId()))));
        if (roots.isEmpty()) roots.add(nodes.values().iterator().next());
        return roots;
    }

    private void assignRows(Node node, ArrayDeque<Node> route, Map<Node, Integer> rowAssignments) {
        if (!route.contains(node)) {

            Integer currentRow = rowAssignments.get(node);
            if (currentRow == null || currentRow < route.size())
                rowAssignments.put(node, route.size());

            route.addLast(node);
            node.getEdges().stream().map(edge -> nodes.get(edge.getTo())).forEach(
                    next -> assignRows(next, route, rowAssignments));
            route.removeLast();
        }
    }
}
