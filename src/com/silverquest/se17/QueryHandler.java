package com.silverquest.se17;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryHandler {
    void handle(ResultSet rs) throws SQLException;
}
