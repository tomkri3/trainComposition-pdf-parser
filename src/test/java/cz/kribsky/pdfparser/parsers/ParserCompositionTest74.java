package cz.kribsky.pdfparser.parsers;


import cz.kribsky.pdfparser.CommonTests;
import cz.kribsky.pdfparser.domain.Engine;
import cz.kribsky.pdfparser.domain.Train;
import cz.kribsky.pdfparser.domain.TrainMetaInfo;
import cz.kribsky.pdfparser.domain.Wagon;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class ParserCompositionTest74 {

    @Test
    void shouldParseCorrectlyWagons() {
        final Path pathToFile = CommonTests.getPdfPath74();
        final Collection<Wagon> parse = new ParserComposition().parseCompost(pathToFile).getWagons();

        assertThat(parse)
                .hasSize(78);

    }

//    @Test
//    void shouldParseCorrectlyTrain() {
//        final Path pathToFile = CommonTests.getPdfPath74();
//
//        final Train parse = new ParserComposition().parseCompost(pathToFile).getTrain();
//
//        final Train expected = new Train();
//        expected.setJourneyFrom("CZ - 30145 Lanžhot st.hr.");
//        expected.setJourneyTo("CZ - 35737 Přibyslav");
//        expected.setFullName("049894 TR/3622/-KADR302215-/00/2019/20191203");
//
//        assertThat(parse)
//                .isNotNull()
//                .isEqualTo(expected);
//    }
//
//    @Test
//    void shouldParseCorrectlyTrainMetaInfo() {
//        final Path pathToFile = CommonTests.getPdfPath74();
//
//        final TrainMetaInfo parse = new ParserComposition().parseCompost(pathToFile).getTrainMetaInfo();
//
//        final TrainMetaInfo metaInfo = new TrainMetaInfo();
//        metaInfo.setWeight(763);
//        metaInfo.setLength(562);
//        metaInfo.setWagonsTotaly(26);
//        metaInfo.setNumberOfAxles(104);
//        metaInfo.setHmotnostZas(673);
//        metaInfo.setMaxSpeed(100);
//        metaInfo.setBreake("P");
//        metaInfo.setTypeVI("Nákladní");
//        metaInfo.setMimZ(Integer.parseInt("0"));
//        metaInfo.setNebV(Integer.parseInt("0"));
//        metaInfo.setAlive("0");
//
//        assertThat(parse)
//                .isNotNull()
//                .isEqualTo(metaInfo);
//    }

    @Test
    void shouldParseCorrectlyEngines() {
        final Path pathToFile = CommonTests.getPdfPath74();

        final Engine expected = new Engine();
        expected.setDesignationNumber("91 80 6193 750-7");

        assertThat(new ParserComposition().parseCompost(pathToFile).getEngines())
                .isNotNull()
                .containsExactly(expected);
    }


}