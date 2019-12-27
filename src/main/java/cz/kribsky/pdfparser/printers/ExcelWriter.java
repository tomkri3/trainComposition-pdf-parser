package cz.kribsky.pdfparser.printers;

import com.google.common.collect.Iterables;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class ExcelWriter implements PrinterInterface {
    @Override
    public void printToFile(List<? extends PrintableInterface> wagons, File file) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            final XSSFSheet sheet = workbook.createSheet(file.getName());

            final List<String> header = List.of(Iterables.getFirst(wagons, null).getHeader());
            // Fill header
            fillRow(
                    header,
                    sheet.createRow(0)
            );
            printRows(wagons, sheet);
            autosizeAllCollumns(sheet, header.size());
            workbook.write(Files.newOutputStream(file.toPath()));
        }
    }

    private void printRows(List<? extends PrintableInterface> wagons, XSSFSheet sheet) {
        for (int i = 0; i < wagons.size(); i++) {
            final PrintableInterface wagon = wagons.get(i);
            // +1 bcs first is header
            fillRow(wagon.getPrintableValues(), sheet.createRow(i + 1));
        }
    }

    private void autosizeAllCollumns(XSSFSheet sheet, int headerSize) {
        for (int i = 0; i < headerSize; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillRow(List<String> values, XSSFRow row) {
        for (int j = 0; j < values.size(); j++) {
            String s = values.get(j);
            final XSSFCell cell = row.createCell(j);
            cell.setCellValue(s);
        }
    }
}
