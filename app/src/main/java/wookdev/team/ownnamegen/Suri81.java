package wookdev.team.ownnamegen;

/**
 * Created by wook on 2017-05-24.
 */

public class Suri81 {
    public static final String EVAL_GOOD = "길한수";
    public static final String EVAL_NORMAL = "보통수";
    public static final String EVAL_BAD = "흉한수";

    private int suri;
    private String name;
    private String explain;
    private String evaluation;

    public Suri81(int suri, String name, String explain, String evaluation) {
        this.suri = suri;
        this.name = name;
        this.explain = explain;
        this.evaluation = evaluation;
    }

    public int getSuri() {
        return this.suri;
    }

    public String getName() {
        return this.name;
    }

    public String getExplain() {
        return this.explain;
    }

    public String getEvaluation() {
        return this.evaluation;
    }
}
