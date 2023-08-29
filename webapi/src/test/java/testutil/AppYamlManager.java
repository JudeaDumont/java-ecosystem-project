package testutil;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AppYamlManager {

    private static AppYamlManager self = null;
    private final Map<String, String> postgresDataSource;

    private String testingUrl;

    private AppYamlManager() {
        postgresDataSource = new HashMap<>();
        loadYamlFile();
    }

    private static AppYamlManager get() {
        if (self == null) {
            self = new AppYamlManager();
        }
        return self;
    }

    private void loadYamlFile() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File("").getAbsolutePath().concat("/src/main/resources/application.yaml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Yaml yaml = new Yaml();
        Map<String, Map<String, Map<String, String>>> data = yaml.load(inputStream);
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();

        decryptor.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"));
        loadPostgresDataSource(decryptor, data);
        loadAppSettings(decryptor, data);
    }

    private void loadPostgresDataSource(StandardPBEStringEncryptor decryptor, Map<String, Map<String, Map<String, String>>> data) {
        postgresDataSource.put("app.datasource.jdbc-url", getDecrypted(decryptor, data, "datasource", "jdbc-url"));
        postgresDataSource.put("app.datasource.username", getDecrypted(decryptor, data, "datasource", "username"));
        postgresDataSource.put("app.datasource.password", getDecrypted(decryptor, data, "datasource", "password"));
    }

    private static String getDecrypted(StandardPBEStringEncryptor decryptor,
                                       Map<String, Map<String, Map<String, String>>> data,
                                       String first,
                                       String second) {
        return decryptor.decrypt(data.get("app").get(first).get(second).
                replace("ENC(", "").replace(")", ""));
    }

    private void loadAppSettings(StandardPBEStringEncryptor decryptor, Map<String, Map<String, Map<String, String>>> data) {
        testingUrl = getDecrypted(decryptor, data, "testing", "base-url");
    }

    public static Map<String, String> getPostgresDataSource() {
        return get().postgresDataSource;
    }

    public static String getTestingUrl() {
        return get().testingUrl;
    }
}
