package cz.kribsky.pdfparser.parsers.domain;

import cz.kribsky.pdfparser.domain.Wagon;
import cz.kribsky.pdfparser.parsers.GroupBuilder;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WagonParser implements InputLineParsingInterface<Wagon> {

    /**
     * its POSSIBLE that are missing some zeroes so its not final regex
     * Poř Označení vozu PočNapr VůzDel VůzHmotn ZasHmot Brzd BrzdHmotn MaxRychl
     * 1 33 52 5412 025-6 4 2170 26500 0 100
     */
    private static final Pattern PATTERN = Pattern.compile("(?<order>\\d+)\\s" +
            "(?<wagonNumber>(\\d+\\s)+\\d+-\\d+)\\s" +
            "(?<numberOfAxles>\\d+)\\s" +
            "(?<lengthOfWagon>\\d+)\\s" +
            "(?<weight>\\d+)\\s" +
            "(?<zasHmot>\\d+)\\s" +
            "(?<maxSpeed>\\d+)");
    public static final String HEADER = "Poř Označení vozu PočNapr VůzDel VůzHmotn ZasHmot Brzd BrzdHmotn MaxRychl";

    @Override
    public boolean shouldConsumeLine(GroupBuilder.InputLine line) {
        final Matcher matcher = PATTERN.matcher(line.getLine());
        return matcher.find();
    }

    @Override
    public List<Wagon> parse(List<GroupBuilder.InputLine> inputLines) {
        return inputLines.stream()
                .map(GroupBuilder.InputLine::getLine)
                .map(this::parse)
                .collect(Collectors.toUnmodifiableList());
    }

    private Wagon parse(String s) {
        final Matcher matcher = PATTERN.matcher(s);
        matcher.find();


        final Wagon wagon = new Wagon();
        wagon.setOrder(Integer.parseInt(matcher.group("order")));
        wagon.setWagonNumber(matcher.group("wagonNumber"));
        wagon.setNumberOfAxles(Integer.parseInt(matcher.group("numberOfAxles")));
        wagon.setLengthOfWagon(Integer.parseInt(matcher.group("lengthOfWagon")));
        wagon.setWeight(Integer.parseInt(matcher.group("weight")));
        wagon.setZasHmot(Integer.parseInt(matcher.group("zasHmot")));
        wagon.setMaxSpeed(Integer.parseInt(matcher.group("maxSpeed")));

        return wagon;
    }
}
