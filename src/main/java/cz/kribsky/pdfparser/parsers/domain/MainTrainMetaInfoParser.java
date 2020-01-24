package cz.kribsky.pdfparser.parsers.domain;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.MainTrainMetaInfo;
import cz.kribsky.pdfparser.parsers.GroupBuilder;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTrainMetaInfoParser implements InputLineParsingInterface.SingleObject<MainTrainMetaInfo> {

    private static final String TRAIN_STRING = "VLAK";
    private static final Pattern TRAIN_NUMBER_PATTERN = Pattern.compile("(\\d+)\\s([\\d\\w-]+/){5}(\\d+)");
    private static final String START_STATION = "Stanice výchozí: ";
    private static final String END_STATION = "Stanice cílová: ";

    @Override
    public boolean shouldConsumeLine(GroupBuilder.InputLine line) {
        final String s = line.getLine();
        return s.startsWith(START_STATION) ||
                s.startsWith(END_STATION) ||
                TRAIN_NUMBER_PATTERN.matcher(s).find();
    }

    @Override
    public MainTrainMetaInfo parseToOne(List<GroupBuilder.InputLine> inputLines) {
        final MainTrainMetaInfo mainTrainMetaInfo = new MainTrainMetaInfo();
        for (GroupBuilder.InputLine inputLine : inputLines) {
            final String s = inputLine.getLine();
            final Matcher matcher = TRAIN_NUMBER_PATTERN.matcher(s);

            if (matcher.find()) {
                Preconditions.checkArgument(mainTrainMetaInfo.getFullName() == null);
                mainTrainMetaInfo.setFullName(matcher.group());
            }
            if (s.startsWith(START_STATION)) {
                mainTrainMetaInfo.setJourneyFrom(s.replace(START_STATION, ""));
            }
            if (s.startsWith(END_STATION)) {
                mainTrainMetaInfo.setJourneyTo(s.replace(END_STATION, ""));
            }
        }

        return mainTrainMetaInfo;
    }
}
