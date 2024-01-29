package com.example.MCM.domain.member.entity;

import com.example.MCM.base.entity.BaseEntity;

import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.member.MemberRole;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import java.util.List;

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

<<<<<<< HEAD
  private boolean isDeleted = false;

  private LocalDateTime deleted;

  public void updateDeleted() {
    this.deleted = LocalDateTime.now();
  }

  @OneToMany(mappedBy = "author")
=======
  @OneToMany(mappedBy = "author", orphanRemoval = true)
>>>>>>> 674a6230a89039c706beb86d53ab168f0838cda1
  private List<Product> productList;

  @OneToOne
  private Cart cart;

}