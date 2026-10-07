package model.course;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private List<Section> sections;

    public Lesson() {
        this.sections = new ArrayList<>();
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection(Section section) {
        this.sections.remove(section);
    }
}
