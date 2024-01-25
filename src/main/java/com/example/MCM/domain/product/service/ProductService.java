package com.example.MCM.domain.product.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.product.dto.ProductDto;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  public Product findById(Long id) {
    Optional<Product> product = this.productRepository.findById(id);
    if (product.isPresent()) {
      return product.get();
    } throw new DataNotFoundException("product not found");
  }

  @Transactional
  public Product create(ProductDto productDto, List<MultipartFile> files) throws IOException {

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
          .imgPath(filePaths)
          .imgName(fileNames)
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
}
