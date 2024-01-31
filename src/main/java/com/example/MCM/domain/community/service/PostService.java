package com.example.MCM.domain.community.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.community.repository.PostRepository;
import com.example.MCM.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    //게시글 리스트
    public List<Post> postList() {
        return this.postRepository.findAll();
    }

    //게시글 호출
    public Post getPost(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("post not found");
        }
    }

    //게시글 생성
    public Post createPost(String title, String content, Member author) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        this.postRepository.save(post);

        return post;
    }

    //게시글 삭제
    public void deletePost(Post post) {
        this.postRepository.delete(post);
    }

    //게시글 수정
    public Post modifyPost(Post post, String title, String content) {
        Post modifyPost = post.toBuilder()
                .title(title)
                .content(content)
                .build();

        this.postRepository.save(modifyPost);

        return post;
    }


}