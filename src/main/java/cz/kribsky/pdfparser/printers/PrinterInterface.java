package cz.kribsky.pdfparser.printers;

import cz.kribsky.pdfparser.domain.PrintableInterface;

import java.io.File;
import java.util.List;

public interface PrinterInterface {
    void printToFile(List<? extends PrintableInterface> wagons, File file) throws Exception;
}
