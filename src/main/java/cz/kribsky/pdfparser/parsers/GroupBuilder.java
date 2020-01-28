package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import com.google.common.collect.MoreCollectors;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<Group> groupLinesByHeader(Collection<InputLineParsingInterface<?>> parsingInterfaces) {
        List<Group> groups = new ArrayList<>();

        for (InputLine inputLine : convert) {
            final Optional<InputLineParsingInterface<?>> canConsumeHeader = parsingInterfaces.stream()
                    .filter(inputLineParsingInterface -> inputLineParsingInterface.shouldConsumeLine(inputLine))
                    .collect(MoreCollectors.toOptional());

            if (canConsumeHeader.isPresent() && !getLastGroup(groups).isSameParserClass(canConsumeHeader.get().getClass())) {
                groups.add(new Group(canConsumeHeader.get()));
            }

            if (!groups.isEmpty()) {
                getLastGroup(groups)
                        .getInputLines()
                        .add(inputLine);
            }
        }

        return groups;
    }

    private Group getLastGroup(List<Group> groups) {
        if(groups.isEmpty()){
            return new Group(new InputLineParsingInterface<PrintableInterface>() {

                @Override
                public boolean shouldConsumeLine(InputLine s) {
                    return false;
                }

                @Override
                public List<PrintableInterface> parse(List<InputLine> inputLines) {
                    return Collections.emptyList();
                }
            });
        }
        return groups.get(groups.size() - 1);
    }

    /**
     * Group is class which represents single un-interupted lines which all can be pontetionaly consumed by
     * stored parser
     */
    @ToString
    @EqualsAndHashCode
    public static class Group {
        private InputLineParsingInterface<?> parser;
        private List<InputLine> inputLines = new ArrayList<>();

        public Group(InputLineParsingInterface<?> parser) {
            this.parser = parser;
        }

        boolean isSameParserClass(@NonNull Class<?> aClass) {
            return parser.getClass() == aClass;
        }

        boolean isSameParserClass(@NonNull Group otherGroup) {
            return parser.getClass() == otherGroup.parser.getClass();
        }

        <T extends InputLineParsingInterface<?>> T getTypedParser(@NonNull Class<T> aClass) {
            return (T) parser;
        }

        List<InputLine> getInputLines() {
            return inputLines;
        }

        String debugOutput() {
            return "Group{ name=" + parser.getClass().getName() + ", lines= "+ inputLines.stream().map(InputLine::getLineNumber).map(Object::toString).collect(Collectors.joining(", ")) + "}";
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
                    "Trying to consume, already consumed line! lineNumber %s\nline text '%s'",
                    lineNumber, line
            );
            isConsumed = true;
        }

    }


}
