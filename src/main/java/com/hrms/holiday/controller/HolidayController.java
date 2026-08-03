package com.hrms.holiday.controller;

import com.hrms.holiday.dto.request.CreateHolidayRequest;
import com.hrms.holiday.dto.request.UpdateHolidayRequest;
import com.hrms.holiday.dto.response.HolidayResponse;
import com.hrms.holiday.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
@Tag(
        name = "Holiday Management",
        description = "APIs for managing holidays"
)
public class HolidayController {

    private final HolidayService holidayService;

    @Operation(summary = "Create Holiday")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Holiday created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Holiday already exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HolidayResponse> createHoliday(
            @Valid @RequestBody CreateHolidayRequest request) {

        HolidayResponse response = holidayService.createHoliday(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get Holiday By Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holiday fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Holiday not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<HolidayResponse> getHolidayById(
            @PathVariable Long id) {

        HolidayResponse response = holidayService.getHolidayById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get All Holidays")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holidays fetched successfully")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<HolidayResponse>> getAllHolidays() {

        List<HolidayResponse> holidays =
                holidayService.getAllHolidays();

        return ResponseEntity.ok(holidays);
    }

    @Operation(summary = "Get Holidays By Year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holidays fetched successfully")
    })
    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<HolidayResponse>> getHolidaysByYear(
            @PathVariable int year) {

        List<HolidayResponse> holidays =
                holidayService.getHolidaysByYear(year);

        return ResponseEntity.ok(holidays);
    }

    @Operation(summary = "Update Holiday")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holiday updated successfully"),
            @ApiResponse(responseCode = "404", description = "Holiday not found"),
            @ApiResponse(responseCode = "409", description = "Holiday already exists")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HolidayResponse> updateHoliday(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHolidayRequest request) {

        HolidayResponse response =
                holidayService.updateHoliday(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete Holiday")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holiday deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Holiday not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHoliday(
            @PathVariable Long id) {

        holidayService.deleteHoliday(id);

        return ResponseEntity.ok(
                "Holiday deleted successfully."
        );
    }

    @Operation(summary = "Restore Holiday")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holiday restored successfully"),
            @ApiResponse(responseCode = "404", description = "Holiday not found")
    })
    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> restoreHoliday(
            @PathVariable Long id) {

        holidayService.restoreHoliday(id);

        return ResponseEntity.ok(
                "Holiday restored successfully."
        );
    }

}