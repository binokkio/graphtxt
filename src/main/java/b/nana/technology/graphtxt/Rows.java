package b.nana.technology.graphtxt;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Rows implements Iterable<Row> {

    private final Nodes nodes;
    private final List<Row> rows;
    private final Map<NodeTxt, Integer> rowAssignments;

    public Rows(Nodes nodes, Roots roots) {
        this.nodes = nodes;
        this.rows = new ArrayList<>();
        this.rowAssignments = new LinkedHashMap<>();

        // populate rows and rowAssignments
        roots.forEach(root -> assignRows(root, new ArrayDeque<>(), rowAssignments));
        rowAssignments.forEach((node, row) -> put(row, node));

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
            rows.set(i, nodeOrder.entrySet().stream().sorted(comparator).map(Map.Entry::getValue).collect(Collectors.toCollection(Row::new)));
        }

        // initial placement
        int maxRowWidth = 0;
        for (Row row : rows) {
            row.placeNodesSideBySide();
            maxRowWidth = Math.max(maxRowWidth, row.getWidth());
        }

        // optimize placement
        optimize(maxRowWidth);
    }

    public int getWidth() {
        int maxRowWidth = 0;
        for (Row row : rows) {
            maxRowWidth = Math.max(maxRowWidth, row.getWidth());
        }
        return maxRowWidth;
    }

    public int getHeight() {
        return rows.get(rows.size() - 1).get(0).getY() + 3;
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    private void put(int index, NodeTxt node) {

        if (index >= rows.size()) {
            for (int i = rows.size(); i <= index; i++) {
                rows.add(new Row());
            }
        }

        rows.get(index).add(node);
    }

    private void optimize(int canvasWidth) {
        optimizeX(canvasWidth);
        optimizeY();
    }

    private void optimizeX(int canvasWidth) {
        while (optimizeXStep(canvasWidth, true, false));
        while (optimizeXStep(canvasWidth, false, true));
        while (optimizeXStep(canvasWidth, true, true));
    }

    private boolean optimizeXStep(int canvasWidth, boolean aboveHasPull, boolean belowHasPull) {
        AtomicBoolean movement = new AtomicBoolean();

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {

            Row row = rows.get(rowIndex);
            Row rowAbove = rowIndex > 0 ? rows.get(rowIndex - 1) : null;
            Row rowBelow = rowIndex < rows.size() - 1 ? rows.get(rowIndex + 1) : null;

            Map<NodeTxt, Score> scores = new HashMap<>();
            for (NodeTxt nodeTxt : row) {
                scores.put(nodeTxt, getAdjacentRowPull(nodeTxt, rowAbove, rowBelow, aboveHasPull, belowHasPull));
            }

            for (int nodeIndex = 0; nodeIndex < row.size(); nodeIndex++) {
                NodeTxt node = row.get(nodeIndex);
                if (nodeIndex > 0 && scores.get(node).getScore() <= -1) {
                    NodeTxt neighbor = row.get(nodeIndex - 1);
                    if (neighbor.hitsX(node.getX() - 1)) {
                        scores.get(neighbor).update(scores.get(node));
                    }
                }
                if (nodeIndex < row.size() - 1 && scores.get(node).getScore() >= 1) {
                    NodeTxt neighbor = row.get(nodeIndex + 1);
                    if (neighbor.hitsX(node.getX() + node.getWidth() + 1)) {
                        scores.get(neighbor).update(scores.get(node));
                    }
                }
            }

            scores.entrySet().stream()
                    .sorted(Comparator.comparingDouble(e -> -Math.abs(e.getValue().getScore())))
                    .forEach(entry -> {
                        NodeTxt node = entry.getKey();
                        Score score = entry.getValue();
                        if (score.getScore() <= -1 && node.getX() - 1 >= 0 && row.isXFree(node.getX() - 1)) {
                            node.setX(node.getX() - 1);
                            movement.set(true);
                        } else if (score.getScore() >= 1 && node.getX() + node.getWidth() + 1 <= canvasWidth && row.isXFree(node.getX() + node.getWidth() + 1)) {
                            node.setX(node.getX() + 1);
                            movement.set(true);
                        }
                    });
        }

        return movement.get();
    }

    private Score getAdjacentRowPull(NodeTxt node, Row rowAbove, Row rowBelow, boolean aboveHasPull, boolean belowHasPull) {

        Score score = new Score();

        int nodeCenterX = node.getCenterX();

        if (aboveHasPull && rowAbove != null) {
            for (NodeTxt other : rowAbove) {
                if (other.linksTo(node)) {
                    score.update(other.getCenterX() - nodeCenterX);
                }
            }
        }

        if (belowHasPull && rowBelow != null) {
            for (NodeTxt other : rowBelow) {
                if (node.linksTo(other)) {
                    score.update(other.getCenterX() - nodeCenterX);
                }
            }
        }

        return score;
    }

    private void optimizeY() {
        int rowOffset = 0;
        for (int i = 0; i < rows.size(); i++) {
            int rowHeight = 0;
            for (NodeTxt node : rows.get(i)) {
                node.setY(rowOffset);
                for (Edge edge : node.getEdges()) {
                    NodeTxt to = nodes.get(edge.getTo());
                    // TODO check rowAssignment.get(to) != i + 1 first
                    if (node.getCenterX() != to.getCenterX()) {
                        rowHeight++;
                        break;
                    }
                }
            }
            rowOffset += 3 + rowHeight;
        }
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
