package cz.kribsky.pdfparser.parsers.domain;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.MainTrainMetaInfo;
import cz.kribsky.pdfparser.domain.TrainMetaInfo;
import cz.kribsky.pdfparser.parsers.GroupBuilder;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainTrainMetaInfoParser implements InputLineParsingInterface<MainTrainMetaInfo> {

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
    public List<MainTrainMetaInfo> parse(List<GroupBuilder.InputLine> inputLines) {
        List<MainTrainMetaInfo> infoList = new ArrayList<>();

        MainTrainMetaInfo mainTrainMetaInfo = new MainTrainMetaInfo();
        for (GroupBuilder.InputLine inputLine : inputLines) {
            final String s = inputLine.getLine();
            final Matcher matcher = TRAIN_NUMBER_PATTERN.matcher(s);

            if (matcher.find()) {
                mainTrainMetaInfo.setFullName(matcher.group());
            }
            if (s.startsWith(START_STATION)) {
                mainTrainMetaInfo.setJourneyFrom(s.replace(START_STATION, ""));
            }
            if (s.startsWith(END_STATION)) {
                mainTrainMetaInfo.setJourneyTo(s.replace(END_STATION, ""));
                infoList.add(mainTrainMetaInfo);
                mainTrainMetaInfo = new MainTrainMetaInfo();
            }
        }

        return infoList;
    }

    @Override
    public boolean isHeader(GroupBuilder.InputLine line) {
        return line.getLine().equals(TRAIN_STRING);
    }

    public static MainTrainMetaInfo combine(Collection<MainTrainMetaInfo> mainTrainMetaInfos) {
        final MainTrainMetaInfo mainTrainMetaInfo = new MainTrainMetaInfo();

        for (MainTrainMetaInfo mainTrainMetaInfoIterable : mainTrainMetaInfos) {
            fillTrain(mainTrainMetaInfo, mainTrainMetaInfoIterable, MainTrainMetaInfo::getFullName, MainTrainMetaInfo::setFullName);
            fillTrain(mainTrainMetaInfo, mainTrainMetaInfoIterable, MainTrainMetaInfo::getJourneyFrom, MainTrainMetaInfo::setJourneyFrom);
            fillTrain(mainTrainMetaInfo, mainTrainMetaInfoIterable, MainTrainMetaInfo::getJourneyTo, MainTrainMetaInfo::setJourneyTo);
        }

        return mainTrainMetaInfo;
    }

    private static void fillTrain(MainTrainMetaInfo fillingMainTrainMetaInfo,
                                  MainTrainMetaInfo mainTrainMetaInfoIterable,
                                  Function<MainTrainMetaInfo, String> getter,
                                  BiConsumer<MainTrainMetaInfo, String> setter) {
        final String get = getter.apply(mainTrainMetaInfoIterable);
        if (get != null) {
            Preconditions.checkArgument(
                    getter.apply(fillingMainTrainMetaInfo) == null,
                    "Expecting to be null before filling for object %s but trying to save %s",
                    fillingMainTrainMetaInfo, get
            );
            setter.accept(fillingMainTrainMetaInfo, get);
        }
    }

}
