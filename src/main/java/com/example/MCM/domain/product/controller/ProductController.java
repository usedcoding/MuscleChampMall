package com.example.MCM.domain.product.controller;

import com.example.MCM.domain.member.service.MemberService.MemberService;
import com.example.MCM.domain.product.dto.ProductCreateForm;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  private final MemberService memberService;

  @Value("${file.root.path}")
  private String originPath;

  @Value("${file.origin.path}")
  private String uploadDir;

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
  public String create(ProductCreateForm productCreateForm,
                       Principal principal) {
    return "product/create";
  }

  @PostMapping("/create")
  public String create(@Valid ProductCreateForm productCreateForm,
                       BindingResult bindingResult,
                       MultipartFile file){

    if (bindingResult.hasErrors()) return "product/create";

    try{
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String name = uuid + "." + StringUtils.getFilenameExtension(originalFilename);

        String saveDirPath = originPath;
        String saveFilePath = saveDirPath + File.separator + name;

        file.transferTo(new File(saveFilePath));

        Product product = this.productService.create(productCreateForm, saveFilePath);

        return "redirect:/product/list";
    } catch (IOException e) {
      e.printStackTrace();
        return "redirect:/";
    }
  }

  @GetMapping("/modify/{id}")
  public String modify(@PathVariable("id") Long id,
                       ProductCreateForm productCreateForm,
                       Principal principal) {

    Product product = this.productService.findById(id);

    this.productService.modify(product, productCreateForm);

    return "product/create";
  }

  @PostMapping("/modify/{id}")
  public String modify(@PathVariable("id") Long id,
                       @Valid ProductCreateForm productCreateForm,
                       Principal principal,
                       BindingResult bindingResult) {
    if (bindingResult.hasErrors()) return "product/create";

    Product product = this.productService.findById(id);

    this.productService.modify(product, productCreateForm);

    return String.format("redirect:/product/detail/{id}", id);
  }

}
