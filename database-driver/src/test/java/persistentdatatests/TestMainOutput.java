package persistentdatatests;

import data.Main;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestMainOutput {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private final PrintStream previousOut = System.out;
    private final PrintStream previousErr = System.err;

    @Test
    public void checkMainOutput() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        String[] arguments = {"a", "b"};
        Main.main(arguments);
        String expected = "Hello, World!";

        assertEquals(expected, outContent.toString().trim());
        System.setOut(previousOut);
        System.setErr(previousErr);
    }
}
