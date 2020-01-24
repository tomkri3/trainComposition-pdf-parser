package cz.kribsky.pdfparser.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * This class represents part in pdf which starts "Údaje o hnacích vozidlech"
 */
@Data
public class Engine implements PrintableInterface {
    private String designationNumber;

    @Override
    public List<String> getRowData() {
        return List.of(designationNumber);
    }

    @Override
    public String[] getHeader() {
        return new String[]{"Lokomotivy"};
    }
}
