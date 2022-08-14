package b.nana.technology.graphtxt;

public class NodeTxt {

    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char BOTTOM_RIGHT_CORNER = '┘';
    private static final char CROSS = '┼';
    private static final char HORIZONTAL_LINE = '─';
    private static final char HORIZONTAL_LINE_WITH_INCOMING_EDGE = '┴';
    private static final char HORIZONTAL_LINE_WITH_OUTGOING_EDGE = '┬';
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char TOP_RIGHT_CORNER = '┐';
    private static final char VERTICAL_LINE = '│';

    private final Node node;

    private int x;
    private int y;

    public NodeTxt(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public int getWidth() {
        return node.getId().length() + 4;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void renderNode(Canvas canvas) {
        canvas.getPixel(x, y).setContent(TOP_LEFT_CORNER);
        canvas.getPixel(x + getWidth() - 1, y).setContent(TOP_RIGHT_CORNER);
        canvas.getPixel(x, y + 1).setContent(VERTICAL_LINE);
        canvas.getPixel(x + getWidth() - 1, y + 1).setContent(VERTICAL_LINE);
        canvas.getPixel(x, y + 2).setContent(BOTTOM_LEFT_CORNER);
        canvas.getPixel(x + getWidth() - 1, y + 2).setContent(BOTTOM_RIGHT_CORNER);
        for (int i = 1; i < getWidth() - 1; i++) {
            canvas.getPixel(x + i, y).setContent(HORIZONTAL_LINE);
            if (i == getWidth() / 2 && y != 0) {
                canvas.getPixel(x + i, y).setContent(HORIZONTAL_LINE_WITH_INCOMING_EDGE);
            } else {
                canvas.getPixel(x + i, y).setContent(HORIZONTAL_LINE);
            }
            if (i == getWidth() / 2 && !node.getEdges().isEmpty()) {
                canvas.getPixel(x + i, y + 2).setContent(HORIZONTAL_LINE_WITH_OUTGOING_EDGE);
            } else {
                canvas.getPixel(x + i, y + 2).setContent(HORIZONTAL_LINE);
            }
        }

        for (int i = 0; i < node.getId().length(); i++) {
            canvas.getPixel(x + 2 + i, y + 1).setContent(node.getId().charAt(i));
        }
    }

    public void renderEdge(Canvas canvas, int coast, NodeTxt nodeTxt) {
        int x = this.x + (getWidth() / 2);
        int y = this.y + 3;

        for (int i = 0; i < coast; i++) {
            canvas.getPixel(x, y++).setContent(VERTICAL_LINE);
        }

        if (x > nodeTxt.x + nodeTxt.getWidth() / 2) {
            canvas.getPixel(x, y).setContent(BOTTOM_RIGHT_CORNER);
            while (x > nodeTxt.x + nodeTxt.getWidth() / 2 + 1)
                canvas.getPixel(--x, y).setContent(HORIZONTAL_LINE);
            canvas.getPixel(--x, y).setContent(TOP_LEFT_CORNER);
        } else if (x < nodeTxt.x + nodeTxt.getWidth() / 2) {
            canvas.getPixel(x, y).setContent(BOTTOM_LEFT_CORNER);
            while (x < nodeTxt.x + nodeTxt.getWidth() / 2 - 1)
                canvas.getPixel(++x, y).setContent(HORIZONTAL_LINE);
            canvas.getPixel(++x, y).setContent(TOP_RIGHT_CORNER);
        }

        while (y < nodeTxt.y)
            canvas.getPixel(x, ++y).setContent(VERTICAL_LINE);
    }
}
