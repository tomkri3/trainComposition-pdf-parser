package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.Train;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrainParser implements ParsingInterface<Train> {

    private static final String TRAIN_STRING = "VLAK";
    private static final Pattern TRAIN_NUMBER_PATTERN = Pattern.compile("(\\d+)\\s([\\d\\w-]+/){5}(\\d+)");
    private static final String START_STATION = "Stanice výchozí: ";
    private static final String END_STATION = "Stanice cílová: ";

    @Override
    public boolean shouldConsumeLine(String s) {
        return s.startsWith(START_STATION) ||
                s.startsWith(END_STATION) ||
                TRAIN_NUMBER_PATTERN.matcher(s).find();
    }

    @Override
    public boolean isHeader(String s) {
        return s.equals(TRAIN_STRING);
    }

    @Override
    public Train parse(String s) {
        final Train train = new Train();
        final Matcher matcher = TRAIN_NUMBER_PATTERN.matcher(s);

        if (matcher.find()) {
            train.setFullName(matcher.group());
        }
        if (s.startsWith(START_STATION)) {
            train.setJourneyFrom(s.replace(START_STATION, ""));
        }
        if (s.startsWith(END_STATION)) {
            train.setJourneyTo(s.replace(END_STATION, ""));
        }

        return train;
    }

    public static Train combine(Collection<Train> trains) {
        final Train train = new Train();

        for (Train trainIterable : trains) {
            fillTrain(train, trainIterable, Train::getFullName, Train::setFullName);
            fillTrain(train, trainIterable, Train::getJourneyFrom, Train::setJourneyFrom);
            fillTrain(train, trainIterable, Train::getJourneyTo, Train::setJourneyTo);
        }

        return train;
    }

    private static void fillTrain(Train fillingTrain, Train trainIterable, Function<Train, String> getter, BiConsumer<Train, String> setter) {
        final String get = getter.apply(trainIterable);
        if (get != null) {
            Preconditions.checkArgument(getter.apply(fillingTrain) == null, "Expecting to be null before filling for object %s but trying to save %s", fillingTrain, get);
            setter.accept(fillingTrain, get);
        }
    }
}
