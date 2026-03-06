package com.example.schedulemanagement.schedule.controller;

import com.example.schedulemanagement.common.Consts.Const;
import com.example.schedulemanagement.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedulemanagement.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedulemanagement.schedule.dto.response.SchedulePageResponseDto;
import com.example.schedulemanagement.schedule.dto.response.ScheduleResponseDto;
import com.example.schedulemanagement.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<ScheduleResponseDto> saveSchedule(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @Valid @RequestBody ScheduleSaveRequestDto dto
    ) {
        return ResponseEntity.ok(scheduleService.saveSchedule(userId, dto));
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getSchedule(scheduleId));
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(userId, scheduleId, dto));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public void deleteSchedule(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteSchedule(userId, scheduleId);
    }

    @GetMapping("/schedules/page")
    public ResponseEntity<Page<SchedulePageResponseDto>> getAllPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(scheduleService.getAllPage(page, size));
    }

}
