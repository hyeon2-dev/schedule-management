package com.example.schedulemanagement.schedule.service;

import com.example.schedulemanagement.comment.dto.CommentCountDto;
import com.example.schedulemanagement.comment.repository.CommentRepository;
import com.example.schedulemanagement.common.exception.BaseException;
import com.example.schedulemanagement.common.exception.ErrorCode;
import com.example.schedulemanagement.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedulemanagement.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedulemanagement.schedule.dto.response.SchedulePageResponseDto;
import com.example.schedulemanagement.schedule.dto.response.ScheduleResponseDto;
import com.example.schedulemanagement.schedule.entity.Schedule;
import com.example.schedulemanagement.schedule.repository.ScheduleRepository;
import com.example.schedulemanagement.user.entity.User;
import com.example.schedulemanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

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

    public Page<SchedulePageResponseDto> getAllPage(int page, int size) {
        // 클라이언트에서 1부터 전달된 페이지 번호를 0 기반으로 조정
        int adjustedPage = (page > 0) ? page - 1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("modifiedAt").descending());

        // 1. Schedule Page 조회
        Page<Schedule> schedulePage = scheduleRepository.findAll(pageable);

        // 2. 일정 ID 리스트 추출
        List<Long> scheduleIds = schedulePage.stream()
                .map(Schedule::getId)
                .collect(Collectors.toList());

        // 3. 별도 쿼리로 댓글 수 조회
        List<CommentCountDto> countResults = commentRepository.countByScheduleIds(scheduleIds);
        Map<Long, Long> commentCountMap = countResults.stream()
                .collect(Collectors.toMap(CommentCountDto::getScheduleId, CommentCountDto::getCount));

        // 4. 각 Schedule을 ScheduleResponseDto로 변환 (댓글 수는 Long을 int로 변환
        return schedulePage.map(schedule -> new SchedulePageResponseDto(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                commentCountMap.getOrDefault(schedule.getId(), 0L).intValue(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        ));
    }
}
