package b.nana.technology.graphtxt;

public class NodeTxt {

    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char BOTTOM_RIGHT_CORNER = '┘';
    private static final char HORIZONTAL_LINE = '─';
    private static final char NEW_LINE = '\n';
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char TOP_RIGHT_CORNER = '┐';
    private static final char VERTICAL_LINE = '│';

    private final Node node;

    public NodeTxt(Node node) {
        this.node = node;
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        text.append(TOP_LEFT_CORNER);
        text.append(String.valueOf(HORIZONTAL_LINE).repeat(node.getId().length() + 2));
        text.append(TOP_RIGHT_CORNER);
        text.append(NEW_LINE);
        text.append(VERTICAL_LINE);
        text.append(' ');
        text.append(node.getId());
        text.append(' ');
        text.append(VERTICAL_LINE);
        text.append(NEW_LINE);
        text.append(BOTTOM_LEFT_CORNER);
        text.append(String.valueOf(HORIZONTAL_LINE).repeat(node.getId().length() + 2));
        text.append(BOTTOM_RIGHT_CORNER);
        return text.toString();
    }

    public void render(char[][][] canvas, int y, int x) {
        canvas[y][x][0] = TOP_LEFT_CORNER;
        canvas[y][x + getWidth() - 1][0] = TOP_RIGHT_CORNER;
        canvas[y + 2][x][0] = BOTTOM_LEFT_CORNER;
        canvas[y + 2][x + getWidth() - 1][0] = BOTTOM_RIGHT_CORNER;

        for (int i = 0; i < node.getId().length(); i++) {
            canvas[y + 1][x + 2 + i][0] = node.getId().charAt(i);
        }
    }

    public int getWidth() {
        return node.getId().length() + 4;
    }
}
