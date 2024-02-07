package com.example.MCM.domain.member.controller;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cart.service.CartService;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.service.CartItemService;
import com.example.MCM.domain.email.MailDto;
import com.example.MCM.domain.member.dto.*;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    private final ProductService productService;

    private final CartService cartService;

    private final CartItemService cartItemService;


    //회원가입
    @GetMapping("/signup")
    public String signup(MemberCreateDTO memberCreateDTO) {
        return "signup_form";
    }

    //회원가입
    @PostMapping("/signup")
    public String signup(Model model, @Valid MemberCreateDTO memberCreateDTO, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!memberCreateDTO.getPassword1().equals(memberCreateDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
        }

        try {
            this.memberService.create(memberCreateDTO.getUsername(), memberCreateDTO.getPassword1(),
                    memberCreateDTO.getEmail(), memberCreateDTO.getNickname(), memberCreateDTO.getPhoneNumber());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    //로그인
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    //로그아웃
    @GetMapping("/logout")
    public String Logout(HttpSession session) {
        session.removeAttribute("loggedIn");
        return "redirect:/login";
    }


    //비밀번호 변경
//    @PostMapping("/update/password")
//    public String updatePassword(@Valid MemberPasswordUpdateDTO memberPasswordUpdateDTO, Principal principal, Model model) {
//        // new password 비교
//        if (!Objects.equals(memberPasswordUpdateDTO.getNewPassword(), memberPasswordUpdateDTO.getConfirmPassword())) {
//            model.addAttribute("dto", memberPasswordUpdateDTO);
//            model.addAttribute("differentPassword", "비밀번호가 같지 않습니다.");
//            return "redirect:/member/update/Password";
//        }
//        Member result = memberService.updateMemberPassword(memberPasswordUpdateDTO, principal.getName());
//
//
//        // 현재 비밀번호가 틀렸을 경우
//        if (result == null) {
//            model.addAttribute("dto", memberPasswordUpdateDTO);
//            model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
//            return "redirect:/member/update/Password";
//        }
//
//        return "redirect:/member/me";
//    }

    //개인 정보 변경
    @PostMapping("/update/me")
    public String updateMe(@Valid MemberUpdateDTO memberUpdateDTO, BindingResult bindingResult, Principal principal) {

        Member member = this.memberService.getMember(principal.getName());

        if (bindingResult.hasErrors()) {
            return "mypage/mypage-edit";
        }

        if (!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        //주소 합치는 로직, 자바스크립트에서 해결하면 베스트
        String address =  memberUpdateDTO.getNewAddress().concat(memberUpdateDTO.getExtraAddress());
        String finalAddress = address.concat(memberUpdateDTO.getDetailAddress());

        //최종 데이터베이스 저장 주소 변수는 finalAddress

        this.memberService.updateMember(member,
                memberUpdateDTO.getNewPassword(),
                memberUpdateDTO.getNewNickname(),
                memberUpdateDTO.getNewEmail(),
                memberUpdateDTO.getNewPhoneNumber(),
                finalAddress);

        return "redirect:/member/login";

    }

//    @GetMapping("/delete/{username}")
//    public String delete(@PathVariable(value = "username") String username, Model model) {
//        Member member = this.memberService.getMember(username);
//        if (member.isDeleted() == true) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
//        } else {
//            model.addAttribute("member", member);
//            return "member_delete";
//        }
//    }

    //회원 탈퇴
    @GetMapping("/delete/{username}")
    public String delete(@PathVariable(value = "username") String username,
                         Principal principal,
                         @Valid MemberDeleteDTO memberDeleteDTO,
                         BindingResult bindingResult) {
        Member member = this.memberService.getMember(username);

        if (bindingResult.hasErrors()) {
            return String.format("redirect:/delete/%s", member.getUsername());
        }
        if (!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        if (member.isDeleted() == true) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
        } else {

            if (memberDeleteDTO.getConfirmPassword().equals(member.getPassword())) {
                this.memberService.delete(member);
            } else if (!memberDeleteDTO.getConfirmPassword().equals(member.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
            return "redirect:/";
        }
    }

    //비밀번호 찾기 페이지 이동
    @GetMapping("/findPassword")
    public String findPassword(MailDto mailDto) {
        return "password_find";
    }

    //회원 아이디 찾기 이동
    @GetMapping("/findUsername")
    public String findUsername(MemberFindUsernameDTO memberFindUsernameDTO, Model model) {
        //DTO초기화 코드 없으면 DTO인식이 안됨

        return "username_find";
    }

    //회원 아이디 찾기
    @PostMapping("/findUsername")
    public String findUsername(@Valid MemberFindUsernameDTO memberFindUsernameDTO, BindingResult bindingResult, Model model) {

        //DTO에 있는 데이터 호출 후 저장
        try{
            Member member =  this.memberService.findUsername(memberFindUsernameDTO.getEmail(), memberFindUsernameDTO.getPhoneNumber());
            model.addAttribute("member", member);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            bindingResult.reject("findFailed", "회원 정보를 다시 확인해 주세요.");
            return "username_find";
        } catch (Exception e) {
            bindingResult.reject("Failed", "정보를 다시 확인해 주세요.");
            return "username_find";
        }


        return "username_get";
    }

    @PostMapping("/cart/{id}/{productId}")
    @ResponseBody
    public String addCartItem(@PathVariable("id") Long id,
                              @PathVariable("productId") Long productId,
                              Integer amount) {

        Member member = this.memberService.findById(id);

        Product product = this.productService.findById(productId);

        if (member != null) {

            cartService.addCart(product, member, amount);

            return "success";

        } else {

            return "redirect:/login?message=장바구니%20서비스는%20로그인%20상태에서만%20이용%20가능합니다.";

        }
    }

    @GetMapping("/cart/{id}")
    public String memberCartPage(@PathVariable("id") Long id,
                                 Model model,
                                 Principal principal) {

        Member member = this.memberService.getMember(principal.getName());
        if (member.getId() == id) {

            member = memberService.findById(id);
            // 로그인 되어 있는 유저에 해당하는 장바구니 가져오기
            Cart cart = member.getCart();

            // 장바구니에 들어있는 아이템 모두 가져오기
            List<CartItem> cartItemList = cartItemService.getAll(cart);

            // 장바구니에 들어있는 상품들의 총 가격
            int totalPrice = 0;
            for (CartItem cartitem : cartItemList) {
                totalPrice += cartitem.getCount() * cartitem.getProduct().getPrice();
            }

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", cart.getCount());
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("member", memberService.findById(id));

            return "cart/cart";
        }
        // 로그인 id와 장바구니 접속 id가 같지 않는 경우
        else {
            return "redirect:/main";
        }
    }

    @GetMapping("/cart/{id}/{cartItemId}/delete")
    public String deleteCartItem(@PathVariable("id") Long id,
                                 @PathVariable("cartItemId") Long itemId,
                                 Model model,
                                 Principal principal) {
        Member member = this.memberService.getMember(principal.getName());
        if (member.getId().equals(id)) {
            // itemId로 장바구니 상품 찾기
            CartItem cartItem = cartService.findCartItemById(itemId);

            // 장바구니 물건 삭제
            cartItemService.cartItemDelete(itemId);

            // 해당 유저의 카트 찾기
            Cart cart = cartService.getCartByMemberId(id);

            // 해당 유저의 장바구니 상품들
            List<CartItem> cartItemList = cartService.getAllMemberCart(cart);

            // 총 가격 += 수량 * 가격
            int totalPrice = 0;
            for (CartItem cartitem : cartItemList) {
                totalPrice += cartitem.getCount() * cartitem.getProduct().getPrice();
            }

            cart = cart.toBuilder()
                    .count(cart.getCount() - cartItem.getCount())
                    .build();
            this.cartService.saveCart(cart);

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", cart.getCount());
            model.addAttribute("cartItems", cartItemList);
            model.addAttribute("member", memberService.findById(id));

            return "redirect:/member/cart/{id}";
        }
        // 로그인 id와 장바구니 삭제하려는 유저의 id가 같지 않는 경우
        else {
            return "redirect:/main";
        }
    }
}
