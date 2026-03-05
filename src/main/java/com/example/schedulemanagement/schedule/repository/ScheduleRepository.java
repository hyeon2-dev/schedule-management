package com.example.schedulemanagement.schedule.repository;

import com.example.schedulemanagement.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
