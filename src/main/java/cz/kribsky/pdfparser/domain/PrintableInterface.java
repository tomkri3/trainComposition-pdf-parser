package cz.kribsky.pdfparser.domain;

import java.util.List;

public interface PrintableInterface {
    List<String> getRowData();

    String[] getHeader();
}
