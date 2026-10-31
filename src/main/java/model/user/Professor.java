package model.user;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    public Professor(String name, String username, String email, String password) {
        super(name, username, password);
    }

    // will not be implemented for now, just mocked

    @Override
    public List<String> getCurrentStateLog() {
        return  new ArrayList<>(List.of("professor log (TODO)"));
    }
}
