package cz.kribsky.pdfparser.printers;

import cz.kribsky.pdfparser.domain.PrintableInterface;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PrinterInterface extends AutoCloseable {

    default void printToFileAndFinish(List<? extends PrintableInterface> wagons, File file) throws IOException {
        init(file, wagons);
        printHeader(wagons);
        printCollection(wagons);
        writeAndFinish(file);
    }

    void init(File file, List<? extends PrintableInterface> printableInterfaces);

    void printCollection(List<? extends PrintableInterface> printableInterfaces);

    void writeAndFinish(File file) throws IOException;

    void printHeader(List<? extends PrintableInterface> printableInterfaces);
}
