package b.nana.technology.graphtxt;

import java.util.*;
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
        for (int i = 1; i < rows.size(); i++) {
            Map<Double, Node> nodeOrder = new HashMap<>();
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
                nodeOrder.put(score, node);
            }
            rows.put(i, nodeOrder.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList()));
        }




        // experimental rendering
        char[][][] canvas = new char[40][80][4];
        rows.forEach((row, nodes) -> {
            int columnOffset = 0;
            for (Node node : nodes) {
                NodeTxt nodeTxt = new NodeTxt(node);
                nodeTxt.render(canvas, row * 4, columnOffset);
                columnOffset += nodeTxt.getWidth() + 1;
            }
        });

        StringBuilder text = new StringBuilder();
        for (int y = 0; y < canvas.length; y++) {
            char[][] row = canvas[y];
            for (int x = 0; x < row.length; x++) {
                char[] pixel = row[x];
                if (pixel[0] != 0) {
                    for (int i = 0; i < pixel.length; i++) {
                        if (pixel[i] != 0) {
                            text.append(pixel[i]);
                        }
                    }
                } else {
                    text.append(' ');
                }
            }
            text.append('\n');
        }


        System.out.println(text);

        return null;
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
