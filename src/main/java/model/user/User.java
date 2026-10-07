package model.user;

import model.subscription.Subscription;
import model.subscription.SubscriptionFactory;
import model.subscription.SubscriptionTier;

import java.time.LocalDateTime;

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

    private String getName() {
        return name;
    }

    private String getUsername() {
        return username;
    }
}
