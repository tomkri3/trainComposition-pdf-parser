package cz.kribsky.pdfparser.parsers;

import com.google.common.collect.Iterables;
import cz.kribsky.pdfparser.domain.PrintableInterface;

import java.util.List;

public interface InputLineParsingInterface<T extends PrintableInterface> {

    boolean isHeader(GroupBuilder.InputLine s);

    boolean shouldConsumeLine(GroupBuilder.InputLine s);

    List<T> parse(List<GroupBuilder.InputLine> inputLines);

    default T parseToOnly(List<GroupBuilder.InputLine> inputLines) {
        return Iterables.getOnlyElement(parse(inputLines));
    }

    interface SingleLine<T extends PrintableInterface> extends InputLineParsingInterface<T> {
        T parse(GroupBuilder.InputLine s);
    }
}
