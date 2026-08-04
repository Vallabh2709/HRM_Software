package com.hrms.entity;

import com.hrms.common.entity.BaseEntity;
import com.hrms.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(//This guarantees that an employee can have only one attendance record per day, even if two requests reach the server simultaneously.
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_attendance_date",
                        columnNames = {
                                "employee_id",
                                "attendance_date"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(//optional = false makes it explicit in JPA that every Attendance must be associated with an Employee
            name = "employee_id",
            nullable = false
    )
    private Employee employee;

    @Column(
            name = "attendance_date",
            nullable = false
    )
    private LocalDate attendanceDate;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(
            name = "working_minutes",
            nullable = false
    )
    @Builder.Default
    private Integer workingMinutes = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column(length = 255)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private boolean regularized = false;

}