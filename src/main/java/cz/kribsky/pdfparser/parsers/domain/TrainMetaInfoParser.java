package cz.kribsky.pdfparser.parsers.domain;

import com.google.common.collect.MoreCollectors;
import cz.kribsky.pdfparser.domain.TrainMetaInfo;
import cz.kribsky.pdfparser.parsers.GroupBuilder;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 27 = "HmotnVl DélkaVl PočVz PočNapr HmotnostZas MaxRychl Brzd TypVl MimZ NebV Živé"
 * 28 = "763 562 26 104 673 100 P Nákladní 0 0 0"
 */
public class TrainMetaInfoParser implements InputLineParsingInterface.SingleObject<TrainMetaInfo> {
    private static final Pattern SHOULD_CONSUME_PATTERN = Pattern.compile("([\\da-žA-Ž]+\\s){10}");
    public static final Pattern SPLITTING_PATTERN = Pattern.compile("[\\da-žA-Ž]+\\s?");
    public static final String HEADER = "HmotnVl DélkaVl PočVz PočNapr HmotnostZas MaxRychl Brzd TypVl MimZ NebV Živé";

    @Override
    public boolean shouldConsumeLine(GroupBuilder.InputLine line) {
        final String s = line.getLine();
        return SHOULD_CONSUME_PATTERN.matcher(s).find() && !s.equals(HEADER);
    }

    private TrainMetaInfo parse(String s) {
        final Matcher matcher = SPLITTING_PATTERN.matcher(s);
        matcher.find();

        final TrainMetaInfo metaInfo = new TrainMetaInfo();

        metaInfo.setWeight(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setLength(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setWagonsTotaly(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setNumberOfAxles(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setHmotnostZas(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setMaxSpeed(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setBreake(matcher.group().trim());
        matcher.find();

        metaInfo.setTypeVI(matcher.group().trim());
        matcher.find();

        metaInfo.setMimZ(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setNebV(Integer.parseInt(matcher.group().trim()));
        matcher.find();

        metaInfo.setAlive(matcher.group().trim());

        return metaInfo;
    }

    @Override
    public TrainMetaInfo parseToOne(List<GroupBuilder.InputLine> lines) {
        return lines.stream()
                .map(GroupBuilder.InputLine::getLine)
                .map(this::parse)
                .collect(MoreCollectors.onlyElement());
    }
}
