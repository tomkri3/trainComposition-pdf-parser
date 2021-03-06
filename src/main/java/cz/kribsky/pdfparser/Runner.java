package cz.kribsky.pdfparser;

import cz.kribsky.pdfparser.domain.PrintableInterface;
import cz.kribsky.pdfparser.parsers.ParseMonitor;
import cz.kribsky.pdfparser.parsers.ParserComposition;
import cz.kribsky.pdfparser.printers.ExcelPrinter;
import cz.kribsky.pdfparser.printers.PrinterInterface;
import cz.kribsky.pdfparser.printers.TrainCompostPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static cz.kribsky.pdfparser.Main.OUTPUT_SUFFIX;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public void convertFile(Path fileToRead) throws Exception {
        ParserComposition parserComposition = new ParserComposition(false);
        final List<PrintableInterface> dataRows = new TrainCompostPrinter(parserComposition.parseCompost(fileToRead)).getDataRows();
        final Path pathToWrite = preparePathToWrite(fileToRead);
        try (PrinterInterface printer = new ExcelPrinter()) {
            printer.printToFileAndFinish(dataRows, pathToWrite.toFile());
        }

        finish(parserComposition, pathToWrite.toAbsolutePath());
    }

    private void finish(ParserComposition parserComposition, Path pathToWrite) {
        final ParseMonitor parseMonitor = parserComposition.getParseMonitor();
        for (Map.Entry<Exception, String> entry : parseMonitor.getExceptionStringMap().entrySet()) {
            LOGGER.warn("Error comment : {}", entry.getValue());
            LOGGER.warn("Stack trace", entry.getKey());
        }

        if (parseMonitor.hasRisenException()) {
            LOGGER.warn("-------------------------");
            parseMonitor.getExceptionStringMap().values().forEach(LOGGER::warn);
            LOGGER.warn("Everything finished with total ERRORs: {}, new file is: {}", parseMonitor.getExceptionStringMap().size(), pathToWrite.toAbsolutePath());
        } else {
            LOGGER.info("Everything finished OK, new file is: {}", pathToWrite.toAbsolutePath());
        }

    }

    private Path preparePathToWrite(Path fileToRead) throws IOException {
        final Path pathToWrite = Paths.get(fileToRead.toAbsolutePath().toString() + OUTPUT_SUFFIX);
        if (Files.exists(pathToWrite)) {
            Files.delete(pathToWrite);
        }
        return pathToWrite;
    }

    public void convertDirectory(Path givenPath) throws IOException {
        final ParserComposition parserComposition = new ParserComposition(false);
        final Path pathToWrite = preparePathToWrite(Paths.get(givenPath.toString(), "consolidation"));

        try (Stream<Path> walk = Files.walk(givenPath)) {
            try (ExcelPrinter excelPrinter = new ExcelPrinter()) {
                excelPrinter.init(pathToWrite.toFile(), Collections.emptyList());
                walk.filter(path -> path.toString().endsWith(".pdf"))
                        .map(parserComposition::parseCompost)
                        .map(TrainCompostPrinter::new)
                        .forEach(trainCompostPrinter -> {
                            excelPrinter.printHeader(trainCompostPrinter.getDataRows());
                            excelPrinter.printCollection(trainCompostPrinter.getDataRows());
                        });
                excelPrinter.writeAndFinish(pathToWrite.toFile());
            }
        }

        finish(parserComposition, givenPath);
    }
}
