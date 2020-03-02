package Model;

public class LogicalRunway
{
    private final int lda;
    private final int tora;
    private final int toda;
    private final int asda;
    private final int degree;
    private final String designation;
    private final Direction direction;

    public LogicalRunway(String designator, int tora, int toda, int asda, int lda){
        this.asda = asda;
        this.toda = toda;
        this.tora = tora;
        this.lda = lda;

        if (designator.length() < 3)
            designator = "0" + designator;

        degree = Integer.parseInt(designator.substring(0, 2));
        designation = designator.substring(2, 3);
        direction = (degree <= 18) ? Direction.LEFT : Direction.RIGHT;
    }

    @Override
    public String toString() {
        return getName();
    }

    String getName() { return ((degree < 10) ? "0" + degree : degree) + designation; }

    public int getDegree() { return degree * 10; }

    Direction getDirection() { return direction; }

    int getLda() {
        return lda;
    }

    int getTora() {
        return tora;
    }

    int getToda() {
        return toda;
    }

    int getAsda() {
        return asda;
    }

    int getDisplacedThreshold() { return tora - lda; }

    String getResults()
    {
        String result = "";

        result += "Runway " + getName() + ":" + "\n";
        result += "LDA: " + getLda() + "\n";
        result += "TORA: " + getTora() + "\n";
        result += "TODA: " + getToda()+ "\n";
        result += "ASDA: " + getAsda()+ "\n\n";

        return result;
    }
}
