package model.course;

public class ExamResult {
    private final Course course;
    private final Difficulty difficulty;
    private final Unit unit;
    private double score;
    private boolean passed;

    public ExamResult(Course course, Difficulty difficulty ,Unit unit) {
        this.course = course;
        this.difficulty = difficulty;
        this.unit = unit;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isPassed() {
        return passed;
    }


}