package cz.kribsky.pdfparser.printers;

import cz.kribsky.pdfparser.domain.CsvFormat;

import java.io.File;
import java.util.List;

public interface PrinterInterface {
    void printToFile(List<? extends CsvFormat> wagons, File file) throws Exception;
}
