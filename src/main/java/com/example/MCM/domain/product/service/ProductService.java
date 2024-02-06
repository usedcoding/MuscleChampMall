package com.example.MCM.domain.product.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.member.MemberRole;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.dto.ProductDto;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.repository.ProductRepository;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  private final ReviewService reviewService;

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

  public void createValidate(Member author) {
    if (!author.getRole().equals(MemberRole.ADMIN)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성 권한이 없습니다.");
    }
  }

  @Transactional
  public Product create(ProductDto productDto, MultipartFile file, Member author) throws IOException {

    String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload";

      UUID uuid = UUID.randomUUID();

      String fileName = uuid + "_" + file.getOriginalFilename();
      String filePath = originPath + fileName;

      File saveFile = new File(projectPath, fileName);
      file.transferTo(saveFile);

    Product product = Product.builder()
          .name(productDto.getName())
          .content(productDto.getContent())
          .price(productDto.getPrice())
          .description(productDto.getDescription())
          .author(author)
          .imgPath(filePath)
          .imgName(fileName)
          .viewCount(0L)
          .category(productDto.getCategory())
          .subCategory(productDto.getSubCategory())
          .createDate(LocalDateTime.now())
          .build();
    this.productRepository.save(product);

    return product;
  }

  public void modifyValidate(Product product, Member author) {
    if (author.getRole().equals(MemberRole.ADMIN)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
    }
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다.");
    }
  }
  @Transactional
  public void modify(Product product, ProductDto productCreateForm, MultipartFile file) throws IOException {

    String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload";

      UUID uuid = UUID.randomUUID();

      String fileName = uuid + "_" + file.getOriginalFilename();
      String filePath = originPath + fileName;

      File saveFile = new File(projectPath, fileName);
      file.transferTo(saveFile);

    Product modifyProduct = product.toBuilder()
        .name(productCreateForm.getName())
        .price(productCreateForm.getPrice())
        .category(productCreateForm.getCategory())
        .subCategory(productCreateForm.getSubCategory())
        .imgPath(filePath)
        .imgName(fileName)
        .modifyDate(LocalDateTime.now())
        .build();
    this.productRepository.save(modifyProduct);
  }

  public void deleteValidate(Member author, Product product) {
    if (!author.getRole().equals(MemberRole.ADMIN)) {
      throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
    }
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다.");
    }
  }

  public void delete(Product product) {
    this.productRepository.delete(product);
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

  public List<Product> getAllByReviewStarScore() {
    return this.productRepository.findAll().stream()
        .sorted(Comparator.comparingDouble(product -> -calculateAverageStarScore(product.getId())))
        .collect(Collectors.toList());
  }

  private double calculateAverageStarScore(Long productId) {
    List<Review> reviews = reviewService.getReviewsByProductId(productId);
    if (reviews.isEmpty()) return 0.0;
    return reviews.stream()
        .mapToDouble(Review::getStarScore)
        .average()
        .orElse(0.0);
  }

  public List<Product> getProductsSortedByStarScore() {
    return getAllByReviewStarScore();
  }

  public void updateAverageRating(Product product) {
    List<Review> reviews = product.getReviewList();
    if (!reviews.isEmpty()) {
      double totalRating = 0.0;
      for (Review review : reviews) {
        totalRating += review.getStarScore();
      }
      double averageRating = totalRating / reviews.size();
      product = product.toBuilder()
          .averageRating(averageRating)
          .build();
      productRepository.save(product);
    }
  }

  public Page<Product> getProducts(Pageable pageable) {
    return this.productRepository.findAll(pageable);
  }
}
