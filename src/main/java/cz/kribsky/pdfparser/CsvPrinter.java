package cz.kribsky.pdfparser;

import com.google.common.collect.Iterables;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import cz.kribsky.pdfparser.printers.PrinterInterface;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.List;

public class CsvPrinter implements PrinterInterface {

    public static final char DELIMITER = ',';

    @Override
    public void printToFile(List<? extends PrintableInterface> wagons, File file) throws Exception {
        FileWriter out = new FileWriter(file, Charset.forName("WINDOWS-1250"));
        final String[] header = Iterables.getFirst(wagons, null).getHeader();
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(header).withDelimiter(DELIMITER);
        try (CSVPrinter printer = new CSVPrinter(out, format)) {
            for (PrintableInterface wagon : wagons) {
                final List<String> csvFormat = wagon.getPrintableValues();
                printer.printRecord(csvFormat);
            }
        }
    }
}
