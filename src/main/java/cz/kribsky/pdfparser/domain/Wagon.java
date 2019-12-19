package cz.kribsky.pdfparser.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

public class Wagon {
    int order;
    String wagonNumber;
    // pocet naprav
    int numberOfAxles;
    int lengthOfWagon;
    int weight;

    // TODO translate meaning
    int zasHmot;
    int breake;
    int weightBreaking;
    int maxSpeed;
}
