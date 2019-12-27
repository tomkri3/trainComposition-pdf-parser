package cz.kribsky.pdfparser.parsers;

import cz.kribsky.pdfparser.CommonTests;
import cz.kribsky.pdfparser.domain.Wagon;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

class WagonParserTest {

    @Test
    void shouldParseCorrectlyWagons() throws IOException, TikaException, SAXException {
        final Path pathToFile = CommonTests.getPdfPath();
        final Collection<Wagon> parse = new ParserComposition().parseCompost(pathToFile).getWagons();

        org.assertj.core.api.Assertions.assertThat(parse)
                .hasSize(25);

    }

}