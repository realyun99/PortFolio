package com.example.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class HomeController {

  @GetMapping("")
  public String main() {
    return "home";
  }

  @GetMapping("/about-me")
  public String resume() { return "about-me/list"; }
}
