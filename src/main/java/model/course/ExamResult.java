package model.course;

public class ExamResult {
    private Course course;
    private Difficulty difficulty;
    private Unit unit;
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