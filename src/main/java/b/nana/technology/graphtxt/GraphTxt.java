package b.nana.technology.graphtxt;

import java.util.*;

public class GraphTxt {

    final Nodes nodes;

    public GraphTxt(Collection<Node> nodes) {
        this.nodes = new Nodes(nodes);
    }

    public String getText() {

        Roots roots = new Roots(nodes);
        Rows rows = new Rows(nodes, roots);
        Canvas canvas = new Canvas(rows.getWidth(), rows.getHeight());

        for (Row row : rows) {
            int coast = 0;
            for (NodeTxt node : row) {
                if (!node.getNode().getEdges().isEmpty()) {
                    boolean incrementCoast = false;
                    for (Edge edge : node.getNode().getEdges()) {
                        NodeTxt to = nodes.get(edge.getTo());
                        node.renderEdge(canvas, coast, to);
                        if (node.getCenterX() != to.getCenterX())
                            incrementCoast = true;
                    }
                    if (incrementCoast)
                        coast++;
                }
            }
        }

        for (NodeTxt node : nodes) {
            node.renderNode(canvas);
        }

        return canvas.getText();
    }
}
