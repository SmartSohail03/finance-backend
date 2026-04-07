// controller/RecordController.java
package com.finance.controller;

import com.finance.access.RequiresRole;
import com.finance.dto.RecordRequest;
import com.finance.model.FinancialRecord;
import com.finance.model.Role;
import com.finance.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final FinancialRecordService recordService;

    public RecordController(FinancialRecordService recordService) {
        this.recordService = recordService;
    }

    // POST /api/records
    // Only ADMIN can create records
    @PostMapping
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<FinancialRecord> createRecord(@Valid @RequestBody RecordRequest request) {
        FinancialRecord created = recordService.createRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/records
    // ANALYST and ADMIN can view all records
    @GetMapping
    @RequiresRole({Role.ANALYST, Role.ADMIN})
    public ResponseEntity<List<FinancialRecord>> getAllRecords() {
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    // GET /api/records/{id}
    // ANALYST and ADMIN can view a specific record
    @GetMapping("/{id}")
    @RequiresRole({Role.ANALYST, Role.ADMIN})
    public ResponseEntity<FinancialRecord> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    // PUT /api/records/{id}
    // Only ADMIN can update records
    @PutMapping("/{id}")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<FinancialRecord> updateRecord(@PathVariable Long id,
                                                        @Valid @RequestBody RecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    // DELETE /api/records/{id}
    // Only ADMIN can delete records
    @DeleteMapping("/{id}")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // GET /api/records/filter?type=INCOME
    // GET /api/records/filter?category=Rent
    // GET /api/records/filter?date=2024-01-15
    // GET /api/records/filter?startDate=2024-01-01&endDate=2024-01-31
    // ANALYST and ADMIN can filter records
    @GetMapping("/filter")
    @RequiresRole({Role.ANALYST, Role.ADMIN})
    public ResponseEntity<List<FinancialRecord>> filterRecords(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Apply whichever filter param was provided
        if (type != null) {
            return ResponseEntity.ok(recordService.filterByType(type));
        }
        if (category != null) {
            return ResponseEntity.ok(recordService.filterByCategory(category));
        }
        if (date != null) {
            return ResponseEntity.ok(recordService.filterByDate(date));
        }
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(recordService.filterByDateRange(startDate, endDate));
        }

        // No filter provided — return all records
        return ResponseEntity.ok(recordService.getAllRecords());
    }
}