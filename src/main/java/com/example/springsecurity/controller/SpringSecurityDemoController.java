package com.example.springsecurity.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@EnableGlobalMethodSecurity(securedEnabled = true)
//@RequestMapping("spring-security")
public class SpringSecurityDemoController{

 Logger logger = LoggerFactory.getLogger(getClass());

 public static final String  SESSION_KEY = "SESSION_KEY_IMAGE_CODE";


  @Autowired
  private DefaultKaptcha defaultKaptcha;

  /**
   * 跳转登录页面
   * @return
   */
  @GetMapping("/showLogin")
    public String showLogin(){
      return "login";
  }

  /**
   * 登录成功页面
   * @return
   */
  @Secured("ROLE_manager")//角色包含ROLE_manager的才能访问
  @RequestMapping("/loginSuccess")
    public ModelAndView loginSuccess(){
      return new ModelAndView("success");
  }

  /**
   * 登录失败页面
   * @return
   */
  @RequestMapping("/loginFail")
  public ModelAndView loginFail(){
    return new ModelAndView("fail");
  }

  /**
   * 获取图形验证码
   * @param request
   * @param response
   * @throws IOException
   */
  @RequestMapping("/code/image")
  public void imageCode(HttpServletRequest request, HttpServletResponse response)throws IOException{
    //获取验证码字符串
    String code = defaultKaptcha.createText();
    logger.info("验证码：{}",code);
    //将验证码存入session
    request.getSession().setAttribute(SESSION_KEY,code);
    //绘制验证码
    BufferedImage image = defaultKaptcha.createImage(code);
    //输出验证码
    ServletOutputStream out = response.getOutputStream();
    ImageIO.write(image,"jpg",out);
  }


}
