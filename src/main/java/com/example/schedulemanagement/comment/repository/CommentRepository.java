package com.example.schedulemanagement.comment.repository;

import com.example.schedulemanagement.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
