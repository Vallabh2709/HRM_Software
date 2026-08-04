package com.hrms.repository;

import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Checks whether attendance already exists
     * for an employee on a specific date.
     */
    boolean existsByEmployeeAndAttendanceDate(
            Employee employee,
            LocalDate attendanceDate
    );

    /**
     * Returns attendance of an employee
     * for a specific date.
     */
    Optional<Attendance> findByEmployeeAndAttendanceDate(
            Employee employee,
            LocalDate attendanceDate
    );

    /**
     * Returns attendance history
     * of an employee.
     */
    List<Attendance> findByEmployeeOrderByAttendanceDateDesc(
            Employee employee
    );

    /**
     * Returns attendance of all employees
     * for a specific date.
     */
    List<Attendance> findByAttendanceDateOrderByCheckInAsc(
            LocalDate attendanceDate
    );

    /**
     * Returns attendance of an employee
     * between two dates.
     */
    List<Attendance> findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate
    );
    /**
     * Returns attendance records between two dates.
     */
    List<Attendance> findByAttendanceDateBetweenOrderByAttendanceDateAsc(
            LocalDate startDate,
            LocalDate endDate
    );


}