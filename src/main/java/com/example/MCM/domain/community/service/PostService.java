package com.example.MCM.domain.community.service;

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
        return post.get();
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


    //좋아요
    public void like(Post post, Member member) {
        post.getLike().add(member);
        this.postRepository.save(post);
    }

    //좋아요 삭제
    public void removeLike(Post post, Member member) {
        post.getLike().remove(member);
        this.postRepository.save(post);
    }

    //싫어요
    public void disLike(Post post, Member member) {
        post.getDisLike().add(member);
        this.postRepository.save(post);
    }

    //싫어요 삭제
    public void removeDisLike(Post post, Member member) {
        post.getDisLike().remove(member);
        this.postRepository.save(post);
    }


}