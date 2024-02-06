package com.example.MCM.domain.product.controller;


import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.product.dto.ProductDto;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import com.example.MCM.domain.review.ReviewCreateDTO;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
  public String list(Model model,
                     @RequestParam(value = "page", defaultValue = "1") int page,
                     @RequestParam(value = "size", defaultValue = "20") int size,
                     @RequestParam(value = "kw", defaultValue = "") String kw,
                     @RequestParam(value = "category", defaultValue = "", required = false) String category,
                     @RequestParam(value = "subCategory", defaultValue = "", required = false) String subCategory,
                     @RequestParam(value = "sort", defaultValue = "createDate", required = false) String sort){

    if (page <= 0) {
      return "redirect:/product/list?category=" + category + "&subCategory=" + subCategory + "&page=1";
    }

    Page<Product> products = this.productService.getList(category, subCategory, page, kw, sort);
    List<String> subCategories = this.productService.getSubCategoriesByCategory(category);

    model.addAttribute("products", products);
    model.addAttribute("category", category);
    model.addAttribute("subCategory", subCategory);
    model.addAttribute("sort", sort);
    model.addAttribute("subCategories", subCategories);
    model.addAttribute("kw", kw);
    model.addAttribute("size", size);

    return "product/list";
  }

  @GetMapping(value = "/{id}")
  public String detail(Model model,
                       @PathVariable("id") Long id,
                       Principal principal,
                       @ModelAttribute("reviewCreateDTO")ReviewCreateDTO reviewCreateDTO){


    Product product = this.productService.findById(id);

    if (principal != null) {
      Member member = this.memberService.getMember(principal.getName());
      if (member != null) {
        model.addAttribute("member", member);
        Long memberId = member.getId();
        model.addAttribute("memberId", memberId);
      }
    }

    model.addAttribute("product", product);

    this.productService.addViewCount(product);

    List<Review> reviewList = this.reviewService.getReviewList(product);

    model.addAttribute("review", reviewList);


    return "product/detail";
  }

  @GetMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public String create(ProductDto productCreateForm,
                       Principal principal) {
    return "product/create";
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public String create(@ModelAttribute("productCreateForm") ProductDto productDto,
                       BindingResult bindingResult,
                       @RequestParam("file") MultipartFile file,
                       Principal principal) throws IOException{

    if (bindingResult.hasErrors())
      return "product/create";

    Member author = this.memberService.getMember(principal.getName());

    this.productService.createValidate(author);

    Product product = this.productService.create(productDto, file, author);

    return "redirect:/product/list";
  }

  @GetMapping("/modify/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String modify(@PathVariable("id") Long id,
                       ProductDto productCreateForm,
                       Principal principal) throws IOException {
    Member author = this.memberService.getMember(principal.getName());

    Product product = this.productService.findById(id);

    this.productService.modifyValidate(product, author);

    return "product/modify";
  }

  @PostMapping("/modify/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String modify(@PathVariable("id") Long id,
                       @Valid ProductDto productCreateForm,
                       Principal principal,
                       @RequestParam("file") MultipartFile file,
                       BindingResult bindingResult) throws IOException {
    if (bindingResult.hasErrors()) return "product/modify";

    Member author = this.memberService.getMember(principal.getName());

    Product product = this.productService.findById(id);

    this.productService.modifyValidate(product, author);

    this.productService.modify(product, productCreateForm, file);

    return String.format("redirect:/product/{id}", id);
  }

  @GetMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String delete(@PathVariable("id") Long id,
                       Principal principal) {
    Member author = this.memberService.getMember(principal.getName());

    Product product = this.productService.findById(id);

    this.productService.deleteValidate(author, product);

    this.productService.delete(product);

    return "redirect:/product/list";
  }

}