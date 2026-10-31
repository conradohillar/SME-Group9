package view.components.exercise;

import model.ContentType;
import model.Text;

import javax.swing.*;
import java.awt.FlowLayout;
import java.util.List;

public class CompletePhraseView extends ExerciseView {
    private final JTextField answerField;

    public CompletePhraseView(List<String> phrases) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.add(new JLabel(phrases.getFirst()));
        answerField = new JTextField(15);
        this.add(answerField);
        this.add(new JLabel(phrases.get(1)));
    }

    @Override
    public ContentType getAnswer() {
        String answer = answerField.getText().trim();
        if (answer.isEmpty()) {
            return null;
        }

        return new Text(answer);
    }

    @Override
    public void setAnswerEnabled(boolean enabled) {
        answerField.setEnabled(enabled);
    }
}