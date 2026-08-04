package com.hrms.repository;

import com.hrms.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("""
            SELECT COUNT(h) > 0
            FROM Holiday h
            WHERE LOWER(TRIM(h.name)) = LOWER(TRIM(:name))
            AND h.holidayDate = :holidayDate
            AND h.active = true
            """)
    boolean existsHoliday(
            @Param("name") String name,
            @Param("holidayDate") LocalDate holidayDate
    );

    @Query("""
            SELECT COUNT(h) > 0
            FROM Holiday h
            WHERE LOWER(TRIM(h.name)) = LOWER(TRIM(:name))
            AND h.holidayDate = :holidayDate
            AND h.id <> :id
            AND h.active = true
            """)
    boolean existsHolidayForUpdate(
            @Param("name") String name,
            @Param("holidayDate") LocalDate holidayDate,
            @Param("id") Long id
    );

    Optional<Holiday> findByIdAndActiveTrue(Long id);

    List<Holiday> findAllByActiveTrueOrderByHolidayDateAsc();

    List<Holiday> findAllByActiveTrueAndHolidayDateBetweenOrderByHolidayDateAsc(
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<Holiday> findByHolidayDateAndActiveTrue(
            LocalDate holidayDate
    );
    boolean existsByHolidayDateAndActiveTrue(LocalDate holidayDate);
}