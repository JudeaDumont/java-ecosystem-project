package testutil;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class PostgresDataSource {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/sampleDB";
    public static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    private PostgresDataSource() {

    }

    public static DataSource getDataSource() {
        // Creates a new instance of DriverManagerDataSource and sets
        // the required parameters such as the Jdbc Driver class,
        // Jdbc URL, database username and password.
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(PostgresDataSource.DRIVER);
        dataSource.setUrl(PostgresDataSource.JDBC_URL);
        dataSource.setUsername(PostgresDataSource.USERNAME);
        dataSource.setPassword(PostgresDataSource.PASSWORD);

        Properties connectionProperties = new Properties();
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }
}