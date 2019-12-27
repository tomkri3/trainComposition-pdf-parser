package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

@Data
public class TrainCompost {
    private final Train train;
    private final TrainMetaInfo trainMetaInfo;
    private final List<Wagon> wagons;
    private final List<Engine> engines;
}
