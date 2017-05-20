package com.znz.controller;

import com.alibaba.fastjson.JSON;
import com.znz.config.AppConfig;
import com.znz.dao.PictureMapper;
import com.znz.dao.SubCategoryMapper;
import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.exception.ServiceException;
import com.znz.model.Picture;
import com.znz.model.SubCategory;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.CategoryUtil;
import com.znz.util.SignUtil;
import com.znz.util.WaterMarkUtil;
import com.znz.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/5/9.
 */
@Controller
@Slf4j
public class MobileController {

    public static final String SALT = LoginController.EKY;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private SubCategoryMapper subCategoryMapper;
    @Resource
    private PictureMapper pictureMapper;
    @Resource
    private AppConfig appConfig;

    @RequestMapping(value = "/signIn" , method= RequestMethod.POST)
    public @ResponseBody CommonResponse<UserInfo> signIn(@RequestBody BaseRequest<SignInRequest> baseRequest) {
        log.info("signIn request:{0}",baseRequest);
        CommonResponse<UserInfo> commonResponse = new CommonResponse();
        try{
            SignInRequest signInRequest = baseRequest.getData();
            checkSign(baseRequest);
            User user =  userMapper.selectByUser(signInRequest.getUserName());
            checkUser(signInRequest.getPassword(), baseRequest.getImei(), user);
            String token = new Md5PasswordEncoder().encodePassword(baseRequest.getImei()+System.currentTimeMillis(),SALT);
            user.setToken(token);
            user.setImei(baseRequest.getImei());
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            UserInfo userInfo = new UserInfo();
            userInfo.setToken(token);
            userInfo.setUserName(user.getUserName());
            userInfo.setRecommend(user.getRecommendFlag()==null?false:user.getRecommendFlag()==1);
            commonResponse.setResult(userInfo);
        }catch (ServiceException e){
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }
        return commonResponse;
    }

    @RequestMapping(value = "/categorys" , method= RequestMethod.POST)
    public @ResponseBody CommonResponse<List<CategoryInfo>> category(@RequestBody BaseRequest baseRequest) {
        CommonResponse<List<CategoryInfo>> commonResponse = new CommonResponse();
        List<CategoryInfo> categoryInfos = new ArrayList<>();
        try{
            checkSign(baseRequest);
            checkToken(baseRequest.getToken());
            User user = getUserByToken(baseRequest);
            commonResponse.setResult(categoryInfos);
            List<UserAuth> userAuths =  userAuthMapper.listByUserId(user.getUserId());
            if(CollectionUtils.isEmpty(userAuths)){
                return commonResponse;
            }
            Set<Integer> ids = userAuths.stream().map(s->Integer.parseInt(s.getAuthId())).collect(Collectors.toSet());
            List<SubCategory> allcategories = subCategoryMapper.selectAll(null);
            if(CollectionUtils.isEmpty(allcategories)){
                return commonResponse;
            }
            List<SubCategory> categories = allcategories.stream().filter(s->ids.contains(s.getId())).collect(Collectors.toList());
            CategoryInfo categoryInfo ;
            List<ReferrerInfo> referrerInfos = new ArrayList<>();
            ReferrerInfo referrerInfo;
            for(SubCategory subCategory:categories){
                categoryInfo = new CategoryInfo();
                categoryInfo.setId(subCategory.getId());
                categoryInfo.setName(subCategory.getName());
                categoryInfo.setSortId(subCategory.getSortId());
                categoryInfo.setChildrens(CategoryUtil.getChildren(categoryInfo.getId(),allcategories));
                List<User> users = userMapper.selectByFirstCategory(String.valueOf(categoryInfo.getId()));
                if(!CollectionUtils.isEmpty(users)){
                    for(User u:users){
                        referrerInfo = new ReferrerInfo();
                        referrerInfo.setReferrerId(u.getUserId());
                        referrerInfo.setReferrerName(u.getUserName());
                        referrerInfos.add(referrerInfo);
                    }
                }
                categoryInfo.setReferrerInfos(referrerInfos);
                categoryInfos.add(categoryInfo);
            }
        }catch (ServiceException e){
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }
        return commonResponse;
    }

