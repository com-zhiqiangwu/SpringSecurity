package com.example.springsecurity.service.impl;

import com.example.springsecurity.dao.UserTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserTest userTest;
    //根据用户名加载用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //从配置文件中获取账号密码 来模拟数据库数据
        String testUsername = userTest.getUsername();
        //从数据库直接获取加密后的密码，下面就无需再加密
        String testPassword = userTest.getPassword();

        //1.模拟从数据库查询用户
        if (!(username.equals("admin"))){
            //如果不是admin，说明不是我们数据库中的用户，抛出异常或进行逻辑处理
            throw  new UsernameNotFoundException("用户名不存在！");
        }
        //2.模拟从数据库查询到了用户信息，用户存在的逻辑
        //2.1封装密码
        //模拟从数据库取出加密后的密码
        String password = "pwd";
        //2.11对密码进行加密处理，否则启动，访问报错
        String encodePassword = passwordEncoder.encode(password);
        System.out.println("加密后---------"+encodePassword);
        //2.12判断原字符加密后和内容是否匹配
        boolean matches = passwordEncoder.matches(password, testPassword);
        System.out.println("原密码和加密后是否匹配-----"+matches);
        //2.2封装权限 假设这个用户查到了两个权限字符  admin1和admin2
        //2.3 拼接权限 循环拼接
        //2.4因为权限字符拼接非常麻烦所以spring security提供了AuthorityUtils工具类，
        // 帮我们封装参数就是权限字符通过逗号分隔，比如”admin1,admin2“
        List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,admin1,admin2,ROLE_manager,ROLE_user");
        //3返回UserDetail
        UserDetails user = new User(username, testPassword, grantedAuthorityList);
        return user;
    }
}
