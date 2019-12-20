package cz.kribsky.pdfparser;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.Wagon;
import cz.kribsky.pdfparser.parsers.PdfParser;
import cz.kribsky.pdfparser.printers.ExcelWriter;
import cz.kribsky.pdfparser.printers.PrinterInterface;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static final String OUTPUT_SUFFIX = "_converted.xslx";

    public static void main(String[] args) throws Exception {
        Preconditions.checkArgument(args.length == 1, "Expecting exactly one argument but got %s", args.length);
        final Path fileToRead = Paths.get(args[0]);
        final List<Wagon> parse = new PdfParser().parse(fileToRead);

        final Path pathToWrite = Paths.get(args[0] + OUTPUT_SUFFIX);
        if (Files.exists(pathToWrite)) {
            Files.delete(pathToWrite);
        }
        final PrinterInterface csvPrinter = new ExcelWriter();
        csvPrinter.printToFile(parse, pathToWrite.toFile());
        System.out.println("Everything finished, new file is: " + pathToWrite.toAbsolutePath());
    }
}
