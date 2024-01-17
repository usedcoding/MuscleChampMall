package com.example.MCM.domain.product.controller;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService.MemberService;
import com.example.MCM.domain.product.dto.ProductCreateForm;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  private final MemberService memberService;
  @GetMapping("/list")
  public String list(Model model){

    List<Product> productList = this.productService.getAll();

    model.addAttribute("productList", productList);

    return "product/list";
  }

  @GetMapping(value = "/detail/{id}")
  public String detail(Model model,
                       @PathVariable("id") Long id){

    Product product = this.productService.findById(id);

    model.addAttribute("product", product);

    return "product/detail";
  }

  @GetMapping("/create")
  public String create(ProductCreateForm productCreateForm) {
    return "product/create";
  }

  @PostMapping("/create")
  public String create(@Valid ProductCreateForm productCreateForm,
                       BindingResult bindingResult,
                       Principal principal,
                       MultipartFile file) {

    if (bindingResult.hasErrors()) return "product/create";

    Product product = this.productService.create(productCreateForm, file);

    return "redirect:/product/list";

  }

}
