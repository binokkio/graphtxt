package b.nana.technology.graphtxt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Rows extends HashMap<Integer, Row> {

    public void optimize(int canvasWidth) {
        while (optimizeStep(canvasWidth, false));
        while (optimizeStep(canvasWidth, true));
    }

    public boolean optimizeStep(int canvasWidth, boolean belowHasPull) {
        AtomicBoolean movement = new AtomicBoolean();

        for (int rowIndex = 0; rowIndex < size(); rowIndex++) {

            Row row = get(rowIndex);
            Row rowAbove = get(rowIndex - 1);
            Row rowBelow = get(rowIndex + 1);

            Map<NodeTxt, Score> scores = new HashMap<>();
            for (NodeTxt nodeTxt : row) {
                scores.put(nodeTxt, getAdjacentRowPull(nodeTxt, rowAbove, rowBelow, belowHasPull));
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

    private Score getAdjacentRowPull(NodeTxt node, Row rowAbove, Row rowBelow, boolean belowHasPull) {

        Score score = new Score();

        int nodeCenterX = node.getCenterX();

        if (rowAbove != null) {
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
}
