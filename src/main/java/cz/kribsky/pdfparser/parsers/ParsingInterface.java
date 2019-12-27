package cz.kribsky.pdfparser.parsers;

import cz.kribsky.pdfparser.domain.PrintableInterface;

public interface ParsingInterface<T extends PrintableInterface> {
    boolean shouldConsumeLine(String s);

    boolean isHeader(String s);

    T parse(String s);
}
