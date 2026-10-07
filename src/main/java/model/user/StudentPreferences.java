package model.user;

public class StudentPreferences {
    private boolean explanations;
    private boolean hints;
    private boolean aiChatbot;

    public StudentPreferences(boolean explanations, boolean hints, boolean aiChatbot) {
        this.explanations = explanations;
        this.hints = hints;
        this.aiChatbot = aiChatbot;
    }

    public boolean wantsExplanations() {
        return explanations;
    }

    public void setExplanations(boolean explanations) {
        this.explanations = explanations;
    }

    public boolean wantsHints() {
        return hints;
    }

    public void setHints(boolean hints) {
        this.hints = hints;
    }

    public boolean wantsAiChatbot() {
        return aiChatbot;
    }

    public void setAiChatbot(boolean aiChatbot) {
        this.aiChatbot = aiChatbot;
    }
}
