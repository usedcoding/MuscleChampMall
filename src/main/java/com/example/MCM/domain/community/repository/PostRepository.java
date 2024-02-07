package com.example.MCM.domain.community.repository;

import com.example.MCM.domain.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAll(Pageable pageable);


  List<Post> findAllByAuthorId(Long id);
}
