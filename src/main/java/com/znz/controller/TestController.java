package com.znz.controller;

import com.znz.model.User;
import com.znz.vo.AuthFileVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class TestController {

    ThreadLocal threadLocal = new ThreadLocal();

    ExecutorService executorService = Executors.newFixedThreadPool(500);

    @RequestMapping(value = "/test-thread", method = RequestMethod.GET)
    public @ResponseBody
    String  testThread(HttpServletRequest request) {
        executorService.execute(()->{
            for(int i=0;i<1000;i++){
                threadLocal.set(addUser());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return "ok";
    }

    List<User> addUser(){
        List<User> users = new ArrayList<>();
        for(int i=0;i<10000;i++){
            User user = new User();
            user.setUserId(i);
            users.add(user);
        }
        return users;
    }
}
