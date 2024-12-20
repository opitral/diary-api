package com.opitral.diaryapi.controllers;

import com.opitral.diaryapi.common.CommonResponse;
import com.opitral.diaryapi.dto.RecordDto;
import com.opitral.diaryapi.services.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<RecordDto>>> getAllRecords() {
        return ResponseEntity.ok(CommonResponse.ok(recordService.getAllRecords()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<RecordDto>> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.ok(recordService.getRecordById(id)));
    }

    @GetMapping("/dates")
    public ResponseEntity<CommonResponse<List<RecordDto>>> getRecordsBetweenDates(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(CommonResponse.ok(recordService.getRecordsBetweenDates(startDate, endDate)));
    }

    @GetMapping("/dates/{date}")
    public ResponseEntity<CommonResponse<RecordDto>> getRecordByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(CommonResponse.ok(recordService.getRecordByDate(date)));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createRecord(@Validated @RequestBody RecordDto record) {
        return new ResponseEntity<>(CommonResponse.ok(recordService.createRecord(record)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> updateRecord(@PathVariable Long id, @Validated @RequestBody RecordDto record) {
        return ResponseEntity.ok(CommonResponse.ok(recordService.updateRecord(id, record)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deleteRecord(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.ok(recordService.deleteRecord(id)));
    }

}
