package com.example.MCM.domain.product.repository;

import com.example.MCM.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findAll(Pageable pageable);

  @Query(
      "select distinct p " +
          "from Product p " +
          "where " +
          " p.name like %:kw% " +
          " and p.category = 'GOODS'"
  )
  Page<Product> findAllGoodsByKeyword(@Param("kw") String kw, Pageable pageable);

  @Query(
      "select distinct p " +
          "from Product p " +
          "where " +
          " p.name like %:kw% " +
          " and p.category = 'EQUIPMENT'"
  )
  Page<Product> findAllEquipmentByKeyword(@Param("kw") String kw, Pageable pageable);

  @Query(
      "select distinct p " +
          "from Product p " +
          "where " +
          " p.name like %:kw% " +
          " and p.category = 'FOOD'"
  )
  Page<Product> findAllFoodByKeyword(@Param("kw") String kw, Pageable pageable);

  @Query("select "
      + "distinct p "
      + "from Product p "
      + "where "
      + "   (p.name like %:kw% "
      + "   or p.description like %:kw% "
      + "   or p.subCategory like %:kw%)"
      + "   and (p.category = :category) ")
  Page<Product> findAllByKeyword(@Param("kw") String kw, @Param("category") String category, Pageable pageable);

  @Query("select "
      + "distinct p "
      + "from Product p "
      + "where "
      + "   (p.name like %:kw% "
      + "   or p.description like %:kw%) "
      + "   and p.category = 'GOODS' "
      + "   and p.subCategory = :subCategory"
  )
  Page<Product> findAllGoodsByKeywordAndSubCategory(@Param("kw") String kw, Pageable pageable, @Param("subCategory") String subCategory);

  @Query("select "
      + "distinct p "
      + "from Product p "
      + "where "
      + "   (p.name like %:kw% "
      + "   or p.description like %:kw% "
      + "   or p.subCategory like %:kw%)"
      + "   and p.category = 'EQUIPMENT' "
      + "   and p.subCategory = :subCategory"
  )
  Page<Product> findAllEquipmentByKeywordAndSubCategory(@Param("kw") String kw, Pageable pageable, @Param("subCategory") String subCategory);

  @Query("select "
      + "distinct p "
      + "from Product p "
      + "where "
      + "   (p.name like %:kw% "
      + "   or p.description like %:kw% "
      + "   or p.subCategory like %:kw%)"
      + "   and p.category = 'FOOD' "
      + "   and p.subCategory = :subCategory"
  )
  Page<Product> findAllFoodByKeywordAndSubCategory(@Param("kw") String kw, Pageable pageable, @Param("subCategory") String subCategory);

  @Query("SELECT DISTINCT p.subCategory FROM Product p WHERE p.category = :category")
  List<String> findSubCategoriesByCategory(@Param("category") String category);
}
