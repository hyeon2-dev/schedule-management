package com.example.schedulemanagement.schedule.service;

import com.example.schedulemanagement.common.exception.BaseException;
import com.example.schedulemanagement.common.exception.ErrorCode;
import com.example.schedulemanagement.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedulemanagement.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedulemanagement.schedule.dto.response.ScheduleResponseDto;
import com.example.schedulemanagement.schedule.entity.Schedule;
import com.example.schedulemanagement.schedule.repository.ScheduleRepository;
import com.example.schedulemanagement.user.entity.User;
import com.example.schedulemanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleResponseDto saveSchedule(Long userId, ScheduleSaveRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        Schedule schedule = new Schedule(user, dto.getTitle(), dto.getContent());
        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponseDto> dtoList = new ArrayList<>();

        for (Schedule schedule : schedules) {
            ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(
                    schedule.getId(),
                    schedule.getUser().getId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
            );
            dtoList.add(scheduleResponseDto);
        }
        return dtoList;
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(ErrorCode.SCHEDULE_NOT_FOUND, null)
        );

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long userId, Long scheduleId, ScheduleUpdateRequestDto dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(ErrorCode.SCHEDULE_NOT_FOUND, null)
        );

        if (!userId.equals(schedule.getUser().getId())) {
            throw new BaseException(ErrorCode.SCHEDULE_FORBIDDEN_ACCESS, null);
        }

        schedule.update(dto.getTitle(), dto.getContent());

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(ErrorCode.SCHEDULE_NOT_FOUND, null)
        );

        if (!userId.equals(schedule.getUser().getId())) {
            throw new BaseException(ErrorCode.SCHEDULE_FORBIDDEN_ACCESS, null);
        }

        scheduleRepository.delete(schedule);
    }
}
