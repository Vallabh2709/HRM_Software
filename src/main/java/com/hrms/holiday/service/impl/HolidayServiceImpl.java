package com.hrms.holiday.service.impl;

import com.hrms.entity.Holiday;
import com.hrms.exception.InvalidRequestException;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.holiday.dto.request.CreateHolidayRequest;
import com.hrms.holiday.dto.request.UpdateHolidayRequest;
import com.hrms.holiday.dto.response.HolidayResponse;
import com.hrms.holiday.service.HolidayService;
import com.hrms.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public HolidayResponse createHoliday(CreateHolidayRequest request) {

        String holidayName = request.getName().trim();

        if (holidayRepository.existsHoliday(
                holidayName,
                request.getHolidayDate())) {

            throw new ResourceAlreadyExistsException(
                    "Holiday already exists.");
        }

        Holiday holiday = Holiday.builder()
                .name(holidayName)
                .holidayDate(request.getHolidayDate())
                .description(request.getDescription())
                .holidayType(request.getHolidayType())
                .active(true)
                .build();

        Holiday savedHoliday = holidayRepository.save(holiday);

        return mapToResponse(savedHoliday);
    }

    @Override
    public HolidayResponse getHolidayById(Long id) {

        Holiday holiday = getHoliday(id);

        return mapToResponse(holiday);
    }

    @Override
    public List<HolidayResponse> getAllHolidays() {

        return holidayRepository
                .findAllByActiveTrueOrderByHolidayDateAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Returns active holiday.
     */
    private Holiday getHoliday(Long id) {

        return holidayRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Holiday not found with id: " + id));
    }

    @Override
    public List<HolidayResponse> getHolidaysByYear(int year) {

        return holidayRepository
                .findAllByActiveTrueAndHolidayDateBetweenOrderByHolidayDateAsc(
                        LocalDate.of(year, 1, 1),
                        LocalDate.of(year, 12, 31)
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public HolidayResponse updateHoliday(
            Long id,
            UpdateHolidayRequest request) {

        Holiday holiday = getHoliday(id);

        String holidayName = request.getName().trim();

        if (holidayRepository.existsHolidayForUpdate(
                holidayName,
                request.getHolidayDate(),
                id)) {

            throw new ResourceAlreadyExistsException(
                    "Holiday already exists.");
        }

        holiday.setName(holidayName);
        holiday.setHolidayDate(request.getHolidayDate());
        holiday.setDescription(request.getDescription());
        holiday.setHolidayType(request.getHolidayType());

        Holiday updatedHoliday = holidayRepository.save(holiday);

        return mapToResponse(updatedHoliday);
    }

    @Override
    public void deleteHoliday(Long id) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Holiday not found with id: " + id));

        if (!holiday.isActive()) {
            throw new InvalidRequestException(
                    "Holiday is already deleted.");
        }

        holiday.setActive(false);

        holidayRepository.save(holiday);
    }

    @Override
    public void restoreHoliday(Long id) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Holiday not found with id: " + id));

        if (holiday.isActive()) {
            throw new InvalidRequestException(
                    "Holiday is already active.");
        }

        holiday.setActive(true);

        holidayRepository.save(holiday);
    }
    /**
     * Maps Holiday entity to HolidayResponse.
     */
    private HolidayResponse mapToResponse(Holiday holiday) {

        return HolidayResponse.builder()
                .id(holiday.getId())
                .name(holiday.getName())
                .holidayDate(holiday.getHolidayDate())
                .description(holiday.getDescription())
                .holidayType(holiday.getHolidayType())
                .active(holiday.isActive())
                .createdAt(holiday.getCreatedAt())
                .updatedAt(holiday.getUpdatedAt())
                .build();
    }

}