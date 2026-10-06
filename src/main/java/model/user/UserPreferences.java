package model.user;

public class UserPreferences {
    private boolean explanations;
    private boolean hints;
    private boolean aiChatbot;

    private boolean getExplanations() {
        return explanations;
    }

    private void setExplanations(boolean explanations) {
        this.explanations = explanations;
    }

    private boolean getHints() {
        return hints;
    }

    private void setHints(boolean hints) {
        this.hints = hints;
    }

    private boolean getAiChatbot() {
        return aiChatbot;
    }

    private void setAiChatbot(boolean aiChatbot) {
        this.aiChatbot = aiChatbot;
    }
}
