package com.example.MCM.domain.mypage;

import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.service.CartItemService;
import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.community.service.PostService;
import com.example.MCM.domain.member.dto.MemberUpdateDTO;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.order.entity.Orders;
import com.example.MCM.domain.order.service.OrderService;
import com.example.MCM.domain.product.service.ProductService;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MemberService memberService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final PostService postService;



    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public String Mypage(){
        return "mypage/mypage-main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String MypageUSer(Model model, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        Cart cart = member.getCart();
        List<Review> reviews = this.reviewService.getReviewAll();
        List<CartItem> mycart = this.cartItemService.getAll(cart);
        List<Orders> myOrders = this.orderService.getByBuyerId(member.getId());

        if (mycart == null) {
            mycart = Collections.emptyList();
        }

        model.addAttribute("member", member);
        model.addAttribute("reviews", reviews);
        model.addAttribute("mycart", mycart);
        model.addAttribute("myOrders", myOrders);

        return "mypage/mypage-me";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order")
    public String MypageOrder(Model model, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        Cart cart = member.getCart();
        List<Review> reviews = this.reviewService.getReviewAll();
        List<CartItem> mycart = this.cartItemService.getAll(cart);
        List<Orders> myOrders = this.orderService.getByBuyerId(member.getId());

        if (mycart == null) {
            mycart = Collections.emptyList();
        }

        model.addAttribute("member", member);
        model.addAttribute("reviews", reviews);
        model.addAttribute("mycart", mycart);
        model.addAttribute("myOrders", myOrders);

        return "mypage/mypage-order";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart")
    public String MypageCart(Model model, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        Cart cart = member.getCart();
        List<Review> reviews = this.reviewService.getReviewAll();
        List<CartItem> mycart = this.cartItemService.getAll(cart);
        List<Orders> myOrders = this.orderService.getByBuyerId(member.getId());

        if (mycart == null) {
            mycart = Collections.emptyList();
        }

        model.addAttribute("member", member);
        model.addAttribute("reviews", reviews);
        model.addAttribute("mycart", mycart);
        model.addAttribute("myOrders", myOrders);

        return "mypage/mypage-cart";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post")
    public String MypagePost(Model model, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        Cart cart = member.getCart();
        List<Post> posts = this.postService.getAllByAuthorId(member.getId());
        List<CartItem> mycart = this.cartItemService.getAll(cart);
        List<Orders> myOrders = this.orderService.getByBuyerId(member.getId());

        if (mycart == null) {
            mycart = Collections.emptyList();
        }

        model.addAttribute("member", member);
        model.addAttribute("posts", posts);
        model.addAttribute("mycart", mycart);
        model.addAttribute("myOrders", myOrders);

        return "mypage/mypage-post";
    }

    @GetMapping("/edit")
    public String MypageEdit(MemberUpdateDTO memberUpdateDTO){
        return "mypage/mypage-edit";
    }



}
