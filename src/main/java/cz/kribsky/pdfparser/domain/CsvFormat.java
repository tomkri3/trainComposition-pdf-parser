package cz.kribsky.pdfparser.domain;

import java.util.List;

public interface CsvFormat {
    List<String> getCsvFormat();

    String[] getHeader();
}
