package com.hrms.attendance.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegularizeAttendanceRequest {

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    @Size(max = 255, message = "Remarks cannot exceed 255 characters.")
    private String remarks;

}