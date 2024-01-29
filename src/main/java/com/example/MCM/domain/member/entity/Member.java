package com.example.MCM.domain.member.entity;

import com.example.MCM.base.entity.BaseEntity;

import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.member.MemberRole;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

  @OneToMany(mappedBy = "author", orphanRemoval = true)
  private List<Product> productList;

  @OneToOne
  private Cart cart;

}