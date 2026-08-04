package com.hrms.holiday.dto.response;

import com.hrms.enums.HolidayType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Holiday response")
public class HolidayResponse {

    @Schema(
            description = "Holiday ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Holiday name",
            example = "Republic Day"
    )
    private String name;

    @Schema(
            description = "Holiday date",
            example = "2026-01-26"
    )
    private LocalDate holidayDate;

    @Schema(
            description = "Holiday description",
            example = "National Holiday"
    )
    private String description;

    @Schema(
            description = "Holiday type",
            example = "MANDATORY"
    )
    private HolidayType holidayType;

    @Schema(
            description = "Holiday status",
            example = "true"
    )
    private boolean active;

    @Schema(
            description = "Created timestamp"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Last updated timestamp"
    )
    private LocalDateTime updatedAt;

}