package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.HolidayDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

public interface HolidayDateRepository extends JpaRepository<HolidayDate, LocalDate>, JpaSpecificationExecutor<HolidayDate> {
    HolidayDate findByDate(LocalDate localDate);
}
