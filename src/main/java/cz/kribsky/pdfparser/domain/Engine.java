package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

@Data
public class Engine implements PrintableInterface {
    private String designationNumber;

    @Override
    public List<String> getPrintableValues() {
        return List.of(designationNumber);
    }

    @Override
    public String[] getHeader() {
        return new String[]{"Lokomotivy"};
    }
}
