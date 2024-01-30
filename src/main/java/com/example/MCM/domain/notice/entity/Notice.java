package com.example.MCM.domain.notice.entity;

import com.example.MCM.base.entity.BaseEntity;
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
public class Notice extends BaseEntity {

  private String subject;

  private String content;

  @ManyToOne
  private Member author;
}
