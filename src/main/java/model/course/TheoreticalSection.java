package model.course;

import model.LearningMaterial;

import java.util.List;
import java.util.Set;

public class TheoreticalSection extends Section {
    private final List<LearningMaterial> materials;

    public TheoreticalSection(String title, List<LearningMaterial> materials) {
        super(title);
        this.materials = List.copyOf(materials);
    }

    @Override
    public List<LearningMaterial> getVisibleMaterials(Set<String> activeFeatures) {
        return materials.stream()
                .filter(material -> activeFeatures.contains(material.getLearningMaterialName()))
                .toList();
    }
}
