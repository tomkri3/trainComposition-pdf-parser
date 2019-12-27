package cz.kribsky.pdfparser;

import cz.kribsky.pdfparser.domain.PrintableInterface;
import cz.kribsky.pdfparser.parsers.ParserComposition;
import cz.kribsky.pdfparser.printers.ExcelPrinter;
import cz.kribsky.pdfparser.printers.PrinterInterface;
import cz.kribsky.pdfparser.printers.TrainCompostPrintable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static cz.kribsky.pdfparser.Main.OUTPUT_SUFFIX;

public class Runner {

    public void convertFile(Path fileToRead) throws Exception {
        final List<PrintableInterface> dataRows = new TrainCompostPrintable(new ParserComposition().parseCompost(fileToRead)).getDataRows();
        final Path pathToWrite = preparePathToWrite(fileToRead);
        try (PrinterInterface printer = new ExcelPrinter()) {
            printer.printToFileAndFinish(dataRows, pathToWrite.toFile());
        }
        System.out.println("Everything finished, new file is: " + pathToWrite.toAbsolutePath());
    }

    private Path preparePathToWrite(Path fileToRead) throws IOException {
        final Path pathToWrite = Paths.get(fileToRead.toAbsolutePath().toString() + OUTPUT_SUFFIX);
        if (Files.exists(pathToWrite)) {
            Files.delete(pathToWrite);
        }
        return pathToWrite;
    }

    public void convertDirectory(Path givenPath) throws Exception {
        final ParserComposition parserComposition = new ParserComposition();
        final Path pathToWrite = preparePathToWrite(Paths.get(givenPath.toString(), "consolidation" + OUTPUT_SUFFIX));

        try (Stream<Path> walk = Files.walk(givenPath)) {
            try (ExcelPrinter excelPrinter = new ExcelPrinter()) {
                excelPrinter.init(pathToWrite.toFile(), Collections.emptyList());
                walk.filter(path -> path.toString().endsWith(".pdf"))
                        .peek(path -> System.out.println("Processing file: " + path.toAbsolutePath()))
                        .map(parserComposition::parseCompost)
                        .map(TrainCompostPrintable::new)
                        .forEach(trainCompostPrintable -> {
                            excelPrinter.printHeader(trainCompostPrintable.getDataRows());
                            excelPrinter.printCollection(trainCompostPrintable.getDataRows());
                        });
                excelPrinter.writeAndFinish(pathToWrite.toFile());
            }
        }
        System.out.println("Everything finished, new file is: " + pathToWrite.toAbsolutePath());
    }
}
