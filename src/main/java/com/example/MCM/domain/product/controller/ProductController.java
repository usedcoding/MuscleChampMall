package com.example.MCM.domain.product.controller;

import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

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

}
