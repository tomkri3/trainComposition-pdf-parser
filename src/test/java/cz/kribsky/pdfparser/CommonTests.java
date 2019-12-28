package cz.kribsky.pdfparser;

import org.junit.jupiter.api.Assertions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonTests {

    public static Path getPdfPath35() {
        final Path pathToFile = Paths.get("src", "test", "resources", "SlozeniVlaku(35).pdf");
        Assertions.assertTrue(Files.exists(pathToFile));
        return pathToFile;
    }

    public static Path getPdfPath74() {
        final Path pathToFile = Paths.get("src", "test", "resources", "SlozeniVlaku (74).pdf");
        Assertions.assertTrue(Files.exists(pathToFile));
        Assertions.assertTrue(Files.isReadable(pathToFile));
        return pathToFile;
    }
}
