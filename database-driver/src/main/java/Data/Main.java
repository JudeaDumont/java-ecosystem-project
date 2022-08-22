package Data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//todo: add a hibernate + postgresql implementation, deprecate dao nonsense
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Hello, World!");
        System.out.println("Hello, World!");
    }
}