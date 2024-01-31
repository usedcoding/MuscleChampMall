package com.example.MCM.domain.product.repository;

import com.example.MCM.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findAll(Pageable pageable);

  @Query(
      "select distinct p " +
          "from Product p " +
          "where " +
          " (p.name like %:kw% " +
          " or p.author.username like %:kw%) " +
          " and p.category = 'GOODS'"
  )
  Page<Product> findAllGoodsByKeyword(@Param("kw") String kw, Pageable pageable);

  @Query(
      "select distinct p " +
          "from Product p " +
          "where " +
          " (p.name like %:kw% " +
          " or p.author.username like %:kw%) " +
          " and p.category = 'EQUIPMENT'"
  )
  Page<Product> findAllEquipmentByKeyword(@Param("kw") String kw, Pageable pageable);

  @Query(
      "select distinct p " +
          "from Product p " +
          "where " +
          " (p.name like %:kw% " +
          " or p.author.username like %:kw%) " +
          " and p.category = 'FOOD'"
  )
  Page<Product> findAllFoodByKeyword(@Param("kw") String kw, Pageable pageable);

  @Query("select "
      + "distinct p "
      + "from Product p "
      + "where "
      + "   (p.name like %:kw% "
            + "   or p.description like %:kw% "
      + "   or p.author.username like %:kw% "
      + "   or p.subCategory like %:kw%)"
      + "   and (p.category = :category) ")
  Page<Product> findAllByKeyword(@Param("kw") String kw, @Param("category") String category, Pageable pageable);
}
