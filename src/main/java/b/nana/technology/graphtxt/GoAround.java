package b.nana.technology.graphtxt;

import java.util.HashSet;
import java.util.Set;

public class GoAround {

    private final NodeTxt from;
    private final Set<NodeTxt> to = new HashSet<>();
    final int x;

    public GoAround(NodeTxt from, int x) {
        this.from = from;
        this.x = x;
    }

    public NodeTxt getFrom() {
        return from;
    }

    public Set<NodeTxt> getTo() {
        return to;
    }

    public void addTo(NodeTxt to) {
        this.to.add(to);
    }
}
