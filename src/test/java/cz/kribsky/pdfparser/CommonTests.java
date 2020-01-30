package cz.kribsky.pdfparser;

import cz.kribsky.pdfparser.domain.SinglePath;
import cz.kribsky.pdfparser.domain.TrainCompost;
import org.junit.jupiter.api.Assertions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonTests {

    public static Path getPdfPath35() {
        final Path pathToFile = Paths.get("src", "test", "resources", "SlozeniVlaku(35).pdf");
        Assertions.assertTrue(Files.exists(pathToFile));
        return pathToFile;
    }
    public static Path getPdfPath1() {
        final Path pathToFile = Paths.get("src", "test", "resources", "Slození vlaku1.pdf");
        Assertions.assertTrue(Files.exists(pathToFile));
        return pathToFile;
    }

    public static Path getPdfPath74() {
        final Path pathToFile = Paths.get("src", "test", "resources", "SlozeniVlaku (74).pdf");
        Assertions.assertTrue(Files.exists(pathToFile));
        Assertions.assertTrue(Files.isReadable(pathToFile));
        return pathToFile;
    }

    public static <O> List<O> getAllCollections(TrainCompost trainCompost, Function<SinglePath, List<O>> getFc) {
        return trainCompost
                .getSinglePaths()
                .stream()
                .map(getFc)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
    }
}
