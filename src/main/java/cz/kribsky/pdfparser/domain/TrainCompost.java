package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

@Data
public class TrainCompost {
    private final Train train;
    private final List<TrainMetaInfo> trainMetaInfo;
    private final List<Wagon> wagons;
    private final List<Engine> engines;
}
