package b.nana.technology.graphtxt;

public class NodeTxt {

    private static final char[] EDGE_SEGMENTS = new char[] {
            // URDL (up, right, down, left)
            ' ',  // 0000
            ' ',  // 0001
            ' ',  // 0010
            '┐',  // 0011
            ' ',  // 0100
            '─',  // 0101
            '┌',  // 0110
            '┬',  // 0111
            ' ',  // 1000
            '┘',  // 1001
            '│',  // 1010
            '┤',  // 1011
            '└',  // 1100
            '┴',  // 1101
            '├',  // 1110
            '┼',  // 1111
    };

    private static final int DOWN_LEFT_MASK = 0b0011;
    private static final int RIGHT_DOWN_LEFT_MASK = 0b0111;
    private static final int RIGHT_DOWN_MASK = 0b0110;
    private static final int RIGHT_LEFT_MASK = 0b0101;
    private static final int UP_DOWN_MASK = 0b1010;
    private static final int UP_LEFT_MASK = 0b1001;
    private static final int UP_RIGHT_LEFT_MASK = 0b1101;
    private static final int UP_RIGHT_MASK = 0b1100;

    private static final char DOWN_LEFT = EDGE_SEGMENTS[DOWN_LEFT_MASK];
    private static final char RIGHT_DOWN = EDGE_SEGMENTS[RIGHT_DOWN_MASK];
    private static final char RIGHT_DOWN_LEFT = EDGE_SEGMENTS[RIGHT_DOWN_LEFT_MASK];
    private static final char RIGHT_LEFT = EDGE_SEGMENTS[RIGHT_LEFT_MASK];
    private static final char UP_DOWN = EDGE_SEGMENTS[UP_DOWN_MASK];
    private static final char UP_LEFT = EDGE_SEGMENTS[UP_LEFT_MASK];
    private static final char UP_RIGHT = EDGE_SEGMENTS[UP_RIGHT_MASK];
    private static final char UP_RIGHT_LEFT = EDGE_SEGMENTS[UP_RIGHT_LEFT_MASK];


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
        canvas.getPixel(x, y).setContent(RIGHT_DOWN);
        canvas.getPixel(x + getWidth() - 1, y).setContent(DOWN_LEFT);
        canvas.getPixel(x, y + 1).setContent(UP_DOWN);
        canvas.getPixel(x + getWidth() - 1, y + 1).setContent(UP_DOWN);
        canvas.getPixel(x, y + 2).setContent(UP_RIGHT);
        canvas.getPixel(x + getWidth() - 1, y + 2).setContent(UP_LEFT);
        for (int i = 1; i < getWidth() - 1; i++) {
            canvas.getPixel(x + i, y).setContent(RIGHT_LEFT);
            if (i == getWidth() / 2 && y != 0) {
                canvas.getPixel(x + i, y).setContent(UP_RIGHT_LEFT);
            } else {
                canvas.getPixel(x + i, y).setContent(RIGHT_LEFT);
            }
            if (i == getWidth() / 2 && !node.getEdges().isEmpty()) {
                canvas.getPixel(x + i, y + 2).setContent(RIGHT_DOWN_LEFT);
            } else {
                canvas.getPixel(x + i, y + 2).setContent(RIGHT_LEFT);
            }
        }

        for (int i = 0; i < node.getId().length(); i++) {
            canvas.getPixel(x + 2 + i, y + 1).setContent(node.getId().charAt(i));
        }
    }

    public void renderEdge(Canvas canvas, int coast, NodeTxt nodeTxt) {
        int x = this.x + (getWidth() / 2);
        int y = this.y + 3;

        for (int i = 0; i < coast; i++)
            addEdgeSegment(canvas.getPixel(x, y++), UP_DOWN_MASK);

        if (x > nodeTxt.x + nodeTxt.getWidth() / 2) {
            addEdgeSegment(canvas.getPixel(x, y), UP_LEFT_MASK);
            while (x > nodeTxt.x + nodeTxt.getWidth() / 2 + 1)
                addEdgeSegment(canvas.getPixel(--x, y), RIGHT_LEFT_MASK);
            addEdgeSegment(canvas.getPixel(--x, y), RIGHT_DOWN_MASK);
        } else if (x < nodeTxt.x + nodeTxt.getWidth() / 2) {
            addEdgeSegment(canvas.getPixel(x, y), UP_RIGHT_MASK);
            while (x < nodeTxt.x + nodeTxt.getWidth() / 2 - 1)
                addEdgeSegment(canvas.getPixel(++x, y), RIGHT_LEFT_MASK);
            addEdgeSegment(canvas.getPixel(++x, y), DOWN_LEFT_MASK);
        } else {
            addEdgeSegment(canvas.getPixel(x, y), UP_DOWN_MASK);
        }

        while (y < nodeTxt.y)
            addEdgeSegment(canvas.getPixel(x, ++y), UP_DOWN_MASK);
    }

    private void addEdgeSegment(Pixel pixel, int edgeSegmentMask) {
        pixel.setContent(EDGE_SEGMENTS[getEdgeSegmentMask(pixel.getContent()) | edgeSegmentMask]);
    }

    private int getEdgeSegmentMask(char c) {
        for (int i = 0; i < EDGE_SEGMENTS.length; i++)
            if (EDGE_SEGMENTS[i] == c)
                return i;
        return 0;
    }
}
