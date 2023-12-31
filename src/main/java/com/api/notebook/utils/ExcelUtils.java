package com.api.notebook.utils;

import com.api.notebook.models.FinalAverageModel;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtils {

    public static @NotNull ByteArrayOutputStream createFinalAverageExcelTable(
            @NotNull List<FinalAverageModel> finalAverageList) {
        try {
            var byteArrayOutputStream = new ByteArrayOutputStream();
            var workBook = new XSSFWorkbook();
            var sheet = workBook.createSheet();
            var rowIndex = 0;

            sheet.setColumnWidth(1, (362/7) * 256);

            var cellStyleGlobal = workBook.createCellStyle();
            cellStyleGlobal.setAlignment(HorizontalAlignment.CENTER);

            createSheetHeader(workBook,sheet, cellStyleGlobal, rowIndex++);

            for (FinalAverageModel finalAverage:
                    finalAverageList) {
                var row = sheet.createRow(rowIndex++);
                createRowCell(row, 0, finalAverage.getAttendanceNumber(), cellStyleGlobal);
                createRowCell(row, 1, finalAverage.getName(), cellStyleGlobal);
                createRowCell(row, 2, finalAverage.getAttendanceNumber(), cellStyleGlobal);
                createRowCell(row, 3, finalAverage.getFinalGrade(), cellStyleGlobal);
                createRowCell(row, 4, finalAverage.getAbsences(), cellStyleGlobal);
                createRowCell(row, 5, finalAverage.getCompensatedAbsence(), cellStyleGlobal);
            }

            workBook.write(byteArrayOutputStream);

            return byteArrayOutputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createSheetHeader(
            @NotNull XSSFWorkbook workbook,
            @NotNull XSSFSheet sheet,
            @NotNull XSSFCellStyle cellStyle,
            int index) {
        var row = sheet.createRow(index);

        var cellStyleBold = workbook.createCellStyle();
        var cellFont = workbook.createFont();
        cellFont.setBold(true);
        cellStyleBold.cloneStyleFrom(cellStyle);
        cellStyleBold.setFont(cellFont);

        createRowCell(row, 0, "Nº CH", cellStyleBold);
        createRowCell(row, 1, "Nome", cellStyleBold);
        createRowCell(row, 2, "Nº", cellStyleBold);
        createRowCell(row, 3, "N", cellStyleBold);
        createRowCell(row, 4, "F", cellStyleBold);
        createRowCell(row, 5, "AC", cellStyleBold);
    }

    private static void createRowCell(@NotNull Row row, int index, String value, CellStyle cellStyle) {
        var cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    private static void createRowCell(@NotNull Row row, int index, Integer value, CellStyle cellStyle) {
        var cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    private static void createRowCell(@NotNull Row row, int index, Double value, CellStyle cellStyle) {
        var cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

}
