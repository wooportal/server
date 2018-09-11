package de.codeschluss.wupportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.codeschluss.wupportal.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {

}