package com.hrms.attendance.service.impl;

import com.hrms.attendance.dto.request.RegularizeAttendanceRequest;
import com.hrms.attendance.dto.response.AttendanceResponse;
import com.hrms.attendance.dto.response.AttendanceSummaryResponse;
import com.hrms.attendance.service.AttendanceService;
import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import com.hrms.constants.AttendanceConstants;
import com.hrms.exception.InvalidRequestException;
import com.hrms.enums.AttendanceStatus;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.HolidayRepository;
import com.hrms.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    /*
     * ==========================================================
     * Dependencies
     * ==========================================================
     */

    private final AttendanceRepository attendanceRepository;

    private final EmployeeRepository employeeRepository;

    private final HolidayRepository holidayRepository;



    /*
     * ==========================================================
     * Employee APIs
     * ==========================================================
     */

    @Override
    public AttendanceResponse checkIn() {

        Employee employee = getCurrentEmployee();

        LocalDate today = LocalDate.now();
        LocalDateTime checkInTime = LocalDateTime.now();

        if (attendanceRepository.existsByEmployeeAndAttendanceDate(
                employee,
                today)) {

            throw new InvalidRequestException(
                    "Attendance has already been marked for today.");
        }

        AttendanceStatus status = determineAttendanceStatus(
                today,
                checkInTime.toLocalTime());

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .attendanceDate(today)
                .checkIn(checkInTime)
                .workingMinutes(0)
                .status(status)
                .regularized(false)
                .build();

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        return mapToResponse(savedAttendance);
    }


    @Override
    public AttendanceResponse checkOut() {

        Employee employee = getCurrentEmployee();

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, today)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No attendance found for today."));

        if (attendance.getCheckOut() != null) {

            throw new InvalidRequestException(
                    "Check-out has already been marked.");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();

        attendance.setCheckOut(checkOutTime);

        attendance.setWorkingMinutes(
                calculateWorkingMinutes(
                        attendance.getCheckIn(),
                        checkOutTime));

        Attendance updatedAttendance =
                attendanceRepository.save(attendance);

        return mapToResponse(updatedAttendance);
    }

    @Override
    public List<AttendanceResponse> getMyAttendance() {

        Employee employee = getCurrentEmployee();

        List<Attendance> attendances =
                attendanceRepository.findByEmployeeOrderByAttendanceDateDesc(employee);

        return mapToResponseList(attendances);
    }
    @Override
    public AttendanceResponse getMyAttendanceByDate(
            LocalDate attendanceDate) {

        Employee employee = getCurrentEmployee();

        Attendance attendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, attendanceDate)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance not found."));

        return mapToResponse(attendance);
    }

    @Override
    public List<AttendanceResponse> getMyAttendanceByMonth(
            int year,
            int month) {

        Employee employee = getCurrentEmployee();

        LocalDate startDate = getStartDate(year, month);

        LocalDate endDate = getEndDate(year, month);

        List<Attendance> attendances =
                attendanceRepository
                        .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                                employee,
                                startDate,
                                endDate);

        return mapToResponseList(attendances);
    }
    @Override
    public AttendanceSummaryResponse getMyAttendanceSummary(
            int year,
            int month) {

        Employee employee = getCurrentEmployee();

        LocalDate startDate = getStartDate(year, month);

        LocalDate endDate = getEndDate(year, month);

        List<Attendance> attendances =
                attendanceRepository
                        .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                                employee,
                                startDate,
                                endDate);

        return buildAttendanceSummary(
                employee,
                attendances,
                year,
                month);
    }

    /*
     * ==========================================================
     * Admin APIs
     * ==========================================================
     */

    /**
     * Returns attendance records of all employees.
     */
    @Override
    public List<AttendanceResponse> getAllAttendance() {

        List<Attendance> attendances =
                attendanceRepository.findAll();

        return mapToResponseList(attendances);
    }

    /**
     * Returns attendance by attendance ID.
     */
    @Override
    public AttendanceResponse getAttendanceById(Long attendanceId) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance not found with id: " + attendanceId));

        return mapToResponse(attendance);
    }

    /**
     * Returns attendance of all employees for a specific date.
     */
    @Override
    public List<AttendanceResponse> getAttendanceByDate(
            LocalDate attendanceDate) {

        List<Attendance> attendances =
                attendanceRepository
                        .findByAttendanceDateOrderByCheckInAsc(attendanceDate);

        return mapToResponseList(attendances);
    }

    /**
     * Returns complete attendance history of a specific employee.
     */
    @Override
    public List<AttendanceResponse> getAttendanceByEmployee(
            Long employeeId) {

        Employee employee = getEmployee(employeeId);

        List<Attendance> attendances =
                attendanceRepository
                        .findByEmployeeOrderByAttendanceDateDesc(employee);

        return mapToResponseList(attendances);
    }

    /**
     * Returns attendance of a specific employee
     * for a specific date.
     */
    @Override
    public AttendanceResponse getAttendanceByEmployeeAndDate(
            Long employeeId,
            LocalDate attendanceDate) {

        Employee employee = getEmployee(employeeId);

        Attendance attendance =
                attendanceRepository
                        .findByEmployeeAndAttendanceDate(
                                employee,
                                attendanceDate)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attendance not found."));

        return mapToResponse(attendance);
    }

    /**
     * Returns monthly attendance history
     * of a specific employee.
     */
    @Override
    public List<AttendanceResponse> getAttendanceByEmployeeAndMonth(
            Long employeeId,
            int year,
            int month) {

        Employee employee = getEmployee(employeeId);

        LocalDate startDate = getStartDate(year, month);

        LocalDate endDate = getEndDate(year, month);

        List<Attendance> attendances =
                attendanceRepository
                        .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                                employee,
                                startDate,
                                endDate);

        return mapToResponseList(attendances);
    }
    /**
     * Returns monthly attendance summary
     * of a specific employee.
     */
    @Override
    public AttendanceSummaryResponse getAttendanceSummaryByEmployee(
            Long employeeId,
            int year,
            int month) {

        Employee employee = getEmployee(employeeId);

        LocalDate startDate = getStartDate(year, month);

        LocalDate endDate = getEndDate(year, month);

        List<Attendance> attendances =
                attendanceRepository
                        .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                                employee,
                                startDate,
                                endDate);

        return buildAttendanceSummary(
                employee,
                attendances,
                year,
                month);
    }

    /**
     * Returns company-wide attendance summary.
     */
    /**
     * Returns company-wide attendance summary
     * for a specific month.
     */
    @Override
    public AttendanceSummaryResponse getCompanyAttendanceSummary(
            int year,
            int month) {

        LocalDate startDate = getStartDate(year, month);

        LocalDate endDate = getEndDate(year, month);

        List<Attendance> attendances =
                attendanceRepository
                        .findByAttendanceDateBetweenOrderByAttendanceDateAsc(
                                startDate,
                                endDate);

        int present = 0;
        int late = 0;
        int halfDay = 0;
        int absent = 0;
        int leave = 0;
        int holiday = 0;
        int totalWorkingMinutes = 0;

        for (Attendance attendance : attendances) {

            switch (attendance.getStatus()) {

                case PRESENT -> present++;

                case LATE -> late++;

                case HALF_DAY -> halfDay++;

                case ABSENT -> absent++;

                case LEAVE -> leave++;

                case HOLIDAY -> holiday++;
            }

            totalWorkingMinutes += attendance.getWorkingMinutes();
        }

        return AttendanceSummaryResponse.builder()
                .year(year)
                .month(month)
                .totalEmployees((int) employeeRepository.count())
                .workingDays(attendances.size())
                .present(present)
                .late(late)
                .halfDay(halfDay)
                .absent(absent)
                .leave(leave)
                .holiday(holiday)
                .totalWorkingMinutes(totalWorkingMinutes)
                .build();
    }

    /**
     * Allows HR/Admin to regularize attendance.
     */
    /**
     * Allows HR/Admin to regularize attendance.
     */
    @Override
    public AttendanceResponse regularizeAttendance(
            Long attendanceId,
            RegularizeAttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance not found with id: " + attendanceId));

        attendance.setCheckIn(request.getCheckIn());

        attendance.setCheckOut(request.getCheckOut());

        attendance.setRemarks(request.getRemarks());

        attendance.setRegularized(true);

        if (request.getCheckIn() != null) {

            attendance.setStatus(
                    determineAttendanceStatus(
                            attendance.getAttendanceDate(),
                            request.getCheckIn().toLocalTime()));
        }

        if (request.getCheckIn() != null &&
                request.getCheckOut() != null) {

            attendance.setWorkingMinutes(
                    calculateWorkingMinutes(
                            request.getCheckIn(),
                            request.getCheckOut()));
        }

        Attendance updatedAttendance =
                attendanceRepository.save(attendance);

        return mapToResponse(updatedAttendance);
    }
    /*
     * ==========================================================
     * Helper Methods
     * ==========================================================
     */

    /**
     * Returns the currently logged-in employee.
     */
    /**
     * Returns the currently logged-in employee.
     */
    private Employee getCurrentEmployee() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return employeeRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found."));
    }

    /**
     * Determines attendance status based on
     * office timing and holidays.
     */
    private AttendanceStatus determineAttendanceStatus(
            LocalDate attendanceDate,
            LocalTime checkInTime) {

        if (holidayRepository.existsByHolidayDateAndActiveTrue(attendanceDate)) {
            return AttendanceStatus.HOLIDAY;
        }

        if (!checkInTime.isAfter(AttendanceConstants.GRACE_TIME)) {
            return AttendanceStatus.PRESENT;
        }

        if (checkInTime.isBefore(AttendanceConstants.HALF_DAY_TIME)) {
            return AttendanceStatus.LATE;
        }

        if (checkInTime.isBefore(AttendanceConstants.ABSENT_AFTER_TIME)) {
            return AttendanceStatus.HALF_DAY;
        }

        return AttendanceStatus.ABSENT;
    }

    /**
     * Maps Attendance entity to AttendanceResponse.
     */
    private AttendanceResponse mapToResponse(Attendance attendance) {

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(
                        attendance.getEmployee().getFirstName()
                                + " "
                                + attendance.getEmployee().getLastName())
                .attendanceDate(attendance.getAttendanceDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .workingMinutes(attendance.getWorkingMinutes())
                .status(attendance.getStatus())
                .remarks(attendance.getRemarks())
                .regularized(attendance.isRegularized())
                .build();
    }

    /**
     * Calculates total working minutes.
     */
    private Integer calculateWorkingMinutes(
            LocalDateTime checkIn,
            LocalDateTime checkOut) {

        return (int) Duration.between(checkIn, checkOut).toMinutes();
    }

    /**
     * Maps Attendance entities to AttendanceResponse list.
     */
    private List<AttendanceResponse> mapToResponseList(
            List<Attendance> attendances) {

        return attendances.stream()
                .map(this::mapToResponse)
                .toList();
    }
    /**
     * Returns the first day of the month.
     */
    private LocalDate getStartDate(
            int year,
            int month) {

        return LocalDate.of(year, month, 1);
    }

    /**
     * Returns the last day of the month.
     */
    private LocalDate getEndDate(
            int year,
            int month) {

        return getStartDate(year, month)
                .withDayOfMonth(
                        getStartDate(year, month).lengthOfMonth());
    }

    /**
     * Builds monthly attendance summary.
     */
    private AttendanceSummaryResponse buildAttendanceSummary(
            Employee employee,
            List<Attendance> attendances,
            int year,
            int month) {

        int present = 0;
        int late = 0;
        int halfDay = 0;
        int absent = 0;
        int leave = 0;
        int holiday = 0;
        int totalWorkingMinutes = 0;

        for (Attendance attendance : attendances) {

            switch (attendance.getStatus()) {

                case PRESENT -> present++;

                case LATE -> late++;

                case HALF_DAY -> halfDay++;

                case ABSENT -> absent++;

                case LEAVE -> leave++;

                case HOLIDAY -> holiday++;
            }

            totalWorkingMinutes += attendance.getWorkingMinutes();
        }

        return AttendanceSummaryResponse.builder()
                .employeeId(employee.getId())
                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())
                .year(year)
                .month(month)
                .workingDays(attendances.size())
                .present(present)
                .late(late)
                .halfDay(halfDay)
                .absent(absent)
                .leave(leave)
                .holiday(holiday)
                .totalWorkingMinutes(totalWorkingMinutes)
                .build();
    }
    /**
     * Returns employee by ID.
     */
    private Employee getEmployee(Long employeeId) {

        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + employeeId));
    }

}
