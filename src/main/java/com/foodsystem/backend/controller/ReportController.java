package com.foodsystem.backend.controller;

import com.foodsystem.backend.dto.SalesReportDto;
import com.foodsystem.backend.dto.TopSellingFoodDto;
import com.foodsystem.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity<SalesReportDto> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        SalesReportDto report = reportService.getDailySalesReport(date);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/monthly")
    public ResponseEntity<SalesReportDto> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {
        SalesReportDto report = reportService.getMonthlySalesReport(year, month);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/yearly")
    public ResponseEntity<SalesReportDto> getYearlyReport(
            @RequestParam int year) {
        SalesReportDto report = reportService.getYearlySalesReport(year);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<TopSellingFoodDto>> getTopSellingFoods(
            @RequestParam(defaultValue = "5") int limit) {
        List<TopSellingFoodDto> report = reportService.getTopSellingFoods(limit);
        return ResponseEntity.ok(report);
    }
}
