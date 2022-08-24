package com.webapi.webapi.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcConnection {

    private static final Logger LOGGER =
            Logger.getLogger(JdbcConnection.class.getName());
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) { //todo:hide credentials
            String url = "jdbc:postgresql://localhost:5432/sampleDB";
            String user = "postgres";
            String password = "root";

            try {
                connection =
                        DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return connection;
    }
}
