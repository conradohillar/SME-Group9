package model.user;

public class User {
    private String name = null;
    private String username = null;

    private String email = null;
    private String password = null;

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;

        // TODO: write state in a JSON file
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        // TODO compare to saved password in JSON file
        return true;
    }
}
