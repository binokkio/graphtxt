package b.nana.technology.graphtxt;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphTxt {

    final Nodes nodes;

    public GraphTxt(Collection<Node> nodes) {
        this.nodes = new Nodes(this, nodes);
    }

    public String getText() {

        Set<NodeTxt> roots = getRoots();

        // determine row assignments
        Map<NodeTxt, Integer> rowAssignment = new LinkedHashMap<>();
        roots.forEach(root ->
            assignRows(root, new ArrayDeque<>(), rowAssignment));

        // create rows
        Rows rows = new Rows();
        rowAssignment.forEach((node, row) ->
                rows.computeIfAbsent(row, x -> new Row()).add(node));

        // optimize order of nodes within rows
        Comparator<Map.Entry<Score, NodeTxt>> comparator = Map.Entry.comparingByKey();
        comparator = comparator.thenComparing(e -> nodes.getIndex(e.getValue()));
        for (int i = 1; i < rows.size(); i++) {
            Map<Score, NodeTxt> nodeOrder = new HashMap<>();
            for (NodeTxt node : rows.get(i)) {
                Score score = new Score();
                for (int r = 0; r < i; r++) {
                    List<NodeTxt> row = rows.get(r);
                    for (int j = 0; j < row.size(); j++) {
                        if (row.get(j).linksTo(node)) {
                            score.update(j);
                        }
                    }
                }
                nodeOrder.put(score, node);
            }
            rows.put(i, nodeOrder.entrySet().stream().sorted(comparator).map(Map.Entry::getValue).collect(Collectors.toCollection(Row::new)));
        }

        // initial placement
        AtomicInteger rowOffset = new AtomicInteger();
        Map<Integer, List<NodeTxt>> nodeTxtsByRow = new HashMap<>();
        rows.forEach((row, nodes) -> {
            int columnOffset = 0;
            for (NodeTxt node : nodes) {
                nodeTxtsByRow.computeIfAbsent(row, x -> new ArrayList<>()).add(node);
                node.setPosition(columnOffset, rowOffset.get());
                columnOffset += node.getWidth() + 1;
            }
            rowOffset.addAndGet(5 + (int) nodes.stream().filter(NodeTxt::hasEdges).count());
        });

        // calculate canvas size
        int width = nodes.stream()
                .mapToInt(node -> node.getX() + node.getWidth())
                .max()
                .orElseThrow();

        int height = nodes.stream()
                .mapToInt(node -> node.getY() + 3)
                .max()
                .orElseThrow();

        Canvas canvas = new Canvas(width, height);

        nodeTxtsByRow.forEach((row, nodeTxts) -> {
            int coast = 1;
            for (NodeTxt nodeTxt : nodeTxts) {
                if (!nodeTxt.getNode().getEdges().isEmpty()) {
                    for (Edge edge : nodeTxt.getNode().getEdges())
                        nodeTxt.renderEdge(canvas, coast, nodes.get(edge.getTo()));
                    coast++;
                }
            }
        });

        nodeTxtsByRow.values().stream().flatMap(Collection::stream).forEach(nt -> nt.renderNode(canvas));

        return canvas.getText();
    }

    private Set<NodeTxt> getRoots() {
        Set<NodeTxt> roots = new LinkedHashSet<>(nodes.values());
        roots.removeIf(root -> nodes.values().stream().anyMatch(n -> n.linksTo(root)));
        if (roots.isEmpty()) roots.add(nodes.values().iterator().next());
        return roots;
    }

    private void assignRows(NodeTxt node, ArrayDeque<NodeTxt> route, Map<NodeTxt, Integer> rowAssignments) {
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
