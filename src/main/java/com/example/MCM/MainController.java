package com.example.MCM;

import com.example.MCM.domain.community.entity.Post;
import com.example.MCM.domain.community.service.PostService;
import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.service.NoticeService;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;

    private final NoticeService noticeService;

    private final PostService postService;

    @GetMapping("/")
    public String main(Model model) {

        List<Product> productList = this.productService.getAll();

        List<Notice> noticeList = this.noticeService.getAll();

        List<Post> postList = this.postService.getAll();

        model.addAttribute("productList", productList);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("postList", postList);

        return"main";
    }
}
