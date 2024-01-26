package com.example.MCM.domain.member.entity;

import com.example.MCM.base.entity.BaseEntity;

import com.example.MCM.domain.member.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Member extends BaseEntity {

  @Column(unique = true)
  private String username;

  private String password;

  @Email
  private String email;

  @Column(unique = true)
  private String nickname;

  private String address;

  @Column(unique = true)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  private int mailKey;

  private boolean mailAuth;

  private boolean isDeleted = false;

  private LocalDateTime deleted;

  public void updateDeleted() {
    this.deleted = LocalDateTime.now();
  }

}