package com.example.MCM.domain.comment.service;

import com.example.MCM.domain.comment.entity.Comment;
import com.example.MCM.domain.comment.repository.CommentRepository;
import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    //댓글 목록
    public List<Comment> getCommentList() {
        return this.commentRepository.findAll();
    }

    //댓글 데이터 가져오기
    public Comment getComment(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        return comment.get();
    }

    //댓글 생성
    public Comment createComment(String content, Member author, Post post) {
        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .createDate(LocalDateTime.now())
                .post(post)
                .build();
        this.commentRepository.save(comment);
        return comment;
    }

    //댓글 수정
    public Comment modifyComment(Comment comment, String content) {
        Comment getComment = comment.toBuilder()
                .content(content)
                .modifyDate(LocalDateTime.now())
                .build();
        this.commentRepository.save(getComment);
        return comment;
    }

    //댓글 삭제
    public void deleteComment(Comment comment) {
        this.commentRepository.delete(comment);
    }
}