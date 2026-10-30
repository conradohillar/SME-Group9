package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.LearningMaterial;
import model.user.Student;
import model.user.User;

import java.util.List;
import java.util.Set;

public class TheoreticalSection extends Section {
    private final List<LearningMaterial> materials;

    @JsonCreator
    public TheoreticalSection(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("materials") List<LearningMaterial> materials) {
        super(title, description);
        this.materials = List.copyOf(materials);
    }

    // TODO: esto de activeFeatures hay que cambiarlo a la implementacion actual
    public List<LearningMaterial> getVisibleMaterials() {
        return materials;

//        return materials.stream()
//                .filter(material -> activeFeatures.contains(material.getLearningMaterialName()))
//                .toList();
    }
}
