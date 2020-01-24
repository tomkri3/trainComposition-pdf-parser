package cz.kribsky.pdfparser.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SinglePath {
    private final TrainMetaInfo trainMetaInfo;
    private final List<Engine> engines;
    private final List<Wagon> wagons;
    private final TrainPath trainPath;
}
