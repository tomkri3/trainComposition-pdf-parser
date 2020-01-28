package cz.kribsky.pdfparser.parsers.domain;

import cz.kribsky.pdfparser.domain.TrainPath;
import cz.kribsky.pdfparser.parsers.GroupBuilder;
import cz.kribsky.pdfparser.parsers.InputLineParsingInterface;

import java.util.List;

public class TrainPathParser implements InputLineParsingInterface.SingleObject<TrainPath> {

    private static final String FROM_STATION = "Ze stanice: ";
    private static final String TO_STATION = "Do stanice: ";

    @Override
    public boolean shouldConsumeLine(GroupBuilder.InputLine inputLine) {
        final String s = inputLine.getLine();
        return s.startsWith(FROM_STATION) || s.startsWith(TO_STATION);
    }

    @Override
    public TrainPath parseToOne(List<GroupBuilder.InputLine> lines) {
        final TrainPath.TrainPathBuilder builder = TrainPath.builder();
        for (GroupBuilder.InputLine line : lines) {
            if(line.getLine().startsWith(FROM_STATION)){
                builder.fromStation(line.getLine().replace(FROM_STATION, ""));
            }
            if(line.getLine().startsWith(TO_STATION)){
                builder.toStation(line.getLine().replace(TO_STATION, ""));
            }
        }

        return builder.build();
    }
}
