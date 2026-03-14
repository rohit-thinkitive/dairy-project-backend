package com.dairy.milkcollection.service;

import com.dairy.milkcollection.dto.response.MilkCollectionResponse;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ExportService {

    private static final String[] HEADERS = {
            "Date", "Customer ID", "Farmer Name", "Milk (L)", "Fat", "SNF", "Degree", "Rate (Rs)", "Amount (Rs)"
    };

    public byte[] exportPdf(List<MilkCollectionResponse> data, String title) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(title)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            Table table = new Table(UnitValue.createPercentArray(HEADERS.length))
                    .useAllAvailableWidth();

            for (String header : HEADERS) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setFontSize(9)));
            }

            for (MilkCollectionResponse row : data) {
                table.addCell(new Cell().add(new Paragraph(row.getDate().toString()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(row.getCustomerId()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(row.getFarmerName()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(row.getMilkLiter().toString()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(str(row.getFat())).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(str(row.getSnf())).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(row.getDegree().toString()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(row.getRate().toString()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(row.getTotalAmount().toString()).setFontSize(8)));
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public byte[] exportExcel(List<MilkCollectionResponse> data, String title) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (MilkCollectionResponse row : data) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(row.getDate().toString());
                dataRow.createCell(1).setCellValue(row.getCustomerId());
                dataRow.createCell(2).setCellValue(row.getFarmerName());
                dataRow.createCell(3).setCellValue(row.getMilkLiter().doubleValue());
                dataRow.createCell(4).setCellValue(row.getFat() != null ? row.getFat().doubleValue() : 0);
                dataRow.createCell(5).setCellValue(row.getSnf() != null ? row.getSnf().doubleValue() : 0);
                dataRow.createCell(6).setCellValue(row.getDegree().doubleValue());
                dataRow.createCell(7).setCellValue(row.getRate().doubleValue());
                dataRow.createCell(8).setCellValue(row.getTotalAmount().doubleValue());
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }

    public byte[] exportCsv(List<MilkCollectionResponse> data) {
        try {
            StringWriter sw = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(sw);

            csvWriter.writeNext(HEADERS);

            for (MilkCollectionResponse row : data) {
                csvWriter.writeNext(new String[]{
                        row.getDate().toString(),
                        row.getCustomerId(),
                        row.getFarmerName(),
                        row.getMilkLiter().toString(),
                        str(row.getFat()),
                        str(row.getSnf()),
                        row.getDegree().toString(),
                        row.getRate().toString(),
                        row.getTotalAmount().toString()
                });
            }

            csvWriter.close();
            return sw.toString().getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV", e);
        }
    }

    private String str(Object val) {
        return val != null ? val.toString() : "";
    }
}
