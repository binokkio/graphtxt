package b.nana.technology.graphtxt;

public class Score implements Comparable<Score> {

    private double score;
    private int weight;

    public void update(double value) {
        score = (score * weight + value) / ++weight;
    }

    @Override
    public int compareTo(Score other) {
        return Double.compare(score, other.score);
    }
}
