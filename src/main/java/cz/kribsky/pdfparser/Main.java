package cz.kribsky.pdfparser;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.Wagon;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class Main {

    public static final String OUTPUT_SUFFIX = "_converted.csv";

    public static void main(String[] args) throws TikaException, IOException, SAXException {
        Preconditions.checkArgument(args.length == 1, "Expecting exactly one argument but got %s", args.length);
        final Path fileToRead = Paths.get(args[0]);
        final Collection<Wagon> parse = new PdfParser().parse(fileToRead);

        final Path pathToWrite = Paths.get(args[0] + OUTPUT_SUFFIX);
        if (Files.exists(pathToWrite)) {
            Files.delete(pathToWrite);
        }
        new CsvPrinter().printToFile(parse, pathToWrite.toFile());
        System.out.println("Everything finished, nwe file is: " + pathToWrite.toAbsolutePath());
    }
}
