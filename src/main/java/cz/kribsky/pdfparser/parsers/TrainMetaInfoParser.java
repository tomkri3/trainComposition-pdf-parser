package cz.kribsky.pdfparser.parsers;

import cz.kribsky.pdfparser.domain.TrainMetaInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 27 = "HmotnVl DélkaVl PočVz PočNapr HmotnostZas MaxRychl Brzd TypVl MimZ NebV Živé"
 * 28 = "763 562 26 104 673 100 P Nákladní 0 0 0"
 */
public class TrainMetaInfoParser implements ParsingInterface<TrainMetaInfo> {
    private static final Pattern SHOULD_CONSUME_PATTERN = Pattern.compile("([\\da-žA-Ž]+\\s){10}");
    public static final Pattern SPLITTING_PATTERN = Pattern.compile("[\\da-žA-Ž]+\\s?");

    @Override
    public boolean shouldConsumeLine(String s) {
        return SHOULD_CONSUME_PATTERN.matcher(s).find();
    }

    @Override
    public boolean isHeader(String s) {
        return s.equals("HmotnVl DélkaVl PočVz PočNapr HmotnostZas MaxRychl Brzd TypVl MimZ NebV Živé");
    }

    @Override
    public TrainMetaInfo parse(String s) {
        final Matcher matcher = SPLITTING_PATTERN.matcher(s);
        matcher.find();

        final TrainMetaInfo metaInfo = new TrainMetaInfo();
//        matcher.grou
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
}
