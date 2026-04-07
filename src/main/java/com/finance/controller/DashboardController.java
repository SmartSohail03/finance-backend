// controller/DashboardController.java
package com.finance.controller;

import com.finance.access.RequiresRole;
import com.finance.dto.DashboardSummary;
import com.finance.dto.MonthlySummary;
import com.finance.model.Role;
import com.finance.service.FinancialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final FinancialRecordService recordService;

    public DashboardController(FinancialRecordService recordService) {
        this.recordService = recordService;
    }

    // GET /api/dashboard/summary
    // ALL roles can access dashboard
    @GetMapping("/summary")
    @RequiresRole({Role.VIEWER, Role.ANALYST, Role.ADMIN})
    public ResponseEntity<DashboardSummary> getSummary() {
        return ResponseEntity.ok(recordService.getDashboardSummary());
    }

    // GET /api/dashboard/monthly?month=1&year=2024
    // ALL roles can access monthly summary
    @GetMapping("/monthly")
    @RequiresRole({Role.VIEWER, Role.ANALYST, Role.ADMIN})
    public ResponseEntity<MonthlySummary> getMonthlySummary(@RequestParam int month,
                                                             @RequestParam int year) {
        return ResponseEntity.ok(recordService.getMonthlySummary(month, year));
    }
}