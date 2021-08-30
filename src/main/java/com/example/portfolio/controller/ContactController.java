package com.example.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

  @GetMapping("")
  public String contact(){
    return "contact/contact";
  }
}
