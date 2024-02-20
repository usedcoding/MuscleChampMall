package com.example.MCM.domain.community.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.comment.entity.Comment;
import com.example.MCM.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
public class Post extends BaseEntity {

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Member author;

    @ManyToMany
    private Set<Member> like;

    @ManyToMany
    private Set<Member> disLike;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;



}
