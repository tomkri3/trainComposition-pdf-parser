package cz.kribsky.pdfparser.parsers;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ParsingInterface<T> {
    boolean shouldConsumeLine(String s);
    boolean isHeader(String s);
    T parse(String s);
}
