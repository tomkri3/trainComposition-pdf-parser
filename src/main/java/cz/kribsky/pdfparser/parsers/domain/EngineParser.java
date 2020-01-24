package cz.kribsky.pdfparser.parsers.domain;

import cz.kribsky.pdfparser.domain.Engine;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 37 = "Označení HV        Funkce      Vložené za vozem    Strojvedoucí"
 * 38 = "91 80 6193 750-7   Vlakové     1.                  1"
 */
public class EngineParser implements InputLineParsingInterface<Engine> {

    private static final Pattern ENGINE_PATTERN = Pattern.compile("^((\\d{2}\\s){2}(\\d{4})\\s(\\d{3})-\\d+)");

    @Override
    public boolean shouldConsumeLine(String s) {
        return ENGINE_PATTERN.matcher(s).find();
    }

    @Override
    public boolean isHeader(String s) {
        return s.equals("Označení HV Funkce Vložené za vozem Strojvedoucí");
    }

    @Override
    public Engine parse(String s) {
        final Engine engine = new Engine();
        final Matcher matcher = ENGINE_PATTERN.matcher(s);
        matcher.find();

        engine.setDesignationNumber(matcher.group(0));

        return engine;
    }
}
