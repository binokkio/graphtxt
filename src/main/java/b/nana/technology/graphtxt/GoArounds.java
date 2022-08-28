package b.nana.technology.graphtxt;

import java.util.LinkedHashMap;
import java.util.Map;

public class GoArounds {

    private final Map<NodeTxt, GoAround> goArounds = new LinkedHashMap<>();

    public GoArounds(Nodes nodes, Rows rows) {

        int x = 0;
        for (int fromRowIndex = 0; fromRowIndex < rows.size(); fromRowIndex++) {
            Row fromRow = rows.get(fromRowIndex);
            for (NodeTxt from : fromRow) {
                for (Edge edge : from.getEdges()) {
                    NodeTxt to = nodes.get(edge.getTo());
                    int toRowIndex = rows.getRowIndex(to);
                    int rowDistance = toRowIndex - fromRowIndex;
                    if (rowDistance > 1) {
                        GoAround goAround = goArounds.get(from);
                        if (goAround == null) {
                            goAround = new GoAround(from, x++);
                            goArounds.put(from, goAround);
                        }
                        goAround.addTo(to);
                    }
                }
            }
        }

        // TODO improve x
    }

    public int getWidth() {
        return goArounds.isEmpty() ? 0 : goArounds.size() + 1;
    }

    public GoAround get(NodeTxt to) {
        return goArounds.get(to);
    }

    public boolean isAtStartOfGoAround(NodeTxt node) {
        return goArounds.containsKey(node);
    }

    public boolean isAtEndOfGoAround(NodeTxt node) {
        return goArounds.values().stream().anyMatch(goAround -> goAround.getTo().contains(node));
    }
}
