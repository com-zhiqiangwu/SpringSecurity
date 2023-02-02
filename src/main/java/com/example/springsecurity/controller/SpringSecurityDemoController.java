package com.example.springsecurity.controller;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RequestMapping("spring-security")
public class SpringSecurityDemoController{

  @RequestMapping("/showLogin")
    public String showLogin(){
      return "login";
  }
  @RequestMapping("/loginSuccess")
    public String loginSuccess(){
      return "success";
  }

  @RequestMapping("/loginFail")
  public String loginFail(){
    return "fail";
  }
}
