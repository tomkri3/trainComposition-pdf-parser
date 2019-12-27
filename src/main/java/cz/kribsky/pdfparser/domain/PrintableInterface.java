package cz.kribsky.pdfparser.domain;

import java.util.List;

public interface PrintableInterface {
    List<String> getPrintableValues();

    String[] getHeader();
}
