package model.course;

import model.LearningMaterial;
import model.user.Student;
import model.user.User;

import java.util.List;
import java.util.Set;

public class TheoreticalSection extends Section {
    private final List<LearningMaterial> materials;

    public TheoreticalSection(String title, String description, List<LearningMaterial> materials) {
        super(title, description);
        this.materials = List.copyOf(materials);
    }

    // TODO: esto de activeFeatures hay que cambiarlo a la implementacion actual
    public List<LearningMaterial> getVisibleMaterials(Set<String> activeFeatures) {
        return materials.stream()
                .filter(material -> activeFeatures.contains(material.getLearningMaterialName()))
                .toList();
    }
}
