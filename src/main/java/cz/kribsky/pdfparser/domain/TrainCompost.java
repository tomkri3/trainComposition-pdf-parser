package cz.kribsky.pdfparser.domain;

import lombok.Data;

import java.util.List;

/**
 * Main class which is equal to OnePdf
 */
@Data
public class TrainCompost {
    private final MainTrainMetaInfo mainTrainMetaInfo;
    private final List<SinglePath> singlePaths;
}
