package com.znz.controller;

import com.alibaba.fastjson.JSON;
import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.listener.MySessionLister;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.Constants;
import com.znz.util.IPUtil;
import com.znz.vo.UserLoginVO;
import com.znz.vo.UserSession;
import com.znz.vo.WatermarkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/1/27.
 */

@Controller
@Slf4j
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @RequestMapping(value = "/login" , method= RequestMethod.POST)
    public String login(HttpServletRequest request,HttpServletResponse response, @Valid @ModelAttribute("userLoginVO") UserLoginVO userLoginVO,BindingResult br,Model model) {
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/index";
        }
        String randCode = (String)request.getSession().getAttribute("randCode");
        if(!userLoginVO.getRandomCode().equals(randCode)){
            model.addAttribute("error", "验证码错误");
            return  "/index";
        }
        String userName = userLoginVO.getUserName();
        String pwd = userLoginVO.getPwd();
        //System.out.println(userName+":"+pwd);
        User user =  userMapper.selectByUser(userName);
        if(user == null || !pwd.equals(user.getPwd())){
            model.addAttribute("error", "用户或名密码不正确");
            return  "/index";
        }
        //账号密码验证正确
        int limitIpFlag = user.getLimitIpFlag();
        //鉴权IP
        if(1==limitIpFlag && !StringUtils.isEmpty(user.getLimitIps()) && !user.getLimitIps().contains(IPUtil.getIpAddr(request))){
            model.addAttribute("error", "IP鉴权不通过");
            return  "/index";
        }
        user.setUpdateTime(new Date());
        user.setLimitIps(request.getRemoteHost());
        boolean islogin = false;
        if(StringUtils.isEmpty(user.getSessionId())){
            user.setSessionId(request.getSession().getId());
        }
        islogin =   MySessionLister.replaceSession(request.getSession(), user.getSessionId());
        if(islogin){
            user.setSessionId(request.getSession().getId());//如果已经登陆，覆盖之前的sessionId
        }
        List<UserAuth> userAuths = userAuthMapper.listByUserId(user.getUserId());
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setUserAuths(userAuths);
        if(!StringUtils.isEmpty(user.getWatermark())){
            try{
                WatermarkVO watermarkVO = JSON.parseObject(user.getWatermark(),WatermarkVO.class);
                String watermarkParam ="";
                if(!StringUtils.isEmpty(watermarkVO.getImage())){
                    if(watermarkVO.getP()!=null){
                        String img = watermarkVO.getImage()+"?x-oss-process=image/resize,P_"+watermarkVO.getP();
                        String base64 = safeUrlBase64Encode(img.getBytes("UTF-8"));
                        watermarkParam+="/watermark,image_"+base64;
                    }else {
                        watermarkParam+="/watermark,image_"+safeUrlBase64Encode(watermarkVO.getImage().getBytes());
                    }
                    if(!watermarkParam.equals("") && watermarkVO.getT()!=null){
                        watermarkParam+=",t_"+watermarkVO.getT();
                    }
                    if(!watermarkParam.equals("") && watermarkVO.getG()!=null){
                        watermarkParam+=",g_"+watermarkVO.getG();
                    }
                    if(!watermarkParam.equals("") && watermarkVO.getX()!=null){
                        watermarkParam+=",x_"+watermarkVO.getX();
                    }
                    if(!watermarkParam.equals("") && watermarkVO.getY()!=null){
                        watermarkParam+=",y_"+watermarkVO.getY();
                    }
                }

                request.getSession().setAttribute(Constants.WATERMARK_PARAM,watermarkParam);
            }catch (Exception e){
                log.error(e.getLocalizedMessage(),e);
            }

        }
        user.setLastLoginTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        request.getSession().setAttribute(Constants.USER_SESSION,userSession);
        if(1==userLoginVO.getRemember()){
            String uname = "";
            try {
                uname = URLEncoder.encode(user.getUserName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Cookie cookie = new Cookie("znzauth",uname+"-"+user.getPwd());
            try {
                response.addCookie(cookie);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }else{
            Cookie cookie = new Cookie("znzauth","");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
        MySessionLister.setActiveSessions(MySessionLister.getActiveSessions() + 1);

        return  "redirect:/admin/subCategory/showCategory";

    }


    @RequestMapping(value = "/logout" , method= RequestMethod.GET)
    public String login(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.USER_SESSION);
        MySessionLister.removeSession(request.getSession().getId());
        request.getSession().invalidate();
        return "redirect:/";
    }

    public static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        safeBase64Str = safeBase64Str.replaceAll("\r\n", "");
        safeBase64Str = safeBase64Str.replaceAll("\n", "");
        return safeBase64Str;
    }



}
