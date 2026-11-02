package model;

import java.util.List;

public interface LoggableState {
    default List<String> getCurrentStateLog() {
        return List.of();
    }
}
