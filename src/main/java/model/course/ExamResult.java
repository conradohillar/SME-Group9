package model.course;

public class ExamResult {
    private Topic topic;
    private Unit unit;
    private double score;
    private boolean passed;

    public ExamResult(Topic topic, Unit unit, double score, boolean passed) {
        this.topic = topic;
        this.unit = unit;
        this.score = score;
        this.passed = passed;
    }
}