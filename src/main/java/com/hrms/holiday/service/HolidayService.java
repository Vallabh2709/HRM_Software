package com.hrms.holiday.service;

import com.hrms.holiday.dto.request.CreateHolidayRequest;
import com.hrms.holiday.dto.request.UpdateHolidayRequest;
import com.hrms.holiday.dto.response.HolidayResponse;

import java.util.List;

public interface HolidayService {

    /**
     * Create a new holiday.
     *
     * @param request holiday details
     * @return created holiday
     */
    HolidayResponse createHoliday(CreateHolidayRequest request);

    /**
     * Get holiday by id.
     *
     * @param id holiday id
     * @return holiday details
     */
    HolidayResponse getHolidayById(Long id);

    /**
     * Get all active holidays.
     *
     * @return list of holidays
     */
    List<HolidayResponse> getAllHolidays();

    /**
     * Get holidays for a particular year.
     *
     * @param year year
     * @return list of holidays
     */
    List<HolidayResponse> getHolidaysByYear(int year);

    /**
     * Update holiday.
     *
     * @param id holiday id
     * @param request updated holiday details
     * @return updated holiday
     */
    HolidayResponse updateHoliday(
            Long id,
            UpdateHolidayRequest request
    );

    /**
     * Soft delete holiday.
     *
     * @param id holiday id
     */
    void deleteHoliday(Long id);

    /**
     * Restore deleted holiday.
     *
     * @param id holiday id
     */
    void restoreHoliday(Long id);

}