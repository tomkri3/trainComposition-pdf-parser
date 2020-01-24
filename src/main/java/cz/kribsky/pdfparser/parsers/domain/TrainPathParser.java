package cz.kribsky.pdfparser.parsers.domain;

import cz.kribsky.pdfparser.domain.TrainPath;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

public class TrainPathParser implements InputLineParsingInterface<TrainPath> {
    @Override
    public boolean shouldConsumeLine(String s) {
        return false;
    }

    @Override
    public boolean isHeader(String s) {
        return "SLOŽENÍ".equals(s);
    }

    @Override
    public TrainPath parse(String s) {
        return null;
    }
}
