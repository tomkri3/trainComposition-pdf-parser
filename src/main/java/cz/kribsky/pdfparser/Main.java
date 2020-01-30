package cz.kribsky.pdfparser;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static final String OUTPUT_SUFFIX = "_converted.xlsx";

    public static void main(String[] args) throws Exception {
        Preconditions.checkArgument(args.length == 1, "Expecting exactly one argument but got %s arguments which i get are: %s", args.length, Arrays.toString(args));
        final Path givenPath = Paths.get(args[0]);
        final Runner runner = new Runner();
        if (Files.isRegularFile(givenPath)) {
            LOGGER.info("Converting file: {}", givenPath);
            runner.convertFile(givenPath);
            System.exit(0);
        } else if (Files.isDirectory(givenPath)) {
            LOGGER.info("Converting directory: {}", givenPath);
            runner.convertDirectory(givenPath);
            System.exit(0);
        }

        throw new IllegalArgumentException("You need to enter directory or file which is readable and exists! " + Arrays.toString(args));
    }
}
