package com.hrms.attendance.controller;

import com.hrms.attendance.dto.request.RegularizeAttendanceRequest;
import com.hrms.attendance.dto.response.AttendanceResponse;
import com.hrms.attendance.dto.response.AttendanceSummaryResponse;
import com.hrms.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance Management", description = "Attendance Management APIs")
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * Employee Check-In
     */
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Employee Check-In")
    public ResponseEntity<AttendanceResponse> checkIn() {

        return ResponseEntity.ok(
                attendanceService.checkIn());
    }

    /**
     * Employee Check-Out
     */
    @PostMapping("/check-out")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Employee Check-Out")
    public ResponseEntity<AttendanceResponse> checkOut() {

        return ResponseEntity.ok(
                attendanceService.checkOut());
    }

    /**
     * Returns attendance history
     * of logged-in employee.
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Get My Attendance")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance() {

        return ResponseEntity.ok(
                attendanceService.getMyAttendance());
    }

    /**
     * Returns attendance of logged-in employee
     * for a specific date.
     */
    @GetMapping("/my/{date}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Get My Attendance By Date")
    public ResponseEntity<AttendanceResponse> getMyAttendanceByDate(

            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                attendanceService.getMyAttendanceByDate(date));
    }

    /**
     * Returns monthly attendance
     * of logged-in employee.
     */
    @GetMapping("/my/month/{year}/{month}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Get My Monthly Attendance")
    public ResponseEntity<List<AttendanceResponse>>
    getMyAttendanceByMonth(

            @PathVariable int year,
            @PathVariable int month) {

        return ResponseEntity.ok(
                attendanceService.getMyAttendanceByMonth(
                        year,
                        month));
    }

    /**
     * Returns monthly attendance summary
     * of logged-in employee.
     */
    @GetMapping("/my/summary/{year}/{month}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Get My Attendance Summary")
    public ResponseEntity<AttendanceSummaryResponse>
    getMyAttendanceSummary(

            @PathVariable int year,
            @PathVariable int month) {

        return ResponseEntity.ok(
                attendanceService.getMyAttendanceSummary(
                        year,
                        month));
    }


    /*
     * ==========================================================
     * Admin APIs
     * ==========================================================
     */

    /**
     * Returns all attendance records.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Attendance")
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance());
    }

    /**
     * Returns attendance by attendance ID.
     */
    @GetMapping("/{attendanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Attendance By ID")
    public ResponseEntity<AttendanceResponse> getAttendanceById(

            @PathVariable Long attendanceId) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(attendanceId));
    }

    /**
     * Returns attendance of all employees
     * for a specific date.
     */
    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Attendance By Date")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByDate(

            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByDate(date));
    }

    /**
     * Returns complete attendance history
     * of a specific employee.
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Employee Attendance")
    public ResponseEntity<List<AttendanceResponse>>
    getAttendanceByEmployee(

            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByEmployee(employeeId));
    }

    /**
     * Returns attendance of a specific employee
     * for a specific date.
     */
    @GetMapping("/employee/{employeeId}/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Employee Attendance By Date")
    public ResponseEntity<AttendanceResponse>
    getAttendanceByEmployeeAndDate(

            @PathVariable Long employeeId,

            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByEmployeeAndDate(
                        employeeId,
                        date));
    }

    /**
     * Returns monthly attendance
     * of a specific employee.
     */
    @GetMapping("/employee/{employeeId}/month/{year}/{month}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Employee Monthly Attendance")
    public ResponseEntity<List<AttendanceResponse>>
    getAttendanceByEmployeeAndMonth(

            @PathVariable Long employeeId,

            @PathVariable int year,

            @PathVariable int month) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByEmployeeAndMonth(
                        employeeId,
                        year,
                        month));
    }

    /**
     * Returns monthly attendance summary
     * of a specific employee.
     */
    @GetMapping("/employee/{employeeId}/summary/{year}/{month}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Employee Attendance Summary")
    public ResponseEntity<AttendanceSummaryResponse>
    getAttendanceSummaryByEmployee(

            @PathVariable Long employeeId,

            @PathVariable int year,

            @PathVariable int month) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceSummaryByEmployee(
                        employeeId,
                        year,
                        month));
    }

    /**
     * Returns company-wide attendance summary.
     */
    @GetMapping("/summary/{year}/{month}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Company Attendance Summary")
    public ResponseEntity<AttendanceSummaryResponse>
    getCompanyAttendanceSummary(

            @PathVariable int year,

            @PathVariable int month) {

        return ResponseEntity.ok(
                attendanceService.getCompanyAttendanceSummary(
                        year,
                        month));
    }

    /**
     * Regularizes attendance.
     */
    @PatchMapping("/{attendanceId}/regularize")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Regularize Attendance")
    public ResponseEntity<AttendanceResponse>
    regularizeAttendance(

            @PathVariable Long attendanceId,

            @Valid
            @RequestBody
            RegularizeAttendanceRequest request) {

        return ResponseEntity.ok(
                attendanceService.regularizeAttendance(
                        attendanceId,
                        request));
    }


}