    @RequestMapping(value = "/pictures" , method= RequestMethod.POST)
    public @ResponseBody PageResponse<PictureInfo> pictures(HttpServletRequest request,@RequestBody BaseRequest<QueryParams> baseRequest){
        Device device = DeviceUtils.getCurrentDevice(request);
        PageResponse<PictureInfo> commonResponse = new PageResponse();
        checkSign(baseRequest);
        checkToken(baseRequest.getToken());
        User user = getUserByToken(baseRequest);
        Integer userId = user.getUserId();
        String waterMark = WaterMarkUtil.getWaterMark(user.getWatermark());;
        FileQueryVO fileQueryVO = new FileQueryVO();
        QueryParams queryParams = baseRequest.getData();
        if(queryParams == null){
            throw new ServiceException("1008","参数不合法");
        }
        if(CollectionUtils.isEmpty(queryParams.getCategoryIds())){
            throw new ServiceException("1008","分类不能为空");
        }
        if(queryParams.getCurrentPage()==null){
            queryParams.setCurrentPage(1);
        }
        if(queryParams.getPageSize()==null){
            queryParams.setPageSize(80);
        }
        if(CollectionUtils.isEmpty(queryParams.getCategoryIds())){
            //todo
        }
        PageParameter pageParameter = new PageParameter(queryParams.getCurrentPage(), queryParams.getPageSize());
        fileQueryVO.setRecommendId(queryParams.getReferrerId());
        fileQueryVO.setPage(pageParameter);
        List<Set<Integer>> categoryConditions = new ArrayList<>();
        if(!CollectionUtils.isEmpty(queryParams.getCategoryIds())){
            categoryConditions.add(queryParams.getCategoryIds());
        }
        fileQueryVO.setCategoryConditions(categoryConditions);
        List<Picture> pictures =  pictureMapper.selectByPage(fileQueryVO);
        if(CollectionUtils.isEmpty(pictures)){
            return commonResponse;
        }
        int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                / pageParameter.getPageSize();
        PictureInfo pictureInfo = new PictureInfo() ;
        PictureInfo.Picture picture ;
        List<PictureInfo.Picture> list = new ArrayList<>();
        for(Picture p:pictures){
            picture = new PictureInfo.Picture();
            picture.setId(p.getId());
            picture.setClickTimes(p.getClickTimes());
            picture.setDownloadTimes(p.getDownloadTimes());
            picture.setName(p.getName());
            picture.setFilePath(p.getFilePath());
            picture.setMyRecommend(p.getRecId()!=null && p.getRecId().contains(String.valueOf(userId)));
            String attachs = p.getAttach();
            if(!StringUtils.isEmpty(attachs)){
                try{
                    picture.setAttachs(Arrays.asList(attachs.split(",")));
                }catch (Exception e){
                    log.error(e.getLocalizedMessage(),e);
                }
            }
            list.add(picture);
        }
        String width;
        String height;
        if(device.isMobile()){
            width = "140";
            height = "98";
        }else{
            width = "140";
            height = "98";
        }
        String paramPrefix = "?x-oss-process=image";
        String resizeParam = "/resize,m_pad,h_"+height+",w_"+width;
        //?x-oss-process=image/resize,m_pad,h_199,w_280
        //?x-oss-process=image/resize,m_pad,h_199,w_280/watermark,image_d2F0ZXJtYXJrX-m7hOawuOemj--8iOiOq--8iS5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8xMDA,t_20,g_center
        //?x-oss-process=image/watermark,image_d2F0ZXJtYXJrX-m7hOawuOemj--8iOiOq--8iS5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8xMDA,t_20,g_center
        PictureInfo.PictureProperty p = new PictureInfo.PictureProperty();
        p.setUrl(appConfig.getOssPath());
        p.setSizeParam(resizeParam);
        p.setParamPrefix(paramPrefix);
        p.setWaterMark(waterMark);
        pictureInfo.setPictures(list);
        pictureInfo.setPictureProperty(p);
        commonResponse.setTotalPage(totalPage);
        commonResponse.setTotalCount(pageParameter.getTotalCount());
        commonResponse.setResult(pictureInfo);
        return commonResponse;
    }




    public User getUserByToken(BaseRequest baseRequest) {
        User user =  userMapper.selectByToken(baseRequest.getToken());
        if(user==null){
            throw new ServiceException("1008","token不存在或已过期");
        }
        return user;
    }

