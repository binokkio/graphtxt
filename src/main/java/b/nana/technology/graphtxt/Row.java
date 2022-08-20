package b.nana.technology.graphtxt;

import java.util.ArrayList;

public class Row extends ArrayList<NodeTxt> {

    public boolean isXFree(int x) {
        for (NodeTxt nodeTxt : this)
            if (nodeTxt.hitsX(x))
                return false;
        return true;
    }
}
