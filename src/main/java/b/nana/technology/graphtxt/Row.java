package b.nana.technology.graphtxt;

import java.util.ArrayList;

public class Row extends ArrayList<NodeTxt> {

    public int getWidth() {
        NodeTxt last = get(size() - 1);
        return last.getX() + last.getWidth();
    }

    public boolean isXFree(int x) {
        for (NodeTxt nodeTxt : this)
            if (nodeTxt.hitsX(x))
                return false;
        return true;
    }

    public void placeNodesSideBySide() {
        int columnOffset = 0;
        for (NodeTxt node : this) {
            node.setX(columnOffset);
            columnOffset += node.getWidth() + 1;
        }
    }
}
