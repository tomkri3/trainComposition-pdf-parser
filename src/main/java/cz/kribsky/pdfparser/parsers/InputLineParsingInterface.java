package cz.kribsky.pdfparser.parsers;

import cz.kribsky.pdfparser.domain.PrintableInterface;

import java.util.List;

public interface InputLineParsingInterface<T extends PrintableInterface> {

    boolean shouldConsumeLine(GroupBuilder.InputLine s);

    List<T> parse(List<GroupBuilder.InputLine> inputLines);

    interface SingleObject<T extends PrintableInterface> extends InputLineParsingInterface<T> {
        T parseToOne(List<GroupBuilder.InputLine> s);

        @Override
        default List<T> parse(List<GroupBuilder.InputLine> inputLines){
            throw new UnsupportedOperationException();
        }
    }
}