    public void checkToken(String token) {
        if(StringUtils.isEmpty(token)){
            throw new ServiceException("1007","token不能为空");
        }
    }


    private void checkUser(String pwd, String imei, User user) {
        if(user==null){
            throw new ServiceException("1000","账号不存在");
        }
        if(StringUtils.isEmpty(imei)){
            throw new ServiceException("1001","imei不能为空");
        }
        if("1".equals(user.getLimitIpFlag()) && !StringUtils.isEmpty(user.getImei())  && !imei.equals(user.getImei())){
            throw new ServiceException("1004","该账户只能在绑定的设备上登陆");
        }
        if(!"app".equalsIgnoreCase(user.getDevice())){
            throw new ServiceException("1003","该账号只能在pc端使用");
        }
        String password =  new Md5PasswordEncoder().encodePassword(user.getPwd()+SALT,"");
        if(user == null || !pwd.equals(password)){
            throw new ServiceException("1002","用户名或密码不正确");
        }
    }

    private void checkSign(BaseRequest baseRequest) {
        if(baseRequest.getData() == null){
            return;
        }
        if(StringUtils.isEmpty(baseRequest.getSign())){
            throw new ServiceException("1006","sign不能为空");
        }
        String sign = baseRequest.getSign();
        Object obj = baseRequest.getData();
        if(obj == null){
            return;
        }
        String paramSign = "" ;
        Map<String,Object> paramMap = new HashedMap();
        if(obj instanceof  SignInRequest){
            SignInRequest request = (SignInRequest)obj;
            paramMap.put("userName",request.getUserName());
            paramMap.put("password",request.getPassword());
            paramSign = SignUtil.sign(paramMap);
        }else if(obj instanceof  QueryParams) {
            QueryParams request = (QueryParams)obj;
            if(request.getReferrerId()!=null){
                paramMap.put("referrerId",request.getReferrerId());
            }
            if(request.getCurrentPage()!=null){
                paramMap.put("currentPage",request.getCurrentPage());
            }
            if(request.getPageSize()!=null){
                paramMap.put("pageSize",request.getPageSize());
            }
            if(!CollectionUtils.isEmpty(request.getCategoryIds())){
                paramMap.put("categoryIds",request.getCategoryIds());
            }
            paramSign = SignUtil.sign(paramMap);
        }
        if(!sign.equals(paramSign)){
            throw new ServiceException("1005","签名不正确");
        }
    }

    public static void main(String[] args) {
        BaseRequest<SignInRequest>  baseRequest = new BaseRequest<>();
        baseRequest.setImei("123");
        baseRequest.setToken("343556773");
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUserName("ere");
        signInRequest.setPassword("sfdf");
        baseRequest.setData(signInRequest);
        System.out.println(JSON.toJSONString(baseRequest));

        /*CommonResponse<PictureInfo> pictureInfoCommonResponse = new CommonResponse<>();
        PictureInfo pictureInfo = new PictureInfo();
        PictureInfo.PictureProperty pictureProperty = new PictureInfo.PictureProperty();
        pictureProperty.setUrl("http://znz.oss-cn-shenzhen.aliyuncs.com/");
       // pictureProperty.setParameter("x-oss-process=image/resize,m_pad,h_199,w_280");
        pictureInfo.setPictureProperty(pictureProperty);
        List<PictureInfo.Picture> pictures = new ArrayList<>();
        PictureInfo.Picture picture = new PictureInfo.Picture();
        picture.setId(12353L);
        picture.setFilePath("63f3f5fc-e11f-4fae-82f2-cd82b8f31bd5.jpg");
        picture.setName("男单170508n_1503.jpg");
        picture.setClickTimes(0);
        picture.setDownloadTimes(0);
        List<String> attachs = new ArrayList<>();
        attachs.add("435b78a6-bc8e-4602-aa90-8e6d6d018bd6.jpg");
        picture.setAttachs(attachs);
        pictures.add(picture);
        pictureInfo.setPictures(pictures);
        pictureInfoCommonResponse.setResult(pictureInfo);
        System.out.println(JSON.toJSONString(pictureInfoCommonResponse));

        BaseRequest<QueryParams> queryParamsBaseRequest = new BaseRequest<>();*/
    }
}
