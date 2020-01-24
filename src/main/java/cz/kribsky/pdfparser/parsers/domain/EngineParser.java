package cz.kribsky.pdfparser.parsers.domain;

import cz.kribsky.pdfparser.domain.Engine;
import cz.kribsky.pdfparser.parsers.GroupBuilder;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 37 = "Označení HV        Funkce      Vložené za vozem    Strojvedoucí"
 * 38 = "91 80 6193 750-7   Vlakové     1.                  1"
 */
public class EngineParser implements InputLineParsingInterface<Engine> {

    private static final Pattern ENGINE_PATTERN = Pattern.compile("^((\\d{2}\\s){2}(\\d{4})\\s(\\d{3})-\\d+)");

    @Override
    public boolean shouldConsumeLine(GroupBuilder.InputLine s) {
        return ENGINE_PATTERN.matcher(s.getLine()).find();
    }

    @Override
    public List<Engine> parse(List<GroupBuilder.InputLine> inputLines) {
        return inputLines.stream()
                .map(this::parse)
                .collect(Collectors.toUnmodifiableList());
    }

    private Engine parse(GroupBuilder.InputLine line) {
        final Engine engine = new Engine();
        final Matcher matcher = ENGINE_PATTERN.matcher(line.getLine());
        matcher.find();

        engine.setDesignationNumber(matcher.group(0));

        return engine;
    }

}
