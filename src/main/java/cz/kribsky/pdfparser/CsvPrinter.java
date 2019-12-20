package cz.kribsky.pdfparser;

import com.google.common.collect.Iterables;
import cz.kribsky.pdfparser.domain.CsvFormat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cz.kribsky.pdfparser.parsers.WagonParser.HEADER;

public class CsvPrinter {

    public static final char DELIMITER = ',';

    void printToFile(Collection<? extends CsvFormat> wagons, File file) throws IOException {
        FileWriter out = new FileWriter(file);
        final String[] header = Iterables.getFirst(wagons, null).getHeader();
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(header).withDelimiter(DELIMITER);
        try (CSVPrinter printer = new CSVPrinter(out, format)) {
            for (CsvFormat wagon : wagons) {
                final List<String> csvFormat = wagon.getCsvFormat();
                printer.printRecord(csvFormat);
            }
        }
    }
}
