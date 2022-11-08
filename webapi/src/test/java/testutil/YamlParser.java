package testutil;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlParser {

    public static Map<String, String> getPostgreDataSource() {
        Map<String, String> meaningfulData = new HashMap<>();
        try {
            InputStream inputStream = new FileInputStream(new File("").getAbsolutePath().concat("/src/main/resources/application.yaml"));
            Yaml yaml = new Yaml();

            Map<String, Map<String, Map<String, String>>> data = null;
            data = yaml.load(inputStream);

            StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();

            decryptor.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"));

            meaningfulData.put("app.datasource.jdbc-url", decryptor.decrypt(data.get("app").get("datasource").get("jdbc-url").
                    replace("ENC(", "").replace(")", "")));

            meaningfulData.put("app.datasource.username", decryptor.decrypt(data.get("app").get("datasource").get("username").
                    replace("ENC(", "").replace(")", "")));

            meaningfulData.put("app.datasource.password", decryptor.decrypt(data.get("app").get("datasource").get("password").
                    replace("ENC(", "").replace(")", "")));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return meaningfulData;
    }
}
