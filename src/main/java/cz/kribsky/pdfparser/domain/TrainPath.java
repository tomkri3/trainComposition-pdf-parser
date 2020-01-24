package cz.kribsky.pdfparser.domain;

import lombok.Value;

import java.util.List;

@Value
public class TrainPath implements PrintableInterface {
    String fromStation;
    String toStation;

    @Override
    public List<String> getRowData() {
        return List.of(fromStation, toStation);
    }

    @Override
    public String[] getHeader() {
        return new String[]{"Ze stanice", "Do stanice"};
    }
}
