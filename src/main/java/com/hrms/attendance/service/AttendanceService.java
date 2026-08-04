package com.hrms.attendance.service;

import com.hrms.attendance.dto.request.RegularizeAttendanceRequest;
import com.hrms.attendance.dto.response.AttendanceResponse;
import com.hrms.attendance.dto.response.AttendanceSummaryResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    /*
     * ==========================================================
     * Employee Attendance APIs
     * ==========================================================
     */

    /**
     * Marks attendance for the logged-in employee.
     */
    AttendanceResponse checkIn();

    /**
     * Marks check-out for the logged-in employee.
     */
    AttendanceResponse checkOut();

    /**
     * Returns complete attendance history
     * of the logged-in employee.
     */
    List<AttendanceResponse> getMyAttendance();

    /**
     * Returns attendance of the logged-in employee
     * for a specific date.
     */
    AttendanceResponse getMyAttendanceByDate(LocalDate attendanceDate);

    /**
     * Returns monthly attendance history
     * of the logged-in employee.
     */
    List<AttendanceResponse> getMyAttendanceByMonth(
            int year,
            int month
    );

    /**
     * Returns monthly attendance summary
     * of the logged-in employee.
     */
    AttendanceSummaryResponse getMyAttendanceSummary(
            int year,
            int month
    );

    /*
     * ==========================================================
     * Admin Attendance APIs
     * ==========================================================
     */

    /**
     * Returns attendance records of all employees.
     */
    List<AttendanceResponse> getAllAttendance();

    /**
     * Returns attendance by attendance ID.
     */
    AttendanceResponse getAttendanceById(Long attendanceId);

    /**
     * Returns attendance of all employees
     * for a specific date.
     */
    List<AttendanceResponse> getAttendanceByDate(LocalDate attendanceDate);

    /**
     * Returns complete attendance history
     * of a specific employee.
     */
    List<AttendanceResponse> getAttendanceByEmployee(Long employeeId);

    /**
     * Returns attendance of a specific employee
     * for a specific date.
     */
    AttendanceResponse getAttendanceByEmployeeAndDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    /**
     * Returns monthly attendance history
     * of a specific employee.
     */
    List<AttendanceResponse> getAttendanceByEmployeeAndMonth(
            Long employeeId,
            int year,
            int month
    );

    /**
     * Returns monthly attendance summary
     * of a specific employee.
     */
    AttendanceSummaryResponse getAttendanceSummaryByEmployee(
            Long employeeId,
            int year,
            int month
    );

    /**
     * Returns company-wide attendance summary
     * for a specific month.
     */
    AttendanceSummaryResponse getCompanyAttendanceSummary(
            int year,
            int month
    );

    /**
     * Allows HR/Admin to manually regularize
     * an attendance record.
     */
    AttendanceResponse regularizeAttendance(
            Long attendanceId,
            RegularizeAttendanceRequest request
    );
}