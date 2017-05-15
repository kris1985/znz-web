package com.znz.controller;

import com.alibaba.fastjson.JSON;
import com.znz.dao.SubCategoryMapper;
import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.exception.ServiceException;
import com.znz.listener.MySessionLister;
import com.znz.model.SubCategory;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.AESUtil;
import com.znz.util.Constants;
import com.znz.util.DESUtil;
import com.znz.vo.*;
import com.znz.util.IPUtil;
import com.znz.vo.UserLoginVO;
import com.znz.vo.UserSession;
import com.znz.vo.WatermarkVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2015/1/27.
 */

@Controller
@Slf4j
public class LoginController {

    public static final String EKY = "b868a6c1-e554-4f";
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private SubCategoryMapper subCategoryMapper;

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
        if(!"WEB".equalsIgnoreCase(user.getDevice())){
            model.addAttribute("error", "该账号只能在手机端使用");
            return  "/index";
        }
        //账号密码验证正确
        int limitIpFlag = user.getLimitIpFlag();
        //鉴权IP
        String ip = IPUtil.getIpAddr(request);
        if(1==limitIpFlag && !StringUtils.isEmpty(user.getLimitIps()) && !user.getLimitIps().contains(ip)){
            model.addAttribute("error", "IP鉴权不通过");
            return  "/index";
        }
        user.setUpdateTime(new Date());
        user.setLimitIps(ip);
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
                watermarkParam = getWaterMark(watermarkVO, watermarkParam);

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
       // MySessionLister.setActiveSessions(MySessionLister.getActiveSessions() + 1);

        return  "redirect:/admin/subCategory/showCategory";

    }

    private String getWaterMark(WatermarkVO watermarkVO, String watermarkParam) throws UnsupportedEncodingException {
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
        return watermarkParam;
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

    @RequestMapping(value = "/verify" , method= RequestMethod.GET)
    public @ResponseBody CommonResponse verify(HttpServletRequest request) {
        String encParam = request.getQueryString();
        log.info("signIn request param:{}",encParam);
        CommonResponse response = new CommonResponse<>();
        try{
            String decr = AESUtil.Decrypt(URLDecoder.decode(encParam.replace("param=",""),"utf-8"), EKY);
            String temp1 = new String(decr.getBytes("ISO-8859-1"),"UTF-8");
            String userName = decr.split("\\|")[0];
            String pwd = decr.split("\\|")[1];
            String imei = decr.split("\\|")[2];
            User user =  userMapper.selectByUser(userName);
            checkUser(pwd, imei, user);
            response.setSuccess(true);
        } catch (ServiceException e){
            response.setErrorCode(e.getErrCode());
            response.setErrorMsg(e.getErrReason());
        } catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
            response.setErrorCode("999");
            response.setErrorMsg("系统忙请稍后再试");
        }
        log.info("signIn response:{}",response);
        return response;
    }

    private void checkUser(String pwd, String imei, User user) {
        if(user==null){
            throw new ServiceException("100","账号不存在");
        }
        if(StringUtils.isEmpty(imei)){
            throw new ServiceException("101","imei不能为空");
        }
        if(!StringUtils.isEmpty(user.getImei()) && !imei.equals(user.getImei())){
            throw new ServiceException("104","该账户只能在绑定的电脑上登陆");
        }
        if(!"WEB".equalsIgnoreCase(user.getDevice())){
            throw new ServiceException("103","该账号只能在PC端使用");
        }
        if(user == null || !pwd.equals(user.getPwd())){
            throw new ServiceException("102","用户名或密码不正确");
        }
    }



    @RequestMapping(value = "/register" , method= RequestMethod.GET)
    public String register(HttpServletRequest request,HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String encParam = request.getQueryString();
        log.info("signIn request param:{}",encParam);
        String decr = AESUtil.Decrypt(URLDecoder.decode(encParam.replace("param=",""),"utf-8"), EKY);
        String temp1 = new String(decr.getBytes("ISO-8859-1"),"UTF-8");
        String userName = decr.split("\\|")[0];
        String pwd = decr.split("\\|")[1];
        String imei = decr.split("\\|")[2];
        User user =  userMapper.selectByUser(userName);//userName:userName=&pwd=&imei=40-8D-5C-FF-F8-23
        checkUser(pwd,imei,user);
        List<UserAuth> userAuths = userAuthMapper.listByUserId(user.getUserId());
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setUserAuths(userAuths);
        if(!StringUtils.isEmpty(user.getWatermark())){
            try{
                WatermarkVO watermarkVO = JSON.parseObject(user.getWatermark(),WatermarkVO.class);
                String watermarkParam ="";
                watermarkParam = getWaterMark(watermarkVO, watermarkParam);

                request.getSession().setAttribute(Constants.WATERMARK_PARAM,watermarkParam);
            }catch (Exception e){
                log.error(e.getLocalizedMessage(),e);
            }
        }
        user.setLastLoginTime(new Date());
        user.setImei(imei);
        userMapper.updateByPrimaryKeySelective(user);
        request.getSession().setAttribute(Constants.USER_SESSION,userSession);
        Cookie cookie = new Cookie("MAC","FDSGHJ3");
        response.addCookie(cookie);
        return  "redirect:/admin/subCategory/showCategory";
    }

    @RequestMapping(value = "/signIn" , method= RequestMethod.POST)
    public CommonResponse signIn(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return  null;
    }
}
