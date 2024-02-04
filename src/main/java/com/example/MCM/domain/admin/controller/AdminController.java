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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                             Principal principal,
                             @RequestParam(value = "page",defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "20") int size){

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    Pageable pageable = PageRequest.of(page, size);

    Page<Product> productPage = this.productService.getProducts(pageable);

    List<Product> productList = productPage.getContent();

    long totalProducts = productPage.getTotalElements();

    int totalPages = productPage.getTotalPages();

    model.addAttribute("productList",productList);
    model.addAttribute("page", page);
    model.addAttribute("totalProducts", totalProducts);
    model.addAttribute("totalPages", totalPages);

    return "admin/product";
  }

  @GetMapping("/notice")
  public String adminNotice(Model model,
                            Principal principal,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    Pageable pageable = PageRequest.of(page, size);

    Page<Notice> noticePage = this.noticeService.getNotices(pageable);

    List<Notice> noticeList = noticePage.getContent();

    long totalNotices = noticePage.getTotalElements();

    int totalPages = noticePage.getTotalPages();

    model.addAttribute("productList",noticeList);
    model.addAttribute("page", page);
    model.addAttribute("totalProducts", totalNotices);
    model.addAttribute("totalPages", totalPages);

    return "admin/notice";
  }

  @GetMapping("/review")
  public String adminReview(Model model,
                            Principal principal,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {
    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Product> productList = this.productService.getAll();

    List<Review> reviewList = new ArrayList<>();

    for (Product product : productList) {
      Pageable pageable = PageRequest.of(page, size);
      Page<Review> productReviewPage = this.reviewService.getByproduct(product, pageable);
      List<Review> reviews = productReviewPage.getContent();
      reviewList.addAll(reviews);
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
