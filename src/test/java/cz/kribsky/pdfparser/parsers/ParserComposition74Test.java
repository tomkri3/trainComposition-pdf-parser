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

class ParserComposition74Test {

    @Test
    void shouldParseCorrectlyWagons() {
        final Path pathToFile = CommonTests.getPdfPath74();
        final Collection<Wagon> parse = new ParserComposition().parseCompost(pathToFile).getWagons();

        assertThat(parse)
                .hasSize(78);

    }

    @Test
    void shouldParseCorrectlyTrain() {
        final Path pathToFile = CommonTests.getPdfPath74();

        final MainTrainMetaInfo parse = new ParserComposition().parseCompost(pathToFile).getMainTrainMetaInfo();

        final MainTrainMetaInfo expected = new MainTrainMetaInfo();
        expected.setJourneyFrom("CZ - 30025 Bohumín-Vrbice st.hr");
        expected.setJourneyTo("CZ - 34424 Ostrava-Bartovice");
        expected.setFullName("043225 TR/3622/-KADR305480-/00/2019/20191207");

        assertThat(parse)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void shouldParseCorrectlyTrainMetaInfo() {
        final Path pathToFile = CommonTests.getPdfPath74();

        final List<TrainMetaInfo> parse = new ParserComposition().parseCompost(pathToFile).getTrainMetaInfo();

        final TrainMetaInfo metaInfo = new TrainMetaInfo();
        metaInfo.setWeight(3172);
        metaInfo.setLength(576);
        metaInfo.setWagonsTotaly(40);
        metaInfo.setNumberOfAxles(162);
        metaInfo.setHmotnostZas(860);
        metaInfo.setMaxSpeed(90);
        metaInfo.setBreake("P");
        metaInfo.setTypeVI("Nákladní");
        metaInfo.setMimZ(Integer.parseInt("0"));
        metaInfo.setNebV(Integer.parseInt("0"));
        metaInfo.setAlive("0");

        final TrainMetaInfo metaInfo2 = new TrainMetaInfo();
        metaInfo2.setWeight(3292);
        metaInfo2.setLength(595);
        metaInfo2.setWagonsTotaly(41);
        metaInfo2.setNumberOfAxles(168);
        metaInfo2.setHmotnostZas(860);
        metaInfo2.setMaxSpeed(90);
        metaInfo2.setBreake("P");
        metaInfo2.setTypeVI("Nákladní");
        metaInfo2.setMimZ(Integer.parseInt("0"));
        metaInfo2.setNebV(Integer.parseInt("0"));
        metaInfo2.setAlive("0");

        assertThat(parse)
                .isNotNull()
                .containsExactly(metaInfo, metaInfo2);
    }

    @Test
    void shouldParseCorrectlyEngines() {
        final Path pathToFile = CommonTests.getPdfPath74();

        final Engine expected = new Engine();
        expected.setDesignationNumber("91 51 3150 517-3");

        final Engine expected2 = new Engine();
        expected2.setDesignationNumber("91 51 3150 517-3");

        final Engine expected3 = new Engine();
        expected3.setDesignationNumber("91 56 6183 004-1");

        assertThat(new ParserComposition().parseCompost(pathToFile).getEngines())
                .isNotNull()
                .containsExactly(expected, expected2, expected3);
    }


}