package cz.kribsky.pdfparser.parsers;


import cz.kribsky.pdfparser.CommonTests;
import cz.kribsky.pdfparser.domain.Engine;
import cz.kribsky.pdfparser.domain.MainTrainMetaInfo;
import cz.kribsky.pdfparser.domain.TrainMetaInfo;
import cz.kribsky.pdfparser.domain.Wagon;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParserComposition35Test {

    @Test
    void shouldParseCorrectlyWagons() {
        final Path pathToFile = CommonTests.getPdfPath35();
        final Collection<Wagon> parse = new ParserComposition().parseCompost(pathToFile).getWagons();

        assertThat(parse)
                .hasSize(25);

    }

    @Test
    void shouldParseCorrectlyTrain() {
        final Path pathToFile = CommonTests.getPdfPath35();

        final MainTrainMetaInfo parse = new ParserComposition().parseCompost(pathToFile).getMainTrainMetaInfo();

        final MainTrainMetaInfo expected = new MainTrainMetaInfo();
        expected.setJourneyFrom("CZ - 30145 Lanžhot st.hr.");
        expected.setJourneyTo("CZ - 35737 Přibyslav");
        expected.setFullName("049894 TR/3622/-KADR302215-/00/2019/20191203");

        assertThat(parse)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void shouldParseCorrectlyTrainMetaInfo() {
        final Path pathToFile = CommonTests.getPdfPath35();

        List<TrainMetaInfo> trainMetaInfo = new ParserComposition().parseCompost(pathToFile).getTrainMetaInfo();

        final TrainMetaInfo metaInfo = new TrainMetaInfo();
        metaInfo.setWeight(763);
        metaInfo.setLength(562);
        metaInfo.setWagonsTotaly(26);
        metaInfo.setNumberOfAxles(104);
        metaInfo.setHmotnostZas(673);
        metaInfo.setMaxSpeed(100);
        metaInfo.setBreake("P");
        metaInfo.setTypeVI("Nákladní");
        metaInfo.setMimZ(Integer.parseInt("0"));
        metaInfo.setNebV(Integer.parseInt("0"));
        metaInfo.setAlive("0");

        assertThat(trainMetaInfo)
                .isNotNull()
                .containsExactly(metaInfo);
    }

    @Test
    void shouldParseCorrectlyEngines() {
        final Path pathToFile = CommonTests.getPdfPath35();

        final Engine expected = new Engine();
        expected.setDesignationNumber("91 80 6193 750-7");

        assertThat(new ParserComposition().parseCompost(pathToFile).getEngines())
                .isNotNull()
                .containsExactly(expected);
    }


}