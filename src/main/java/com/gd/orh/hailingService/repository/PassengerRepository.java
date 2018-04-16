package com.gd.orh.hailingService.repository;

import com.gd.orh.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
