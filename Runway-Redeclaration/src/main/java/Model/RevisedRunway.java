package Model;

public class RevisedRunway
{
    private final LogicalRunway revisedRunway1;
    private final LogicalRunway revisedRunway2;
    private final Obstacle obstacle;
    private final Position position;
    private final StringBuilder calcBreakdown = new StringBuilder();

    // Constants
    private static final int RESA = 240;
    private static final int ALS = 50;
    private static final int TOCS = 50;
    private static final int STRIP_END = 60;
    private static final int BLAST_ALLOWANCE = 300;

    public RevisedRunway(Runway runway, Obstacle obstacle, Position position)
    {
        this.obstacle = obstacle;
        this.position = position;

        revisedRunway1 = calculateValues(runway.getLogicalRunway1());
        revisedRunway2 = calculateValues(runway.getLogicalRunway2());
    }

    public LogicalRunway getRevisedRunway1() { return revisedRunway1; }

    public LogicalRunway getRevisedRunway2() { return revisedRunway2; }

    public Obstacle getObstacle() { return obstacle; }

    public Position getPosition() { return position; }

    public String getResults() { return revisedRunway1.getResults() + revisedRunway2.getResults(); }

    public String getCalcBreakdown() { return calcBreakdown.toString(); }

    private LogicalRunway calculateValues(LogicalRunway runway)
    {
        Direction obstacleSide = (position.getDistLThresh() >= position.getDistRThresh()) ?
                Direction.LEFT : Direction.RIGHT;
        int distanceFromThreshold = (runway.getDirection() == Direction.LEFT) ?
                position.getDistLThresh() : position.getDistRThresh();
        boolean towardsObstacle = runway.getDirection() == obstacleSide;

        calcBreakdown.append("Runway ").append(runway.getName()).append(":\n");

        int lda = towardsObstacle ?
                calculateRevisedLDA(distanceFromThreshold) :
                calculateRevisedLDA(runway.getLda(), distanceFromThreshold);
        int tora = towardsObstacle ?
                calculateRevisedTORA(distanceFromThreshold, runway.getDisplacedThreshold()) :
                calculateRevisedTORA(runway.getTora(), distanceFromThreshold, runway.getDisplacedThreshold());

        int toda = tora;
        int asda = tora;

        if (towardsObstacle)
        {
            calcBreakdown.append("TODA:\nRevised TORA (").append(tora)
                    .append(") = ").append(toda).append("\n");
            calcBreakdown.append("ASDA:\nRevised TORA (").append(tora)
                    .append(") = ").append(asda).append("\n\n");
        }
        else
        {
            int clearway = (runway.getToda() - runway.getTora());
            int stopway = (runway.getAsda() - runway.getTora());

            toda = tora + clearway;
            asda = tora + stopway;

            calcBreakdown.append("TODA:\nRevised TORA (").append(tora).append(") + ")
                    .append("Clearway (").append(clearway)
                    .append(") = ").append(toda).append("\n");
            calcBreakdown.append("ASDA:\nRevised TORA (").append(tora).append(") + ")
                    .append("Stopway (").append(stopway)
                    .append(") = ").append(asda).append("\n\n");
        }

        return new LogicalRunway(runway.getName(), tora, toda, asda, lda);
    }

    private int calculateRevisedLDA(int lda, int distanceFromThreshold)
    {
        int tempThreshold;
        int displacement;

        calcBreakdown.append("LDA:\nOriginal LDA (").append(lda)
                .append(") - Distance from Threshold (").append(distanceFromThreshold).append(") - ");

        // Ensure the largest of the possible two values is picked to enforce safety.
        if (obstacle.getHeight() * ALS >= RESA)
        {
            tempThreshold = obstacle.getHeight() * ALS;
            calcBreakdown.append("Slope Calculation (");
        }
        else
        {
            tempThreshold = RESA;
            calcBreakdown.append("RESA (");
        }

        calcBreakdown.append(tempThreshold).append(") - ");

        // Ensure the largest of the possible two values is picked to enforce safety.
        if ((tempThreshold + STRIP_END) >= BLAST_ALLOWANCE)
        {
            displacement = tempThreshold + STRIP_END;
            calcBreakdown.append("Strip End (").append(STRIP_END);
        }
        else
        {
            displacement = BLAST_ALLOWANCE;
            calcBreakdown.append("Blast Allowance (").append(BLAST_ALLOWANCE);
        }

        int result = lda - distanceFromThreshold - displacement;

        calcBreakdown.append(") = ").append(result).append("\n");

        return result;
    }

    private int calculateRevisedLDA(int distanceFromThreshold)
    {
        int result = distanceFromThreshold - RESA - STRIP_END;

        calcBreakdown.append("LDA:\nDistance From Threshold (").append(distanceFromThreshold)
                .append(") - RESA (").append(RESA)
                .append(") - Strip End (").append(STRIP_END)
                .append(") = ").append(result).append("\n");

        return result;
    }

    private int calculateRevisedTORA(int distanceFromThreshold, int displacedThreshold)
    {
        int tempThreshold;

        calcBreakdown.append("TORA:\nDistance from Threshold (").append(distanceFromThreshold).append(")");

        if (displacedThreshold != 0)
                calcBreakdown.append(" + Displaced Threshold (").append(displacedThreshold).append(")");

        // Ensure the largest of the possible two values is picked to enforce safety.
        if (obstacle.getHeight() * TOCS >= RESA)
        {
            tempThreshold = obstacle.getHeight() * TOCS;
            calcBreakdown.append(" - Slope Calculation (");
        }
        else
        {
            tempThreshold = RESA;
            calcBreakdown.append("RESA (");
        }

        int result = distanceFromThreshold + displacedThreshold - tempThreshold - STRIP_END;

        calcBreakdown.append(tempThreshold).append(") - Strip End (").append(STRIP_END)
                .append(") = ").append(result).append("\n");

        return distanceFromThreshold + displacedThreshold - tempThreshold - STRIP_END;
    }

    private int calculateRevisedTORA(int tora, int distanceFromThreshold, int displacedThreshold)
    {
        int result = tora - BLAST_ALLOWANCE - distanceFromThreshold - displacedThreshold;

        calcBreakdown.append("TORA:\nOriginal TORA (").append(tora)
                .append(") - Blast Allowance (").append(BLAST_ALLOWANCE)
                .append(") - Distance From Threshold (").append(distanceFromThreshold);

        if (displacedThreshold != 0)
            calcBreakdown.append(") - Displaced Threshold (").append(displacedThreshold);

        calcBreakdown.append(") = ").append(result).append("\n");

        return result;
    }
}