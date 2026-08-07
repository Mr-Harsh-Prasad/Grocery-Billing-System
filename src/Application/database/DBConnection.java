package Application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:grocery.db";

    public static Connection connect() {

        try {
            return DriverManager.getConnection(URL);

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
}