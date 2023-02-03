package com.example.springsecurity.controller;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
//@RequestMapping("spring-security")
public class SpringSecurityDemoController{

  @RequestMapping("/showLogin")
    public ModelAndView showLogin(){
      return new ModelAndView("login");
  }
  @RequestMapping("/loginSuccess")
    public ModelAndView loginSuccess(){
      return new ModelAndView("success");
  }

  @RequestMapping("/loginFail")
  public ModelAndView loginFail(){
    return new ModelAndView("fail");
  }
}
