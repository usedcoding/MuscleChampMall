package com.example.MCM.domain.community.controller;

import com.example.MCM.domain.community.PostCreateDTO;
import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.community.service.PostService;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostController {

    private final PostService postService;

    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createPost(PostCreateDTO postCreateDTO) {
        return"post_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createPost(@Valid PostCreateDTO postCreateDTO, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()) {
            return "post_create";
        }

        Member member = this.memberService.getMember(principal.getName());

        this.postService.createPost(postCreateDTO.getTitle(), postCreateDTO.getContent(), member);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        Post post = this.postService.getPost(id);

        model.addAttribute("post", post);

        if (!post.getAuthor().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        this.postService.deletePost(post);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyPost(@PathVariable(value = "id") Long id, PostCreateDTO postCreateDTO, Model model, Principal principal) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);

        if(!post.getAuthor().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        return"post_modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyPost(@PathVariable(value = "id") Long id, @Valid PostCreateDTO postCreateDTO, BindingResult bindingResult, Model model, Principal principal) {
        Post post = this.postService.getPost(id);

        model.addAttribute("post", post);

        if(bindingResult.hasErrors()) {
            return "post_modify";
        }

        if(!post.getAuthor().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        this.postService.modifyPost(post, postCreateDTO.getTitle(), postCreateDTO.getContent());

        return "redirect:/";
    }



}



