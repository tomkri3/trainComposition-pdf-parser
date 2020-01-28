package cz.kribsky.pdfparser.printers;

import cz.kribsky.pdfparser.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainCompostPrinter {
    private final TrainCompost trainCompost;

    public TrainCompostPrinter(TrainCompost trainCompost) {
        this.trainCompost = trainCompost;
    }


    public List<PrintableInterface> getDataRows() {
        List<PrintableInterface> rslt = new ArrayList<>();
        String[] fullHeader = calculateLongestHeader(trainCompost);

        for (SinglePath singlePath : trainCompost.getSinglePaths()) {
            final List<String> engines = singlePath.getEngines().stream().map(Engine::getRowData).flatMap(Collection::stream).collect(Collectors.toList());
            final List<String> metaInfo = singlePath.getTrainMetaInfo().getRowData();
            final List<String> trainPath = singlePath.getTrainPath().getRowData();
            final List<String> mainMetaInfo = trainCompost.getMainTrainMetaInfo().getRowData();
            for (Wagon wagon : singlePath.getWagons()) {
                rslt.add(
                        new PrintableInterface() {
                            @Override
                            public List<String> getRowData() {
                                List<String> rowData = new ArrayList<>();
                                rowData.addAll(wagon.getRowData());
                                rowData.addAll(trainPath);
                                rowData.addAll(metaInfo);
                                rowData.addAll(mainMetaInfo);
                                rowData.addAll(engines);
                                return List.copyOf(rowData);
                            }

                            @Override
                            public String[] getHeader() {
                                return fullHeader;
                            }
                        }
                );
            }
        }

        return rslt;
    }

    private String[] calculateLongestHeader(TrainCompost trainCompost) {
        final int numberOfMaxEngines = trainCompost.getSinglePaths().stream().map(SinglePath::getEngines).flatMapToInt(engines -> IntStream.of(engines.size())).max().orElse(1);
        final Engine engine = trainCompost.getSinglePaths().stream().map(SinglePath::getEngines).flatMap(Collection::stream).findAny().orElseThrow();
        List<String> enginesMaxHeader = new ArrayList<>();
        for (int i = 0; i < numberOfMaxEngines; i++) {
            enginesMaxHeader.addAll(List.of(engine.getHeader()));
        }

        final SinglePath singlePath = trainCompost.getSinglePaths().stream().findAny().orElseThrow();
        return concat(
                singlePath.getWagons().stream().findAny().orElseThrow().getHeader(),
                singlePath.getTrainPath().getHeader(),
                singlePath.getTrainMetaInfo().getHeader(),
                trainCompost.getMainTrainMetaInfo().getHeader(),
                enginesMaxHeader.toArray(new String[0])
        );
    }

    static String[] concat(String[]... arrays) {
        int length = 0;
        for (String[] array : arrays) {
            length += array.length;
        }
        String[] result = new String[length];
        int pos = 0;
        for (String[] array : arrays) {
            for (String element : array) {
                result[pos] = element;
                pos++;
            }
        }
        return result;
    }
}
