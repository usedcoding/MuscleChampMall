package com.example.MCM.domain.product.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<Product> getAll() {
    return this.productRepository.findAll();
  }

  public Product findById(Long id) {
    Optional<Product> product = this.productRepository.findById(id);
    if (product.isPresent()) {
      return product.get();
    } throw new DataNotFoundException("product not found");
  }
}
