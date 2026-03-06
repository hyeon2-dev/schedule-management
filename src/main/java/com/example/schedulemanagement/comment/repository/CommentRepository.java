package com.example.schedulemanagement.comment.repository;

import com.example.schedulemanagement.comment.dto.CommentCountDto;
import com.example.schedulemanagement.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByScheduleId(Long scheduleId);

    @Query("""
    select new com.example.schedulemanagement.comment.dto.CommentCountDto(c.schedule.id, count(c))
    from Comment c
    where c.schedule.id in :scheduleIds
    group by c.schedule.id
""")
    List<CommentCountDto> countByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);

}
