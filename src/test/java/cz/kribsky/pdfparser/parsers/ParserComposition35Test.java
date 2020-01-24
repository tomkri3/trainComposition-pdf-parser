package cz.kribsky.pdfparser.parsers;


import com.google.common.collect.MoreCollectors;
import cz.kribsky.pdfparser.CommonTests;
import cz.kribsky.pdfparser.domain.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.kribsky.pdfparser.CommonTests.getAllCollections;
import static org.assertj.core.api.Assertions.assertThat;

class ParserComposition35Test {

    @Test
    void shouldParseCorrectlyWagons() {
        final Path pathToFile = CommonTests.getPdfPath35();
        final Collection<Wagon> parse = getAllCollections(new ParserComposition().parseCompost(pathToFile), SinglePath::getWagons);

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

        List<TrainMetaInfo> trainMetaInfo = new ParserComposition().parseCompost(pathToFile)
                .getSinglePaths()
                .stream()
                .map(SinglePath::getTrainMetaInfo)
                .collect(Collectors.toUnmodifiableList());

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

        assertThat(getAllCollections(new ParserComposition().parseCompost(pathToFile), SinglePath::getEngines))
                .isNotNull()
                .containsExactly(expected);
    }

    @Test
    void shouldParseTrainPath(){
        final Path pathToFile = CommonTests.getPdfPath35();

        final TrainPath expected = TrainPath.builder().fromStation("CZ - 30145 Lanžhot st.hr.").toStation("CZ - 35737 Přibyslav").build();
        final List<TrainPath> collect = new ParserComposition().parseCompost(pathToFile).getSinglePaths().stream().map(SinglePath::getTrainPath).collect(Collectors.toUnmodifiableList());

        assertThat(collect)
                .containsExactly(expected);
    }


}