package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.SalesReportDto;
import com.foodsystem.backend.dto.TopSellingFoodDto;
import com.foodsystem.backend.repository.OrderRepository;
import com.foodsystem.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public SalesReportDto getDailySalesReport(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        
        String period = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return getSalesReport(start, end, period);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReportDto getMonthlySalesReport(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime start = startOfMonth.atStartOfDay();
        LocalDateTime end = startOfMonth.plusMonths(1).minusDays(1).atTime(LocalTime.MAX);

        String period = startOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return getSalesReport(start, end, period);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReportDto getYearlySalesReport(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDateTime start = startOfYear.atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);

        String period = String.valueOf(year);
        return getSalesReport(start, end, period);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopSellingFoodDto> getTopSellingFoods(int limit) {
        List<Object[]> rawResults = orderRepository.findTopSellingFoods(PageRequest.of(0, limit));
        return rawResults.stream().map(row -> {
            Long menuItemId = (Long) row[0];
            String itemName = (String) row[1];
            Long totalQuantity = (Long) row[2];
            Double totalRevenue = (Double) row[3];
            return TopSellingFoodDto.builder()
                    .menuItemId(menuItemId)
                    .itemName(itemName)
                    .totalQuantity(totalQuantity)
                    .totalRevenue(totalRevenue)
                    .build();
        }).collect(Collectors.toList());
    }

    private SalesReportDto getSalesReport(LocalDateTime start, LocalDateTime end, String period) {
        Double totalSales = orderRepository.calculateSalesBetween(start, end);
        if (totalSales == null) {
            totalSales = 0.0;
        }

        Long totalOrders = orderRepository.countOrdersBetween(start, end);
        if (totalOrders == null) {
            totalOrders = 0L;
        }

        double averageOrderValue = 0.0;
        if (totalOrders > 0) {
            averageOrderValue = totalSales / totalOrders;
        }

        return SalesReportDto.builder()
                .period(period)
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .averageOrderValue(averageOrderValue)
                .build();
    }
}
