package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupBuilder {
    private final List<InputLine> convert;

    public GroupBuilder(String[] lines) {
        convert = convert(lines);
    }

    private List<InputLine> convert(String[] lines) {
        List<InputLine> inputLines = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            inputLines.add(new InputLine(line, false, i));
        }
        return List.copyOf(inputLines);
    }

    public List<Group> groupLinesByHeader(Collection<InputLineParsingInterface> parsingInterfaces) {
        List<Group> groups = new ArrayList<>();

        for (InputLine inputLine : convert) {
            for (InputLineParsingInterface parsingInterface : parsingInterfaces) {
                if (parsingInterface.isHeader(inputLine.getLine())) {
                    inputLine.consume();
                    groups.add(new Group(parsingInterface));
                }
                if (!groups.isEmpty()) {
                    groups.get(groups.size()).getInputLines().add(inputLine);
                }
            }
        }

        return groups;
    }

    /**
     * Group is class which represents single un-interupted lines which all can be pontetionaly consumed by
     * stored parser
     */
    @ToString
    @EqualsAndHashCode
    public static class Group {
        private InputLineParsingInterface parser;
        private List<InputLine> inputLines = new ArrayList<>();

        public Group(InputLineParsingInterface parser) {
            this.parser = parser;
        }

        boolean isSameParserClass(@NotNull Class aClass){
            return parser.getClass() == aClass;
        }

        <T extends InputLineParsingInterface> T getTypedParse(@NotNull Class<T> aClass){
            return (T) parser;
        }

        public List<InputLine> getInputLines() {
            return inputLines;
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class InputLine {
        String line;
        boolean isConsumed;
        int lineNumber;

        public void consume() {
            Preconditions.checkArgument(!isConsumed,
                    "Trying to consume, already consumed line! lineNumber %s\nline %s",
                    line
            );
            isConsumed = true;
        }

    }


}
