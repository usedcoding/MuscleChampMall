package com.example.MCM.domain.admin.controller;

import com.example.MCM.domain.member.MemberRole;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.service.NoticeService;
import com.example.MCM.domain.order.entity.Orders;
import com.example.MCM.domain.order.service.OrderService;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  private final MemberService memberService;

  private final ProductService productService;

  private final NoticeService noticeService;

  private final ReviewService reviewService;

  private final OrderService orderService;
  @GetMapping("/product")
  public String adminProduct(Model model,
                             Principal principal){

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Product> productList = this.productService.getAll();

    model.addAttribute("productList",productList);

    return "admin/product";
  }

  @GetMapping("/notice")
  public String adminNotice(Model model,
                            Principal principal) {
    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Notice> noticeList = this.noticeService.getAll();

    model.addAttribute("noticeList", noticeList);

    return "admin/notice";
  }

  @GetMapping("/review")
  public String adminReview(Model model,
                            Principal principal) {
    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Product> productList = this.productService.getAll();

    List<Review> reviewList = new ArrayList<>();

    for (Product product : productList) {
      List<Review> productReviews = this.reviewService.getReviewsByProduct(product);
      reviewList.addAll(productReviews);
    }

    model.addAttribute("reviewList", reviewList);

    return "admin/review";
  }

  @GetMapping("/orders")
  public String adminOrders(Model model,
                            Principal principal) {

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Orders> ordersList = this.orderService.getAll();

    model.addAttribute("ordersList", ordersList);

    return "admin/orders";
  }
}
