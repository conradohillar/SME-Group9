package view.components.exercise;

import model.ContentType;
import model.Image;

import javax.swing.*;
import java.io.File;

public class UploadImageView extends ExerciseView {
    private final JButton uploadButton;
    private final JLabel fileLabel;

    private File selectedFile;

    public UploadImageView() {
        uploadButton = new JButton("Upload image");
        fileLabel = new JLabel("No image selected");

        uploadButton.addActionListener(e -> chooseImage());
        this.add(uploadButton);
        this.add(fileLabel);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileLabel.setText(selectedFile.getName());
        }
    }

    @Override
    public ContentType getAnswer() {
        if (selectedFile == null) {
            return null;
        }
        return new Image(selectedFile.getAbsolutePath());
    }

    @Override
    public void setAnswerEnabled(boolean enabled) {
        uploadButton.setEnabled(enabled);
    }
}