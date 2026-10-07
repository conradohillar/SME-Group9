package model.course;

import model.ContentType;

import java.util.List;

public class TheoreticalSection implements Section {
    private List<ContentType> learningMaterials;

    public TheoreticalSection(List<ContentType> learningMaterials) {
        this.learningMaterials = learningMaterials;
    }
}
