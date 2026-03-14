package com.dairy.milkcollection.controller;

import com.dairy.milkcollection.dto.response.ApiResponse;
import com.dairy.milkcollection.dto.response.MilkCollectionResponse;
import com.dairy.milkcollection.service.ExportService;
import com.dairy.milkcollection.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ExportService exportService;

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<List<MilkCollectionResponse>>> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) date = LocalDate.now();
        List<MilkCollectionResponse> report = reportService.getDailyReport(date);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<List<MilkCollectionResponse>>> getWeeklyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        List<MilkCollectionResponse> report = reportService.getWeeklyReport(weekStart);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<List<MilkCollectionResponse>>> getMonthlyReport(
            @RequestParam int year, @RequestParam int month) {
        List<MilkCollectionResponse> report = reportService.getMonthlyReport(year, month);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam String type,
            @RequestParam String format,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        List<MilkCollectionResponse> data;
        String title;

        switch (type) {
            case "daily":
                LocalDate reportDate = date != null ? date : LocalDate.now();
                data = reportService.getDailyReport(reportDate);
                title = "Daily Milk Collection Report - " + reportDate;
                break;
            case "weekly":
                data = reportService.getWeeklyReport(weekStart);
                title = "Weekly Report - " + weekStart + " to " + weekStart.plusDays(6);
                break;
            case "monthly":
                data = reportService.getMonthlyReport(year, month);
                title = "Monthly Report - " + year + "/" + String.format("%02d", month);
                break;
            default:
                throw new IllegalArgumentException("Invalid report type: " + type);
        }

        byte[] content;
        String contentType;
        String filename;

        switch (format) {
            case "pdf":
                content = exportService.exportPdf(data, title);
                contentType = "application/pdf";
                filename = "report_" + type + "." + "pdf";
                break;
            case "excel":
                content = exportService.exportExcel(data, title);
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                filename = "report_" + type + ".xlsx";
                break;
            case "csv":
                content = exportService.exportCsv(data);
                contentType = "text/csv";
                filename = "report_" + type + ".csv";
                break;
            default:
                throw new IllegalArgumentException("Invalid format: " + format);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(content);
    }
}
