package yamlparsertest;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import testutil.YamlParser;

import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestYamlParser {

    @Test
    @Order(1)
    void test_yamlparser(){
        Map<String, String> postgreDataSource = YamlParser.getPostgreDataSource();
    }
}
