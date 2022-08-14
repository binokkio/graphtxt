package b.nana.technology.graphtxt;

public class Score implements Comparable<Score> {

    private final double score;

    public Score(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Score other) {
        return Double.compare(score, other.score);
    }
}
