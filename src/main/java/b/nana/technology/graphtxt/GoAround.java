package b.nana.technology.graphtxt;

public class GoAround {

    int x;
    private int height;

    public void setX(int x) {
        this.x = x;
    }

    public void updateHeight(int height) {
        this.height = Math.max(this.height, height);
    }
}
