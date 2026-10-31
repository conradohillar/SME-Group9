package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.LearningMaterial;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;

public class TheoreticalSection extends Section {
    private final List<LearningMaterial> materials;

    @JsonCreator
    public TheoreticalSection(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("materials") List<LearningMaterial> materials) {
        super(title, description, FeatureType.THEORETICAL_SECTION);
        this.materials = List.copyOf(materials);
    }

    public List<LearningMaterial> getLearningMaterials() {
        return materials;
    }

    @Override
    public List<String> getCurrentStateLog() {
        return new ArrayList<>(List.of(new String[]{"TheoreticalSection: { title: " + getTitle() + " }"}));
    }
}
