package com.example.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {

  @GetMapping("")
  public String contact(){
    return "resume/list";
  }
}
