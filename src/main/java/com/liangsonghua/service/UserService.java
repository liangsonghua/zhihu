package com.liangsonghua.service;

import com.liangsonghua.dao.LoginTicketDAO;
import com.liangsonghua.dao.UserDAO;
import com.liangsonghua.model.LoginTicket;
import com.liangsonghua.model.User;
import com.liangsonghua.util.ZhihuUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liangsonghua on 2016/7/2.
 */
@Service
public class UserService  {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String,String> regirster(String username,String password) {
         Map<String,String> map = new HashMap<String, String>();
            if (StringUtils.isBlank(username)) {
                    map.put("msg", "用户名不能为空");
                    return map;
            }

            if (StringUtils.isBlank(password)) {
                    map.put("msg", "密码不能为空");
                    return map;
            }
            User user = userDAO.selectByName(username);
            if(user!= null) {
                map.put("msg","用户名已经注册");
                return map;
            }
            user = new User();
            user.setName(username);
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setPassword(ZhihuUtil.MD5(password + user.getSalt()));
            String head = String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
            user.setHeadUrl(head);
            userDAO.addUser(user);
            String ticket = addLoginTicket(user.getId());
            map.put("ticket",ticket);
            return  map;
    }
        public Map<String,String> login(String username,String password) {
                Map<String,String> map = new HashMap<String, String>();
                if (StringUtils.isBlank(username)) {
                        map.put("msg", "用户名不能为空");
                        return map;
                }

                if (StringUtils.isBlank(password)) {
                        map.put("msg", "密码不能为空");
                        return map;
                }
                User user = userDAO.selectByName(username);
                if(user== null) {
                        map.put("msg","用户名不存在");
                        return map;
                }
                if(!ZhihuUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
                        map.put("msg", "密码不正确");
                        return map;
                }
                String ticket = addLoginTicket(user.getId());
                map.put("ticket", ticket);
                return  map;
        }
    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public String addLoginTicket(int userId) {
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUserId(userId);
            Date now = new Date();
            now.setTime(3600*24*100+now.getTime());
            loginTicket.setExpired(now);
            loginTicket.setStatus(0);
            loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
            loginTicketDAO.addTicket(loginTicket);
            return loginTicket.getTicket();
    } 
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
