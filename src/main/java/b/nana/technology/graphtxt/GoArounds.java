package b.nana.technology.graphtxt;

import java.util.LinkedHashMap;
import java.util.Map;

public class GoArounds {

    private final Map<NodeTxt, GoAround> goArounds = new LinkedHashMap<>();

    public GoArounds(Nodes nodes, Rows rows) {

        int x = 0;
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            for (NodeTxt node : row) {
                for (Edge edge : node.getEdges()) {
                    NodeTxt to = nodes.get(edge.getTo());
                    int j = rows.getRowIndex(to);
                    if (j - i > 1) {
                        GoAround goAround = goArounds.computeIfAbsent(to, t -> new GoAround());
                        goAround.setX(x++);
                        goAround.updateHeight(j - i);
                    }
                }
            }
        }

        // TODO improve x
    }

    public int getWidth() {
        return goArounds.size() + 1;
    }

    public GoAround get(NodeTxt to) {
        return goArounds.get(to);
    }
}
