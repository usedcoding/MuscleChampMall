package com.example.MCM.domain.admin.controller;

import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.community.service.PostService;
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
import org.springframework.data.web.PageableDefault;
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

  private final PostService postService;
  @GetMapping("/product")
  public String adminProduct(Model model,
                             Principal principal,
                             @PageableDefault(page = 1) Pageable pageable){

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

   List<Product> productList = this.productService.getAll();

   model.addAttribute("productList", productList);

    return "product";
  }

  @GetMapping("/notice")
  public String adminNotice(Model model,
                            Principal principal,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Notice> noticeList = this.noticeService.getAll();

    model.addAttribute("noticeList",noticeList);

    return "admin/notice";
  }

  @GetMapping("/review")
  public String adminReview(Model model,
                            Principal principal,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {
    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Product> productList = this.productService.getAll();

    List<Review> reviewList = new ArrayList<>();

    long totalReview = 0;

    int totalPages = 0;

    for (Product product : productList) {
      Pageable pageable = PageRequest.of(page - 1, size);  // Adjust page index
      Page<Review> reviewPage = this.reviewService.getByProduct(product, pageable);
      List<Review> reviews = reviewPage.getContent();
      reviewList.addAll(reviews);

      totalReview += reviewPage.getTotalElements();
    }

    if (!reviewList.isEmpty()) {
      totalPages = (int) Math.ceil((double) totalReview / size);
    }

    model.addAttribute("page", page);
    model.addAttribute("totalReview", totalReview);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("reviewList", reviewList);

    return "admin/review";
  }

  @GetMapping("/order")
  public String adminOrders(Model model,
                            Principal principal,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    List<Orders> ordersList = this.orderService.getAll();

    model.addAttribute("orderList", ordersList);

    return "orders";
  }

  @GetMapping("/post")
  public String adminPost(Model model,
                          Principal principal,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "page", defaultValue = "1") int size) {

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

    Pageable pageable = PageRequest.of(page, size);

    Page<Post> postPage = this.postService.getPosts(pageable);

    List<Post> postList = postPage.getContent();

    long totalPosts = postPage.getTotalElements();

    int totalPages = postPage.getTotalPages();

    model.addAttribute("postList",postList);
    model.addAttribute("page", page);
    model.addAttribute("totalPosts", totalPosts);
    model.addAttribute("totalPages", totalPages);

    return "admin/post";
  }

  @GetMapping("/member")
  public String adminMember(Model model,
                            Principal principal,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {

    if (page <= 0) {
      return "redirect:/admin/member?page=" + page;
    }

    Member member = this.memberService.getMember(principal.getName());

    if (!member.getRole().equals(MemberRole.ADMIN)) return "/";

   List<Member> memberList = this.memberService.getAll();

   model.addAttribute("memberList", memberList);

    return "admin/member";
  }
}
