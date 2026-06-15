package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.SalesReportDto;
import com.foodsystem.backend.dto.TopSellingFoodDto;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    SalesReportDto getDailySalesReport(LocalDate date);
    SalesReportDto getMonthlySalesReport(int year, int month);
    SalesReportDto getYearlySalesReport(int year);
    List<TopSellingFoodDto> getTopSellingFoods(int limit);
}
