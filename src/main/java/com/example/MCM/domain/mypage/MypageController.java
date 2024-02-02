package com.example.MCM.domain.mypage;

import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.service.CartItemService;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.order.service.OrderService;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
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


    public String Mypage(Model model, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        Cart cart = member.getCart();
        List<Review> reviews = this.reviewService.getReviewAll();
        List<CartItem> mycart = this.cartItemService.getAll(cart);
        List<Orders>
    }
}
