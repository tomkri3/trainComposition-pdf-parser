package cz.kribsky.pdfparser.parsers;


import cz.kribsky.pdfparser.CommonTests;
import cz.kribsky.pdfparser.domain.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.kribsky.pdfparser.CommonTests.getAllCollections;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * PDF 1, is pdf where is no wagons
 */
class ParserComposition1Test {

    @Test
    void shouldParseCorrectlyWagons() {
        final Path pathToFile = CommonTests.getPdfPath1();
        final Collection<Wagon> parse = getAllCollections(new ParserComposition().parseCompost(pathToFile), SinglePath::getWagons);

        assertThat(parse)
                .isEmpty();
    }

    @Test
    void shouldParseCorrectlyMainTrainMetaInfo() {
        final Path pathToFile = CommonTests.getPdfPath1();

        final MainTrainMetaInfo parse = new ParserComposition().parseCompost(pathToFile).getMainTrainMetaInfo();

        final MainTrainMetaInfo expected = new MainTrainMetaInfo();
        expected.setJourneyFrom("CZ - 33845 Hodonín");
        expected.setJourneyTo("CZ - 38001 Břeclav přednádraží");
        expected.setFullName("055260 TR/3622/-KADR011957-/00/2020/20200105");

        assertThat(parse)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void shouldParseCorrectlyTrainMetaInfo() {
        final Path pathToFile = CommonTests.getPdfPath1();

        List<TrainMetaInfo> trainMetaInfo = new ParserComposition().parseCompost(pathToFile)
                .getSinglePaths()
                .stream()
                .map(SinglePath::getTrainMetaInfo)
                .collect(Collectors.toUnmodifiableList());

        final TrainMetaInfo metaInfo = new TrainMetaInfo();
        metaInfo.setWeight(146);
        metaInfo.setLength(27);
        metaInfo.setWagonsTotaly(2);
        metaInfo.setNumberOfAxles(8);
        metaInfo.setHmotnostZas(0);
        metaInfo.setMaxSpeed(70);
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
        final Path pathToFile = CommonTests.getPdfPath1();

        final Engine expected = new Engine();
        expected.setDesignationNumber("92 54 2721 555-1");
        final Engine expected2 = new Engine();
        expected2.setDesignationNumber("92 54 2740 892-5");

        assertThat(getAllCollections(new ParserComposition().parseCompost(pathToFile), SinglePath::getEngines))
                .isNotNull()
                .containsExactly(expected, expected2);
    }

    @Test
    void shouldParseTrainPath(){
        final Path pathToFile = CommonTests.getPdfPath1();

        final TrainPath expected = TrainPath.builder().fromStation("CZ - 33845 Hodonín").toStation("CZ - 38001 Břeclav přednádraží").build();
        final List<TrainPath> collect = new ParserComposition().parseCompost(pathToFile).getSinglePaths().stream().map(SinglePath::getTrainPath).collect(Collectors.toUnmodifiableList());

        assertThat(collect)
                .containsExactly(expected);
    }


}