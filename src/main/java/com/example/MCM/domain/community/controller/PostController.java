package com.example.MCM.domain.community.controller;

import com.example.MCM.domain.community.PostCreateDTO;
import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.community.service.PostService;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final MemberService memberService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> paging = this.postService.getList(page);
        model.addAttribute("paging", paging);
        return "community/post_list";
    }

    //게시글 생성으로 이동
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createPost(PostCreateDTO postCreateDTO) {
        return"community/post_create";
    }

    //게시글 생성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createPost(@Valid PostCreateDTO postCreateDTO, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()) {
            return "community/post_create";
        }

        Member member = this.memberService.getMember(principal.getName());

        this.postService.createPost(postCreateDTO.getTitle(), postCreateDTO.getContent(), member);

        return "redirect:/post/list";
    }

    //게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        Post post = this.postService.getPost(id);

        model.addAttribute("post", post);

        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        this.postService.deletePost(post);

        return "redirect:/post/list";
    }

    //게시글 수정으로 이동
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyPost(@PathVariable(value = "id") Long id, PostCreateDTO postCreateDTO, Model model, Principal principal) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);

        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        return"/community/post_modify";
    }

    //게시글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyPost(@PathVariable(value = "id") Long id, @Valid PostCreateDTO postCreateDTO, BindingResult bindingResult, Model model, Principal principal) {
        Post post = this.postService.getPost(id);

        model.addAttribute("post", post);

        if(bindingResult.hasErrors()) {
            return "/community/post_modify";
        }

        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        this.postService.modifyPost(post, postCreateDTO.getTitle(), postCreateDTO.getContent());

        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String like(@PathVariable(value = "id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        Member member = this.memberService.getMember(principal.getName());
//        Set<Member> memberSet = post.getLike();
//
//        boolean isLikedPost = false;
//        for (Member likedMember : memberSet) {
//            if (member.getId() == likedMember.getId()) {
//                isLikedPost = true;
//            }
//        }
//
//        if (isLikedPost == false) {
//           this.postService.like(post,member);
//        } else {
//        }

        if (!post.getLike().contains(member)) {
            postService.like(post, member);
        } else if (post.getLike().contains(member)) {
            postService.removeLike(post, member);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");
        }
        return String.format("redirect:/post/detail/%d", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/disLike/{id}")
    public String disLike(@PathVariable(value = "id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        Member member = this.memberService.getMember(principal.getName());

        if (!post.getDisLike().contains(member)) {
            postService.disLike(post, member);
        } else if (post.getDisLike().contains(member)) {
            postService.removeDisLike(post, member);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");
        }
        return String.format("redirect:/post/detail/%d", id);
    }

    //상세 페이지로 이동
    @GetMapping("/detail/{id}")
    public String detailPost(@PathVariable(value = "id")Long id, Model model) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);

        return"community/post_detail";
    }

}



