package com.example.MCM.domain.product.controller;


import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.product.dto.ProductDto;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  private final MemberService memberService;

  private final ReviewService reviewService;

  @GetMapping("/list")
  public String list(Model model){

    List<Product> productList = this.productService.getAll();

    model.addAttribute("productList", productList);

    return "product/list";
  }

  @GetMapping(value = "/{id}")
  public String detail(Model model,
                       @PathVariable("id") Long id){


    Product product = this.productService.findById(id);

    model.addAttribute("product", product);

    List<Review> reviewList = this.reviewService.getReviewList(product);
    model.addAttribute("review", reviewList);

    return "product/detail";
  }

  @GetMapping("/create")
  public String create(ProductDto productCreateForm,
                       Principal principal) {
    return "product/create";
  }

  @PostMapping("/create")
  @Transactional
  public String create(@ModelAttribute("productCreateForm") ProductDto productDto,
                       BindingResult bindingResult,
                       @RequestParam("files") List<MultipartFile> files,
                       Principal principal) throws IOException{

    if (bindingResult.hasErrors())
      return "product/create";

    Member author = this.memberService.getMember(principal.getName());

    Product product = this.productService.create(productDto, files, author);

    return "redirect:/product/list";
  }

  @GetMapping("/modify/{id}")
  public String modify(@PathVariable("id") Long id,
                       ProductDto productCreateForm,
                       Principal principal) {

    Product product = this.productService.findById(id);

    this.productService.modify(product, productCreateForm);

    return "product/create";
  }

  @PostMapping("/modify/{id}")
  public String modify(@PathVariable("id") Long id,
                       @Valid ProductDto productCreateForm,
                       Principal principal,
                       BindingResult bindingResult) {
    if (bindingResult.hasErrors()) return "product/create";

    Product product = this.productService.findById(id);

    this.productService.modify(product, productCreateForm);

    return String.format("redirect:/product/detail/{id}", id);
  }

}