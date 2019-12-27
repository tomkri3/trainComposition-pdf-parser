package cz.kribsky.pdfparser.printers;

import cz.kribsky.pdfparser.domain.Engine;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import cz.kribsky.pdfparser.domain.TrainCompost;
import cz.kribsky.pdfparser.domain.Wagon;

import java.util.ArrayList;
import java.util.List;

public class TrainCompostPrintable {
    private final TrainCompost trainCompost;

    public TrainCompostPrintable(TrainCompost trainCompost) {
        this.trainCompost = trainCompost;
    }


    public List<PrintableInterface> getDataRows() {
        List<PrintableInterface> rslt = new ArrayList<>();

        List<Wagon> wagons = trainCompost.getWagons();
        for (int i = 0; i < wagons.size(); i++) {
            final List<String> rowData = buildData(wagons, i);

            rslt.add(new PrintableInterface() {
                @Override
                public List<String> getRowData() {
                    return rowData;
                }

                @Override
                public String[] getHeader() {
                    return getFullHeader();
                }
            });
        }


        return rslt;
    }

    private List<String> buildData(List<Wagon> wagons, final int i) {
        final List<String> rowData = new ArrayList<>(wagons.get(i).getRowData());
        if(i == 0) {
            rowData.addAll(trainCompost.getTrainMetaInfo().getRowData());
        }
        if(i < trainCompost.getEngines().size()){
            final Engine engine = trainCompost.getEngines().get(i);
            rowData.addAll(engine.getRowData());
        }
        if(i == 0) {
            rowData.addAll(trainCompost.getTrain().getRowData());
        }
        return rowData;
    }

    public String[] getFullHeader() {
        return concat(
                trainCompost.getWagons().stream().findAny().orElseThrow().getHeader(),
                trainCompost.getTrainMetaInfo().getHeader(),
                trainCompost.getEngines().stream().findAny().orElseThrow().getHeader(),
                trainCompost.getTrain().getHeader()
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
