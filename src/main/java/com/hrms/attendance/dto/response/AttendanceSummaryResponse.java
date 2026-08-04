package com.hrms.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryResponse {

    private Long employeeId;

    private String employeeName;

    private Integer year;

    private Integer month;

    private Integer workingDays;

    private Integer present;

    private Integer late;

    private Integer halfDay;

    private Integer absent;

    private Integer leave;

    private Integer holiday;

    private Integer totalWorkingMinutes;

    private Integer totalEmployees;
}