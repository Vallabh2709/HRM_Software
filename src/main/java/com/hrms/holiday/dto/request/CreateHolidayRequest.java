package com.hrms.holiday.dto.request;

import com.hrms.enums.HolidayType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request to create a new holiday")
public class CreateHolidayRequest {

    @NotBlank(message = "Holiday name is required.")
    @Size(max = 100, message = "Holiday name cannot exceed 100 characters.")
    @Schema(
            description = "Holiday name",
            example = "Republic Day"
    )
    private String name;

    @NotNull(message = "Holiday date is required.")
    @Schema(
            description = "Holiday date",
            example = "2026-01-26"
    )

    @FutureOrPresent(message = "Holiday date cannot be in the past.")
    private LocalDate holidayDate;

    @Size(max = 255, message = "Description cannot exceed 255 characters.")
    @Schema(
            description = "Holiday description",
            example = "National Holiday"
    )
    private String description;

    @NotNull(message = "Holiday type is required.")
    @Schema(
            description = "Holiday type",
            example = "MANDATORY"
    )
    private HolidayType holidayType;

}