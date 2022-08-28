package b.nana.technology.graphtxt;

import java.util.HashSet;
import java.util.Set;

public class GoAround {

    private final NodeTxt to;
    private final Set<NodeTxt> from = new HashSet<>();
    final int x;
    private final int height;
    final int incomingGoAroundOffset;

    public GoAround(Row row, NodeTxt to, int x, int height) {
        this.to = to;
        this.x = x;
        this.height = height;
        this.incomingGoAroundOffset = row.claimIncomingGoAroundOffset();
    }

    public NodeTxt getTo() {
        return to;
    }

    public Set<NodeTxt> getFrom() {
        return from;
    }

    public void addFrom(NodeTxt from) {
        this.from.add(from);
    }
}
