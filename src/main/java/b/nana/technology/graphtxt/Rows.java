package b.nana.technology.graphtxt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Rows extends HashMap<Integer, Row> {

    public void optimize() {

        for (int i = 0; i < 499; i++) {

            int rowIndex = i % size();
            int rowAboveIndex = rowIndex - 1;
            int rowBelowIndex = rowIndex + 1;

            Row row = get(rowIndex);
            Row rowAbove = get(rowAboveIndex);
            Row rowBelow = get(rowBelowIndex);

            Map<NodeTxt, Score> scores = new HashMap<>();
            for (NodeTxt nodeTxt : row) {
                scores.put(nodeTxt, getAdjacentRowPull(nodeTxt, rowAbove, rowBelow));
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

            AtomicBoolean movement = new AtomicBoolean();

            scores.entrySet().stream()
                    .sorted(Comparator.comparingDouble(e -> -Math.abs(e.getValue().getScore())))
                    .forEach(entry -> {
                        NodeTxt node = entry.getKey();
                        Score score = entry.getValue();

                        if (score.getScore() <= -1 && row.isXFree(node.getX() - 1)) {
                            node.setX(node.getX() - 1);
                            movement.set(true);
                        } else if (score.getScore() >= -1 && row.isXFree(node.getX() + node.getWidth() + 1)) {
                            node.setX(node.getX() + 1);
                            movement.set(true);
                        }
                    });
        }
    }

    private Score getAdjacentRowPull(NodeTxt node, Row rowAbove, Row rowBelow) {

        Score score = new Score();

        int nodeCenterX = node.getCenterX();

        if (rowAbove != null) {
            for (NodeTxt other : rowAbove) {
                if (other.linksTo(node)) {
                    score.update(other.getCenterX() - nodeCenterX);
                }
            }
        }

        if (rowBelow != null) {
            for (NodeTxt other : rowBelow) {
                if (node.linksTo(other)) {
                    score.update(other.getCenterX() - nodeCenterX);
                }
            }
        }

        return score;
    }
}
