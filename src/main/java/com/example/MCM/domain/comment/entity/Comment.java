package com.example.MCM.domain.comment.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Comment extends BaseEntity {

    private String content;

    @ManyToOne
    private Member author;

    @ManyToOne
    private Post post;


}
