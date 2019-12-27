package cz.kribsky.pdfparser;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonTests {

    public static Path getPdfPath() {
        final String path = Resources.getResource("SlozeniVlaku(35).pdf").getPath();
        final Path pathToFile = Paths.get(path);
        Assertions.assertTrue(Files.exists(pathToFile));
        return pathToFile;
    }
}
