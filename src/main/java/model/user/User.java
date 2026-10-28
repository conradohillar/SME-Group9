package model.user;

import java.util.Objects;

public class User {
    private String name;
    private String username;

    private String email;
    private String password;

    public User(String name, String username, String email, String password) {
        this.name = Objects.requireNonNull(name);
        this.username = Objects.requireNonNull(username);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
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
