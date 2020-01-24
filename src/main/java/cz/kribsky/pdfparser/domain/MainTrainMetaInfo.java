package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

/**
 * Main train Info, its only one times there and on the top
 */
@Data
public class MainTrainMetaInfo implements PrintableInterface {
    private String fullName;
    private String journeyFrom;
    private String journeyTo;


    @Override
    public List<String> getRowData() {
        return List.of(fullName, journeyFrom, journeyTo);
    }

    @Override
    public String[] getHeader() {
        return new String[]{"číslo vlaku", "Stanice výchozí", "Stanice cílová"};
    }
}
