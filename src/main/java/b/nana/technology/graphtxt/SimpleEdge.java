package b.nana.technology.graphtxt;

public class SimpleEdge implements Edge {

    private final String to;

    public SimpleEdge(String to) {
        this.to = to;
    }

    @Override
    public String getTo() {
        return to;
    }
}
