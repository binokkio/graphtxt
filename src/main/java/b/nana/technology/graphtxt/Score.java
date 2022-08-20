package b.nana.technology.graphtxt;

public class Score implements Comparable<Score> {

    private double score;
    private int weight;

    public void update(double value) {
        score = (score * weight + value) / ++weight;
    }

    public void update(Score score) {
        this.score = (this.score * weight + score.score * score.weight) / (weight + score.weight);
        this.weight = weight + score.weight;
    }

    @Override
    public int compareTo(Score other) {
        return Double.compare(score, other.score);
    }

    public double getScore() {
        return score;
    }
}
