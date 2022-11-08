package testutil;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

import static testutil.YamlParser.getPostgreDataSource;

public class PostgresDataSource {
    private static final String DRIVER = "org.postgresql.Driver";

    private PostgresDataSource() {

    }

    public static DataSource getDataSource() {
        Map<String, String> postgreDataSource = getPostgreDataSource();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(PostgresDataSource.DRIVER);
        dataSource.setUrl(postgreDataSource.get("app.datasource.jdbc-url"));
        dataSource.setUsername(postgreDataSource.get("app.datasource.username"));
        dataSource.setPassword(postgreDataSource.get("app.datasource.password"));

        Properties connectionProperties = new Properties();
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }
}