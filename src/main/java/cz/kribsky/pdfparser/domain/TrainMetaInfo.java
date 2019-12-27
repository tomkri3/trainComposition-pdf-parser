package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

@Data
public class TrainMetaInfo implements PrintableInterface {
    private Integer weight;
    private Integer length;
    private Integer wagonsTotaly;
    private Integer numberOfAxles;
    private Integer hmotnostZas;
    private Integer maxSpeed;
    private String breake;
    private String typeVI;
    private Integer mimZ;
    private Integer nebV;
    private String alive;


    @Override
    public List<String> getPrintableValues() {
        return List.of(
                weight.toString(),
                length.toString(),
                wagonsTotaly.toString(),
                numberOfAxles.toString(),
                hmotnostZas.toString(),
                maxSpeed.toString(),
                breake,
                typeVI,
                mimZ.toString(),
                nebV.toString(),
                alive
        );
    }

    @Override
    public String[] getHeader() {
        return new String[]{
                "HmotnVl",
                "DélkaVl",
                "PočVz",
                "PočNapr",
                "HmotnostZas",
                "MaxRychl",
                "Brzd",
                "TypVl",
                "MimZ",
                "NebV",
                "Živé"
        };
    }
}
