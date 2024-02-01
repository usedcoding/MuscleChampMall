package com.example.MCM.domain.product.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.dto.ProductDto;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Value("${custom.originPath}")
  private String originPath;

  public List<Product> getAll() {
    return this.productRepository.findAll();
  }

  public Page<Product> getList(String category, String subCategory, int page, String kw) {
    if (StringUtils.isEmpty(category)) {
      return getAllProducts(page, kw);
    } else if (StringUtils.isEmpty(subCategory)) {
      return getCategoryProducts(category, page, kw);
    } else {
      return getSubCategoryProducts(category, subCategory, page, kw);
    }
  }

  private Page<Product> getSubCategoryProducts(String category, String subCategory, int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
    if (category.equals("GOODS")){
      getGoodsProducts(page, kw);
      return this.productRepository.findAllGoodsByKeywordAndSubCategory(kw, pageable, subCategory);
    } else if (category.equals("EQUIPMENT")) {
      getEquipmentProducts(page, kw);
      return this.productRepository.findAllEquipmentByKeywordAndSubCategory(kw, pageable, subCategory);
    } else if (category.equals("FOOD")) {
      getFoodProducts(page, kw);
      return this.productRepository.findAllFoodByKeywordAndSubCategory(kw, pageable, subCategory);
    }
    return Page.empty();
  }

  private Page<Product> getCategoryProducts(String category, int page, String kw) {
      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("createDate"));
      Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));

    if (category.equals("GOODS")) {
      getGoodsProducts(page, kw);
      return this.productRepository.findAllGoodsByKeyword(kw, pageable);
    } else if (category.equals("EQUIPMENT")) {
      getEquipmentProducts(page, kw);
      return this.productRepository.findAllEquipmentByKeyword(kw, pageable);
    } else if (category.equals("FOOD")) {
      getFoodProducts(page, kw);
      return this.productRepository.findAllFoodByKeyword(kw, pageable);
    }
    return Page.empty();
  }

  private Page<Product> getAllProducts(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
    return productRepository.findAll(pageable);
  }

  public Product findById(Long id) {
    Optional<Product> product = this.productRepository.findById(id);
    if (product.isPresent()) {
      return product.get();
    } throw new DataNotFoundException("product not found");
  }

  public Optional<Product> findProductById(Long id) {
    return this.productRepository.findById(id);
  }

  @Transactional
  public Product create(ProductDto productDto, List<MultipartFile> files, Member author) throws IOException {

    String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload";

    List<String> fileNames = new ArrayList<>();
    List<String> filePaths = new ArrayList<>();

    for(MultipartFile file : files) {

      UUID uuid = UUID.randomUUID();

      String fileName = uuid + "_" + file.getOriginalFilename();
      String filePath = originPath + fileName;

      File saveFile = new File(projectPath, fileName);
      file.transferTo(saveFile);

      fileNames.add(fileName);
      filePaths.add(filePath);
    }

    Product product = Product.builder()
          .name(productDto.getName())
          .content(productDto.getContent())
          .price(productDto.getPrice())
          .description(productDto.getDescription())
          .author(author)
          .imgPath(filePaths)
          .imgName(fileNames)
          .viewCount(0L)
          .category(productDto.getCategory())
          .subCategory(productDto.getSubCategory())
          .createDate(LocalDateTime.now())
          .build();
    this.productRepository.save(product);

    return product;
  }

  @Transactional
  public void modify(Product product, ProductDto productCreateForm) {
    product = product.toBuilder()
        .name(productCreateForm.getName())
        .price(productCreateForm.getPrice())
        .category(productCreateForm.getCategory())
        .subCategory(productCreateForm.getSubCategory())
        .modifyDate(LocalDateTime.now())
        .build();
    this.productRepository.save(product);
  }

  public Page<Product> getGoodsProducts(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
    return this.productRepository.findAllGoodsByKeyword(kw, pageable);
  }

  public Page<Product> getEquipmentProducts(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
    return this.productRepository.findAllEquipmentByKeyword(kw, pageable);
  }

  public Page<Product> getFoodProducts(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
    return this.productRepository.findAllFoodByKeyword(kw, pageable);
  }

  public List<String> getSubCategoriesByCategory(String category) {
    return this.productRepository.findSubCategoriesByCategory(category);
  }

  public void addViewCount(Product product) {
    product = product.toBuilder()
        .viewCount(product.getViewCount() + 1)
        .build();
    this.productRepository.save(product);
  }
}
