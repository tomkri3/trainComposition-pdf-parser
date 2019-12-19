package cz.kribsky.pdfparser;


import com.google.common.io.Resources;
import cz.kribsky.pdfparser.domain.Wagon;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

class PdfParserTest {

    @Test
    void shouldFindStructureInPdf() throws IOException, TikaException, SAXException {
        final String path = Resources.getResource("SlozeniVlaku(35).pdf").getPath();
        Assertions.assertTrue(Files.exists(Paths.get(path)));

        final Collection<Wagon> parse = new PdfParser().parse(Paths.get(path));

        org.assertj.core.api.Assertions.assertThat(parse)
                .hasSize(25);

    }
}