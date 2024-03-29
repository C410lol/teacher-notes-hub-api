package com.api.notebook.utils;

import com.api.notebook.models.entities.*;
import com.api.notebook.services.LessonService;
import com.api.notebook.services.NotebookService;
import com.api.notebook.services.WorkService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;

public class NotebookUtils {

    public static @NotNull ByteArrayOutputStream finalizeNotebook(
            @NotNull NotebookEntity notebook,
            Map<String, Integer> workTypeWeights
    ) throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var notebookWorkbook = new XSSFWorkbook();

        notebook.getStudents().sort(Comparator.comparing(StudentEntity::getNumber));
        notebook.getLessons().sort(Comparator.comparing(LessonEntity::getDate));
        notebook.getWorks().sort(Comparator.comparing(WorkEntity::getDeliveryDate));

        createFrequenciasSheet(notebookWorkbook, notebook);
        createMediasSheet(notebookWorkbook, notebook, workTypeWeights);
        createObservacoesSheet(notebookWorkbook, notebook);
        createFerramentasDeAvaliacaoSheet(notebookWorkbook, notebook, workTypeWeights);

        notebookWorkbook.write(byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    private static void createFrequenciasSheet(
            @NotNull XSSFWorkbook workbook,
            @NotNull NotebookEntity notebook
    ) {
        var frequenciasSheet = workbook.createSheet("Frequências");

        //Creating sheet header
        var firstRow = frequenciasSheet.createRow(0);
        ExcelUtils.createRowCell(firstRow, 0, "Número");
        ExcelUtils.createRowCell(firstRow, 1, "Aluno");
        //Creating sheet header

        //Setting columns settings
        frequenciasSheet.setColumnWidth(0,(int) (6 * 1.5 * 256));
        frequenciasSheet.setColumnWidth(1, (int) (33 * 1.5 * 256));
        //Setting Columns settings

        //Creating date formatter
        var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //Creating date formatter

        //Setting lessons date in the sheet header
        var cellCount = 2;
        for(LessonEntity lesson:
                notebook.getLessons()) {

            if(lesson.getQuantity() > 1) {
                frequenciasSheet.setColumnWidth(cellCount, (int) ((10 * 1.5 * 256) / 2));
                frequenciasSheet.setColumnWidth(cellCount + 1, (int) ((10 * 1.5 * 256) / 2));

                ExcelUtils.createRowCell(firstRow, cellCount, dateFormatter.format(lesson.getDate()));
                frequenciasSheet.addMergedRegion(new CellRangeAddress(
                        0, 0, cellCount, cellCount + 1));
                cellCount += 2;
                continue;
            }

            frequenciasSheet.setColumnWidth(cellCount, (int) (10 * 1.5 * 256));
            ExcelUtils.createRowCell(firstRow, cellCount, dateFormatter.format(lesson.getDate()));
            cellCount++;
        }
        //Setting lessons date in the sheet header

        var studentRowCount = 1;
        var studentNumberCount = 1;
        for (StudentEntity student:
                notebook.getStudents()) {
            var studentRow = frequenciasSheet.createRow(studentRowCount);
            studentRowCount++;

            ExcelUtils.createRowCell(studentRow, 0, String.valueOf(studentNumberCount));
            ExcelUtils.createRowCell(studentRow, 1, student.getName());
            studentNumberCount++;

            var studentCellCount = 2;
            for (LessonEntity lesson:
                    notebook.getLessons()) {
                if (lesson.getAttendances().isEmpty()) {
                    if (lesson.getQuantity() == 2) studentCellCount += 2;
                    if (lesson.getQuantity() == 1) studentCellCount++;
                    continue;
                }

                for (int x = 0; x < lesson.getQuantity(); x++) {
                    if (lesson.getAttendances().get(x).getPresentStudents().contains(student)) {
                        ExcelUtils.createRowCell(studentRow, studentCellCount, "C");
                    } else ExcelUtils.createRowCell(studentRow, studentCellCount, "F");
                    studentCellCount++;
                }
            }
        }

        ExcelUtils.setSheetHeaderRowStyles(workbook, firstRow);
        ExcelUtils.setGlobalCellCentralizedStyle(workbook, frequenciasSheet, 1);
    }

    private static void createMediasSheet(
            @NotNull XSSFWorkbook workbook,
            NotebookEntity notebook,
            @NotNull Map<String, Integer> workTypeWeights
    ) {
        var mediasSheet = workbook.createSheet("Médias");

        //Creating sheet header
        var firstRow = mediasSheet.createRow(0);
        ExcelUtils.createRowCell(firstRow, 0, "Número");
        ExcelUtils.createRowCell(firstRow, 1, "Aluno");
        //Creating sheet header

        //Setting columns settings
        mediasSheet.setColumnWidth(0,(int) (6 * 1.5 * 256));
        mediasSheet.setColumnWidth(1, (int) (33 * 1.5 * 256));
        //Setting Columns settings

        //Setting work types in sheet header
        var workTypeCellCount = 2;
        for (Map.Entry<String, Integer> map:
                workTypeWeights.entrySet()) {
            if (map.getValue() <= 0) continue;

            mediasSheet.setColumnWidth(workTypeCellCount, (int) (11 * 1.5 * 256));

            ExcelUtils.createRowCell(firstRow, workTypeCellCount, map.getKey());
            workTypeCellCount++;
        }
        ExcelUtils.createRowCell(firstRow, workTypeCellCount, "MÉDIA");
        //setting work types in sheet header

        //Setting 'media' column width
        mediasSheet.setColumnWidth(workTypeCellCount, (int) (5 * 1.5 * 256));
        //Setting 'media' column width

        var studentRowCount = 1;
        var studentNumberCount = 1;
        for (StudentEntity student:
                notebook.getStudents()) {
            var studentRow = mediasSheet.createRow(studentRowCount);
            studentRowCount++;

            ExcelUtils.createRowCell(studentRow, 0, String.valueOf(studentNumberCount));
            ExcelUtils.createRowCell(studentRow, 1, student.getName());
            studentNumberCount++;

            var finalGrade = 0.0;

            var studentGradeCellCount = 2;
            for (Map.Entry<String, Integer> map:
                    workTypeWeights.entrySet()) {
                if (map.getValue() <= 0) continue;
                var gradesSum = 0.0;
                var quantity = 0;

                for (WorkEntity work:
                        notebook.getWorks()) {
                    if(work.getType().toString().equals(map.getKey())) {
                        for (GradeEntity grade:
                                work.getGrades()) {
                            if (grade.getStudent().equals(student)) {
                                gradesSum += grade.getGrade();
                                quantity++;
                                break;
                            }
                        }
                    }
                }

                var approximateAverage = (double) Math.round((gradesSum / quantity) * 2) / 2;
                ExcelUtils.createRowCell(studentRow, studentGradeCellCount, String.valueOf(approximateAverage));
                studentGradeCellCount++;

                finalGrade += (gradesSum * map.getValue()) / quantity;
            }

            var approximateFinalGrade = (double) Math.round((finalGrade / 10) * 2) / 2;
            ExcelUtils.createRowCell(studentRow, workTypeCellCount, String.valueOf(approximateFinalGrade));
        }

        ExcelUtils.setSheetHeaderRowStyles(workbook, firstRow);
        ExcelUtils.setGlobalCellCentralizedStyle(workbook, mediasSheet, 1);
    }

    private static void createObservacoesSheet(
            @NotNull XSSFWorkbook workbook,
            @NotNull NotebookEntity notebook
    ) {
        var observacoesSheet = workbook.createSheet("Observações");

        //Creating sheet header
        var firstRow = observacoesSheet.createRow(0);
        ExcelUtils.createRowCell(firstRow, 0, "Data");
        ExcelUtils.createRowCell(firstRow, 1, "Observação");
        //Creating sheet header

        //Setting columns settings
        observacoesSheet.setColumnWidth(0,(int) (10 * 1.5 * 256));
        observacoesSheet.setColumnWidth(1, (int) (100 * 1.5 * 256));
        //Setting Columns settings

        var lessonRowCount = 1;
        for (LessonEntity lesson:
                notebook.getLessons()) {
            if (lesson.getObservations().isEmpty() && lesson.getObservations().isBlank()) continue;
            var lessonRow = observacoesSheet.createRow(lessonRowCount);

            ExcelUtils.createRowCell(lessonRow, 0, DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    .format(lesson.getDate()));
            ExcelUtils.createRowCell(lessonRow, 1, lesson.getObservations());

            lessonRowCount++;
        }

        ExcelUtils.setSheetHeaderRowStyles(workbook, firstRow);
        ExcelUtils.setGlobalCellCentralizedStyle(workbook, observacoesSheet, 1);
    }

    private static void createFerramentasDeAvaliacaoSheet(
            @NotNull XSSFWorkbook workbook,
            NotebookEntity notebook,
            @NotNull Map<String, Integer> workTypeWeights
    ) {
        var ferramentasSheet = workbook.createSheet("Ferramentas De Avaliação");

        //Create sheet header
        var firstRow = ferramentasSheet.createRow(0);
        var secondRow = ferramentasSheet.createRow(1);
        var worksTypeFirstRowCellCount = 2;
        var worksTypeSecondRowCellCount = 2;

        //Create second row of header
        ExcelUtils.createRowCell(secondRow, 0, "Número");
        ExcelUtils.createRowCell(secondRow, 1, "Aluno");
        //Create second row of header

        //Setting columns settings
        ferramentasSheet.setColumnWidth(0,(int) (6 * 1.5 * 256));
        ferramentasSheet.setColumnWidth(1, (int) (33 * 1.5 * 256));
        //Setting Columns settings

        for (Map.Entry<String, Integer> map:
                workTypeWeights.entrySet()) {
            if (map.getValue() <= 0) continue;

            var worksCount = -1;
            for (WorkEntity work:
                    notebook.getWorks()) {
                if (work.getType().toString().equals(map.getKey())) {

                    //Create second row of header
                    ExcelUtils.createRowCell(secondRow, worksTypeSecondRowCellCount, work.getTitle());
                    worksTypeSecondRowCellCount++;
                    //Create second row of header

                    worksCount++;
                }
            }

            //Create first row of header
            ExcelUtils.createRowCell(firstRow, worksTypeFirstRowCellCount, map.getKey());
            if (worksCount > 0) {
                ferramentasSheet.addMergedRegion(new CellRangeAddress(
                        0, 0,
                        worksTypeFirstRowCellCount, worksCount + worksTypeFirstRowCellCount));
            }
            //Create first row of header

            //Setting columns width
            for (int x = 0; x < worksCount + 1; x++) {
                ferramentasSheet.setColumnWidth(x + worksTypeFirstRowCellCount, (int) (22 * 1.5 * 256));
            }
            //Setting columns width

            worksTypeFirstRowCellCount += worksCount + 1;

        }
        //Create sheet header

        var studentRowCount = 2;
        var studentNumberCount = 1;
        for (StudentEntity student:
                notebook.getStudents()) {

            //Create student row
            var studentRow = ferramentasSheet.createRow(studentRowCount);
            studentRowCount++;
            //Create student row

            //Create student's firsts cells
            ExcelUtils.createRowCell(studentRow, 0, String.valueOf(studentNumberCount));
            ExcelUtils.createRowCell(studentRow, 1, student.getName());
            studentNumberCount++;
            //Create student's firsts cells

            var studentCellCount = 2;
            for (Map.Entry<String, Integer> map:
                    workTypeWeights.entrySet()) {
                if (map.getValue() <= 0) continue;
                for (WorkEntity work:
                        notebook.getWorks()) {
                    if (work.getType().toString().equals(map.getKey())) {

                        for(GradeEntity grade:
                                work.getGrades()) {
                            if (grade.getStudent().equals(student)) {
                                ExcelUtils.createRowCell(
                                        studentRow, studentCellCount, String.valueOf(grade.getGrade()));
                                studentCellCount++;
                                break;
                            }
                        }
                    }
                }
            }
        }

        ExcelUtils.setSheetHeaderRowStyles(workbook, firstRow);
        ExcelUtils.setSheetHeaderRowStyles(workbook, secondRow);
        ExcelUtils.setGlobalCellCentralizedStyle(workbook, ferramentasSheet, 2);
    }

}
