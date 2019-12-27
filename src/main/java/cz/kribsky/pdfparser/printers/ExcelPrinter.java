package cz.kribsky.pdfparser.printers;

import com.google.common.collect.Iterables;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ExcelPrinter implements PrinterInterface, AutoCloseable {

    private final XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    @Override
    public void init(File file, List<? extends PrintableInterface> printableInterfaces) {
        this.sheet = workbook.createSheet(file.getName());
    }

    @Override
    public void printHeader(List<? extends PrintableInterface> printableInterfaces) {
        final List<String> header = List.of(Iterables.getFirst(printableInterfaces, null).getHeader());
        // Fill header
        fillRow(
                header,
                sheet.createRow(0)
        );
    }

    @Override
    public void printCollection(List<? extends PrintableInterface> printableInterfaces)  {
        for (int i = 0; i < printableInterfaces.size(); i++) {
            final PrintableInterface wagon = printableInterfaces.get(i);
            // +1 bcs first is header
            fillRow(wagon.getRowData(), sheet.createRow(i + 1));
        }
    }

    @Override
    public void writeAndFinish(File file) throws IOException {
        // TODO remove magic number!
        autosizeAllCollumns(sheet, 64);
        workbook.write(Files.newOutputStream(file.toPath()));
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

    @Override
    public void close() throws Exception {
        workbook.close();
    }
}
