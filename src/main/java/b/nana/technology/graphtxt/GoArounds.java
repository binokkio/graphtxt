package b.nana.technology.graphtxt;

import java.util.HashMap;
import java.util.Map;

public class GoArounds {

    private final Map<NodeTxt, GoAround> goArounds = new HashMap<>();

    public GoArounds(Nodes nodes, Rows rows) {

        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            for (NodeTxt node : row) {
                for (Edge edge : node.getEdges()) {
                    NodeTxt to = nodes.get(edge.getTo());
                    int j = rows.getRowIndex(to);
                    if (j - i > 1)
                        goArounds.computeIfAbsent(to, x -> new GoAround()).updateHeight(j - i);
                }
            }
        }

        // sort by top descending, height ascending
    }
}
