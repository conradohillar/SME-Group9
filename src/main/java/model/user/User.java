package model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.feature.Feature;
import model.feature.FeatureType;

import java.util.List;

public class User extends Feature {
    private final String name;
    private final String username;
    private final String password;

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("username") String username, @JsonProperty("password") String password) {
        super(FeatureType.USER);
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    @Override
    public List<String> getCurrentStateLog() {
        return List.of();
    }
}
