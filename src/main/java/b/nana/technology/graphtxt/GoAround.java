package b.nana.technology.graphtxt;

public class GoAround {

    private int height;

    public void updateHeight(int height) {
        this.height = Math.max(this.height, height);
    }
}
