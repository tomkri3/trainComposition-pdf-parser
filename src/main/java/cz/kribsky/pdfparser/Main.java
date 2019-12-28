package cz.kribsky.pdfparser;

import com.google.common.base.Preconditions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static final String OUTPUT_SUFFIX = "_converted.xlsx";

    public static void main(String[] args) throws Exception {
        Preconditions.checkArgument(args.length == 1, "Expecting exactly one argument but got %s arguments are: %s", args.length, Arrays.toString(args));
        final Path givenPath = Paths.get(args[0]);
        final Runner runner = new Runner();
        if (Files.isRegularFile(givenPath)) {
            System.out.println("Converting file: " + givenPath);
            runner.convertFile(givenPath);
            System.exit(0);
        } else if (Files.isDirectory(givenPath)) {
            System.out.println("Converting directory: " + givenPath);
            runner.convertDirectory(givenPath);
            System.exit(0);
        }

        throw new IllegalArgumentException("You need to enter directory or file which is readable and exists!");
    }
}
