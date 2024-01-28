package com.example.MCM.domain.order.controller;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.order.entity.Order;
import com.example.MCM.domain.order.service.OrderService;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  private final MemberService memberService;

  @Value("${custom.paymentSecretKey}")
  private String paymentSecretKey;

  private final ProductService productService;

  @GetMapping("/{id}")
  public String detail(Model model,
                       @PathVariable("id") Long id,
                       @RequestParam(value = "orderId") String orderId,
                       @RequestParam(value = "amount") Integer amount,
                       @RequestParam(value = "paymentKey") String paymentKey,
                       Principal principal) throws Exception {
    String secretKey = paymentSecretKey;

    Base64.Encoder encoder = Base64.getEncoder();
    byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
    String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

    URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("Authorization", authorizations);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    JSONObject obj = new JSONObject();
    obj.put("orderId", orderId);
    obj.put("amount", amount);

    OutputStream outputStream = connection.getOutputStream();
    outputStream.write(obj.toString().getBytes("UTF-8"));

    int code = connection.getResponseCode();
    boolean isSuccess = code == 200 ? true : false;
    model.addAttribute("isSuccess", isSuccess);

    InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

    Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
    JSONParser parser = new JSONParser();
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    responseStream.close();
    model.addAttribute("responseStr", jsonObject.toJSONString());
    System.out.println(jsonObject.toJSONString());

    model.addAttribute("method", (String) jsonObject.get("method"));
    model.addAttribute("orderName", (String) jsonObject.get("orderName"));

    if (((String) jsonObject.get("method")) != null) {
      if (((String) jsonObject.get("method")).equals("카드")) {
        model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
      } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
        model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
      } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
        model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
      } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
        model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
      }
    } else {
      model.addAttribute("code", (String) jsonObject.get("code"));
      model.addAttribute("message", (String) jsonObject.get("message"));
    }

    Member member = this.memberService.getMember(principal.getName());
    Product product = this.productService.findById(id);
    Order order = this.orderService.createOrder(member, product);

    model.addAttribute("member", member);
    model.addAttribute("product", product);
    model.addAttribute("order", order);
//    this.cashService.addCashLog(member, member.getLastName(), member.getFirstName(), product, product.getTitle(), amount);
    return "order/success";
  }


  @GetMapping("/fail")
  public String paymentResult(Model model, @RequestParam(value = "message") String message, @RequestParam(value = "code") Integer code) throws Exception {

    model.addAttribute("code", code);
    model.addAttribute("message", message);

    return "order/fail";
  }

}
