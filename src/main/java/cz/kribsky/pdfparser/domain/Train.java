package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

@Data
public class Train implements PrintableInterface {
    private String fullName;
    private String journeyFrom;
    private String journeyTo;


    @Override
    public List<String> getPrintableValues() {
        return List.of(fullName, journeyFrom, journeyTo);
    }

    @Override
    public String[] getHeader() {
        return new String[]{"číslo vlaku", "Stanice výchozí", "Stanice cílová"};
    }
}
