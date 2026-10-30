package model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import database.Database;

import java.util.Objects;

public class User {
    private final String name;
    private final String username;
    private final String password;

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("username") String username, @JsonProperty("password") String password) {
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

}
