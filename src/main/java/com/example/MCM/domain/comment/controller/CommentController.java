
package com.example.MCM.domain.comment.controller;

import com.example.MCM.domain.comment.CommentCreateDTO;
import com.example.MCM.domain.comment.entity.Comment;
import com.example.MCM.domain.comment.service.CommentService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(@PathVariable(value = "id") Long id, @Valid CommentCreateDTO commentCreateDTO, BindingResult bindingResult, Model model, Principal principal) {
        Post post = this.postService.getPost(id);

        Member member = this.memberService.getMember(principal.getName());

        if (principal == null) {
            return "redirect:/member/login";
        }

        if (bindingResult.hasErrors()) {
            return String.format("redirect:/post/detail/%s", post.getId());
        }

        this.commentService.createComment(commentCreateDTO.getContent(), member, post);

        return String.format("redirect:/post/detail/%s", post.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable(value = "id") Long id, Principal principal) {

        Comment comment = this.commentService.getComment(id);

        if (comment.getAuthor().getUsername().equals(principal.getName())) {
            this.commentService.deleteComment(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        return String.format("redirect:/post/detail/%d", comment.getPost().getId());
    }

    @PostMapping("/modify/{id}")
    @ResponseBody
    public String modifyComment(@PathVariable(value = "id") Long id, @RequestParam(value = "content") String content, Principal principal) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 수정 가능합니다.");
        }

        if (content == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "내용이 없습니다.");
        }

        this.commentService.modifyComment(comment, content);

        return content;
    }

}