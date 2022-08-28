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
        GoArounds goArounds = new GoArounds(nodes, rows);
        Canvas canvas = new Canvas(goArounds.getWidth()+ rows.getWidth(), rows.getHeight());

        nodes.translateX(goArounds.getWidth());

        for (Row row : rows) {
            int coast = 0;
            for (NodeTxt node : row) {
                if (!node.getNode().getEdges().isEmpty()) {
                    boolean incrementCoast = false;
                    for (Edge edge : node.getNode().getEdges()) {
                        NodeTxt to = nodes.get(edge.getTo());
                        if (rows.getRowIndex(node) + 1 != rows.getRowIndex(to)) {
                            node.renderEdge(canvas, coast, goArounds.get(to), to);
                            incrementCoast = true;
                        } else {
                            node.renderEdge(canvas, coast, to);
                            if (node.getCenterX() != to.getCenterX())
                                incrementCoast = true;
                        }

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
