package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

/**
 * This is single Wagon in part Údaje o vozech
 */
@Data
public class Wagon implements PrintableInterface {
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

    @Override
    public List<String> getRowData() {
        return List.of(
                String.valueOf(order),
                String.valueOf(wagonNumber),
                String.valueOf(numberOfAxles),
                String.valueOf(lengthOfWagon),
                String.valueOf(weight),
                String.valueOf(zasHmot),
                String.valueOf(breake),
                String.valueOf(weightBreaking),
                String.valueOf(maxSpeed)
        );
    }

    @Override
    public String[] getHeader() {
        return new String[]{
                "Poř",
                "Označení vozu",
                "PočNapr",
                "VůzDel",
                "VůzHmotn",
                "ZasHmot",
                "Brzd",
                "BrzdHmotn",
                "MaxRychl"
        };
    }
}
