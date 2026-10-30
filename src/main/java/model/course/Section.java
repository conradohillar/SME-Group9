package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.exam.Exam;
import model.user.Student;
import model.user.User;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TheoreticalSection.class, name = "THEORETICAL"),
        @JsonSubTypes.Type(value = PracticalSection.class, name = "PRACTICAL"),
        @JsonSubTypes.Type(value = Exam.class, name = "EXAM")
})
public abstract class Section implements CompletableByStudent {
    private final String title;
    private final String description;

    @JsonCreator
    protected Section(@JsonProperty("title") String title, @JsonProperty("description") String description) {
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompletedByStudent(Student student) {
        //By defualt, the sections don't have to necessarily be completed (only the unit's exam), although they could in the future.
        return true;
    }
}