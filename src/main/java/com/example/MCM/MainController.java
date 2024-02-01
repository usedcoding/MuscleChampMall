package com.example.MCM;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main1() {
        return"main";
    }

    @GetMapping("/main")
    public String main2() {
        return"main";
    }
}
