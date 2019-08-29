package com.znz.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Sets;
import com.znz.config.AppConfig;
import com.znz.dao.PicRecommendMapper;
import com.znz.dao.PictureMapper;
import com.znz.dao.SubCategoryMapper;
import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.exception.ServiceException;
import com.znz.model.PicRecommend;
import com.znz.model.Picture;
import com.znz.model.SubCategory;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.service.CategoryService;
import com.znz.util.CategoryUtil;
import com.znz.util.PartionCodeHoder;
import com.znz.util.SignUtil;
import com.znz.util.WaterMarkUtil;
import com.znz.vo.BaseRequest;
import com.znz.vo.BrandVO;
import com.znz.vo.CategoryInfo;
import com.znz.vo.CheckUpdateVO;
import com.znz.vo.CommonResponse;
import com.znz.vo.FileQueryVO;
import com.znz.vo.PageParameter;
import com.znz.vo.PictureInfo;
import com.znz.vo.PictureRequest;
import com.znz.vo.QueryParams;
import com.znz.vo.ReferrerInfo;
import com.znz.vo.SignInRequest;
import com.znz.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/5/9.
 */
@Controller
@Slf4j
public class MobileController {

    public static final String SALT = LoginController.EKY;
    public static final String UID = "uid";
    public static final String CATEGORY_KEY = "category_key:";

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
    @Resource
    private CategoryService categoryService;
    @Resource
    private PicRecommendMapper picRecommendMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/signIn" , method= RequestMethod.POST)
    public @ResponseBody CommonResponse<UserInfo> signIn(@RequestBody BaseRequest<SignInRequest> baseRequest) {
        log.info("signIn request:{}",baseRequest);
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
        log.info("signIn request:{}",commonResponse);
        return commonResponse;
    }

    @RequestMapping(value = "/categorys" , method= RequestMethod.POST)
    public @ResponseBody CommonResponse<List<CategoryInfo>> category(HttpServletRequest request,@RequestBody BaseRequest baseRequest) {
        CommonResponse<List<CategoryInfo>> commonResponse = new CommonResponse();
        List<CategoryInfo> categoryInfos = new ArrayList<>();
        try{
            //checkSign(baseRequest);
            //checkToken(baseRequest.getToken());
            if(StringUtils.isEmpty(baseRequest.getImei())){
                String uid = String.valueOf(request.getSession().getAttribute(UID));
                //web 端
                log.info("token:"+uid);
                baseRequest.setToken(uid);
            }
            /*String redisKey = CATEGORY_KEY+baseRequest.getToken();
            Object object = redisTemplate.opsForValue().get(redisKey);
            if(object!=null ){
                 String redisValus = (String)object;
                 if(!StringUtils.isEmpty(redisValus)){
                     log.info("load category from redis");
                     categoryInfos = JSON.parseArray(redisValus,CategoryInfo.class);
                     commonResponse.setResult(categoryInfos);
                     return commonResponse;
                 }
            }*/
            User user = getUserByToken(baseRequest);
            Set<Integer> ids ;
            if(user!=null){
                commonResponse.setResult(categoryInfos);
                List<UserAuth> userAuths =  userAuthMapper.listByUserId(user.getUserId());
                if(CollectionUtils.isEmpty(userAuths)){
                    return commonResponse;
                }
                ids = userAuths.stream().map(s->Integer.parseInt(s.getAuthId())).collect(Collectors.toSet());
            }else {
                //招聘
                ids = Sets.newHashSet(21347569);
            }
            List<SubCategory> allcategories = subCategoryMapper.selectAll(null);
            if(CollectionUtils.isEmpty(allcategories)){
                return commonResponse;
            }
            List<SubCategory> categories = allcategories.stream().filter(s->ids.contains(s.getId())).collect(Collectors.toList());
            Set<Integer> brandIds =  subCategoryMapper.selectBrandCategoryIds();
            CategoryInfo categoryInfo ;
            for(SubCategory subCategory:categories){
                categoryInfo = new CategoryInfo();
                categoryInfo.setId(subCategory.getId());
                categoryInfo.setName(subCategory.getName());
                categoryInfo.setSortId(subCategory.getSortId());
                categoryInfo.setChildrens(CategoryUtil.getChildren(categoryInfo.getId(),allcategories));
                if(subCategory.getCategoryLevel() == 0){
                    categoryInfo.setReferrerInfos(getReferrers(categoryInfo.getId()));
                }
                for(CategoryInfo info :categoryInfo.getChildrens()){
                    if(brandIds.contains(info.getId())){
                        info.setBrandFlag(true);
                    }
                }
                categoryInfos.add(categoryInfo);
               // redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(categoryInfos),30, TimeUnit.MINUTES);
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

    public List<ReferrerInfo> getReferrers(int categoryId) {
        ReferrerInfo referrerInfo;
        List<ReferrerInfo> referrerInfos = new ArrayList();
        List<User> users = userMapper.selectByFirstCategory(String.valueOf(categoryId));
        if(!CollectionUtils.isEmpty(users)){
            for(User u:users){
                referrerInfo = new ReferrerInfo();
                referrerInfo.setReferrerId(u.getUserId());
                referrerInfo.setReferrerName(u.getCompany());
                referrerInfos.add(referrerInfo);
            }
        }
        return referrerInfos;
    }

    @RequestMapping(value = "/pictures" , method= RequestMethod.POST)
    public @ResponseBody CommonResponse<PictureInfo> pictures(HttpServletRequest request,@RequestBody BaseRequest<QueryParams> baseRequest){
        CommonResponse<PictureInfo> commonResponse = new CommonResponse();
        try {
            Device device = DeviceUtils.getCurrentDevice(request);
            if(StringUtils.isEmpty(baseRequest.getImei())){
                //web 端
                baseRequest.setToken(String.valueOf(request.getSession().getAttribute(UID)));
            }
            //checkSign(baseRequest);
            checkToken(baseRequest.getToken());
            User user = getUserByToken(baseRequest);
            Integer userId = user.getUserId();
            String waterMark = WaterMarkUtil.getWaterMark(user.getWatermark());
            FileQueryVO fileQueryVO = new FileQueryVO();
            QueryParams queryParams = baseRequest.getData();
           /* if (queryParams == null) {
                throw new ServiceException("1008", "参数不合法");
            }
            if (StringUtils.isEmpty(queryParams.getSecondCategoryId())) {
                throw new ServiceException("1008", "分类不能为空");
            }*/
            if (queryParams.getCurrentPage() == null) {
                queryParams.setCurrentPage(1);
            }
            if (queryParams.getPageSize() == null) {
                queryParams.setPageSize(120);
            }
            List<Set<Integer>> categoryConditions = new ArrayList<>();
            String categoryIds = queryParams.getCategoryIds();
            PageParameter pageParameter = new PageParameter(queryParams.getCurrentPage(), queryParams.getPageSize());
            fileQueryVO.setRecommendId(queryParams.getReferrerId());
            fileQueryVO.setPage(pageParameter);
            String gid = queryParams.getBookId();
            List<Picture> pictures ;
            log.info("gid:{}",gid);
            String partionCode = null;
            if(!StringUtils.isEmpty(gid)){
                partionCode = gid.substring(0,gid.indexOf("_"));
                log.info("partionCode:{}",partionCode);
            }else{
                partionCode = String.valueOf(categoryService.getPartionCodeBy2(Integer.parseInt(queryParams.getSecondCategoryId())));
                if(categoryService.isSortByName(queryParams.getSecondCategoryId())){
                    fileQueryVO.setSortFiled("sort");
                }
            }
            if(!StringUtils.isEmpty(partionCode)){
                fileQueryVO.setPartionCode(Integer.parseInt(partionCode));
            }
            PartionCodeHoder.set(partionCode);
            Integer brandId = queryParams.getBrandId();
            if(!StringUtils.isEmpty(gid)){
                //按书id查询
                Picture picture =  pictureMapper.selectByGid(queryParams.getBookId());
                fileQueryVO.setBookId(picture.getId());
                pictures = pictureMapper.selectByBookIdPage(fileQueryVO);
            }else if(StringUtils.isEmpty(categoryIds) && brandId==null){
                pictures = pictureMapper.selectBySimplePage(fileQueryVO);
            }else{
                if(!StringUtils.isEmpty(categoryIds)){
                    String[] ids = categoryIds.split("[,]");
                    Set<Integer> set ;
                    for(String x:ids){
                        set = new HashSet<>();
                        set.add(Integer.parseInt(x));
                        if(!CollectionUtils.isEmpty(set)){
                            categoryConditions.add(set);
                        }
                    }
                }
                //categoryConditions.add(Arrays.stream(ids).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toSet()));
                fileQueryVO.setCategoryConditions(categoryConditions);
                if(brandId!=null){
                    fileQueryVO.getCategoryConditions().add(Sets.newHashSet(brandId));
                }
                pictures = pictureMapper.selectByPage(fileQueryVO);
            }
            PartionCodeHoder.clear();
            if (CollectionUtils.isEmpty(pictures)) {
                commonResponse.setSuccess(true);
                return commonResponse;
            }
            int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
            PictureInfo pictureInfo = new PictureInfo();
            PictureInfo.Picture picture;
            List<PictureInfo.Picture> list = new ArrayList<>();
            for (Picture p : pictures) {
                picture = new PictureInfo.Picture();
                picture.setId(p.getGid());
                picture.setReadId(p.getId());
                picture.setClickTimes(p.getClickTimes());
                picture.setDownloadTimes(p.getDownloadTimes());
                picture.setName(p.getName().substring(0,p.getName().indexOf(".")));
                picture.setFilePath(p.getFilePath());
                picture.setMyRecommend(p.getRecId() != null && p.getRecId().contains(String.valueOf(userId)));
                picture.setWidth(StringUtils.isEmpty(p.getWidth())?1400:Integer.parseInt(p.getWidth()));
                picture.setHeight(StringUtils.isEmpty(p.getHeight())?1000:Integer.parseInt(p.getHeight()));
                picture.setSize(StringUtils.isEmpty(p.getSize())?100:Integer.parseInt(p.getSize()));
                String attachs = p.getAttach();
                final  Integer width = picture.getWidth();
                final  Integer height = picture.getHeight();
                if (!StringUtils.isEmpty(attachs)) {
                    try {
                        picture.setAttachs(Arrays.stream(attachs.split(FileController.ATTACH_SEPARATOR)).map(s->s.split("\\|")[0]).collect(Collectors.toList()));
                        picture.setAttachs2(Arrays.stream(attachs.split(FileController.ATTACH_SEPARATOR)).map(
                            s->{
                                if(s.indexOf(FileController.ATTACH_SIZE_SEPARATOR)!=-1){
                                    return s.split("\\|")[0]+s.substring(s.lastIndexOf(FileController.ATTACH_SIZE_SEPARATOR));
                                }return s.split("\\|")[0]+FileController.ATTACH_SIZE_SEPARATOR+width+"x"+height;
                            }
                        ).collect(Collectors.toList()));
                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                }
                list.add(picture);
            }
            String width;
            String height;
            if (device.isMobile()) {
                width = "387";
                height = "284";
            } else {
                width = "387";
                height = "284";
            }
            String paramPrefix = "?x-oss-process=image";
            String resizeParam = "/resize,m_pad,h_" + height + ",w_" + width;
            //?x-oss-process=image/resize,m_pad,h_199,w_280
            //?x-oss-process=image/resize,m_pad,h_199,w_280/watermark,image_d2F0ZXJtYXJrX-m7hOawuOemj--8iOiOq--8iS5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8xMDA,t_20,g_center
            //?x-oss-process=image/watermark,image_d2F0ZXJtYXJrX-m7hOawuOemj--8iOiOq--8iS5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8xMDA,t_20,g_center
            PictureInfo.PictureProperty property = new PictureInfo.PictureProperty();
            property.setUrl(appConfig.getOssPath());
            property.setSizeParam(resizeParam);
            property.setParamPrefix(paramPrefix);
            property.setWaterMark(waterMark);
            property.setPicType(pictures.get(0).getPicType());
            pictureInfo.setPictures(list);
            pictureInfo.setPictureProperty(property);
            pictureInfo.setTotalPage(totalPage);
            pictureInfo.setTotalCount(pageParameter.getTotalCount());
            pictureInfo.setPageSize(queryParams.getPageSize());
            commonResponse.setResult(pictureInfo);
           // log.info("pictures commonResponse:{}",commonResponse);
        }catch (ServiceException e){
            log.error(e.getLocalizedMessage(),e);
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }finally {
            PartionCodeHoder.clear();
        }
        return commonResponse;
    }




    public User getUserByToken(BaseRequest baseRequest) {
        User user =  userMapper.selectByToken(baseRequest.getToken());
        /*if(user==null){
            throw new ServiceException("1008","登录超时,请重新登录");
        }*/
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
        if(1 == user.getLimitImeiFlag() && !StringUtils.isEmpty(user.getImei())  && !imei.equals(user.getImei())){
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
            if(!StringUtils.isEmpty(request.getCategoryIds())){
                paramMap.put("categoryIds",request.getCategoryIds());
            }
            if(!StringUtils.isEmpty(request.getSecondCategoryId())){
                paramMap.put("secondCategoryId",request.getSecondCategoryId());
            }
            if(!StringUtils.isEmpty(request.getBrandId())){
                paramMap.put("brandId",request.getBrandId());
            }

        }else if(obj instanceof  PictureRequest) {
            PictureRequest request = (PictureRequest)obj;
            paramMap.put("id",request.getId());
        }
        paramSign = SignUtil.sign(paramMap);
        if(!sign.equals(paramSign)){
            throw new ServiceException("1005","签名不正确");
        }
    }


    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public
    @ResponseBody
    CommonResponse recommend(@RequestBody BaseRequest<PictureRequest> baseRequest) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            checkSign(baseRequest);
            checkToken(baseRequest.getToken());
            User user = getUserByToken(baseRequest);
            String picId = baseRequest.getData().getId();
            String partionCode = picId.substring(0, picId.indexOf("_"));
            PartionCodeHoder.set(partionCode);
            Picture picture = pictureMapper.selectByGid(picId);
            int userId = user.getUserId();
            PicRecommend picRecommend = new PicRecommend();
            picRecommend.setPictureId(picture.getId());
            picRecommend.setUserId(userId);
            picRecommend.setPartionCode(Integer.parseInt(partionCode));
            picRecommend.setCreateTime(new Date());
            picRecommendMapper.insert(picRecommend);
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(picture.getRecId())) {
                picture.setRecId(picture.getRecId() + "," + userId);
            } else {
                picture.setRecId(String.valueOf(userId));
            }
            Picture updatePicture = new Picture();
            updatePicture.setId(picture.getId());
            updatePicture.setRecId(picture.getRecId());
            pictureMapper.updateByPrimaryKeySelective(updatePicture);
            commonResponse.setResult(null);
        } catch (ServiceException e) {
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }finally {
            PartionCodeHoder.clear();
        }
        return commonResponse;
    }

    @RequestMapping(value = "/cancelRecommend", method = RequestMethod.POST)
    public
    @ResponseBody
    CommonResponse cancelRecommend(@RequestBody BaseRequest<PictureRequest> baseRequest) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            checkSign(baseRequest);
            checkToken(baseRequest.getToken());
            User user = getUserByToken(baseRequest);
            String picId = baseRequest.getData().getId();
            String partionCode = picId.substring(0, picId.indexOf("_"));
            PartionCodeHoder.set(partionCode);
            Picture picture = pictureMapper.selectByGid(picId);
            int userId = user.getUserId();
            picRecommendMapper.delete(picture.getId(),userId,Integer.parseInt(partionCode));

            if(org.apache.commons.lang3.StringUtils.isNoneBlank(picture.getRecId())) {
                List list = Arrays.stream(picture.getRecId().split(",")).filter(s -> !s.equals(String.valueOf(userId))).collect(Collectors.toList());
                String recId = org.apache.commons.lang3.StringUtils.join(list, ",");
                picture.setRecId(recId);
                Picture updatePicture = new Picture();
                updatePicture.setId(picture.getId());
                updatePicture.setRecId(picture.getRecId());
                pictureMapper.updateByPrimaryKeySelective(updatePicture);
            }
            commonResponse.setResult(null);
        } catch (ServiceException e) {
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }finally {
            PartionCodeHoder.clear();
        }
        return commonResponse;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public
    @ResponseBody
    CommonResponse download(@RequestBody BaseRequest<PictureRequest> baseRequest) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            checkSign(baseRequest);
            checkToken(baseRequest.getToken());
            User user = getUserByToken(baseRequest);
            log.info("user.getDownloadPerDay:{},getMaxDownloadTimes:{}",user.getDownloadPerDay(),user.getMaxDownloadTimes());
            if(user.getDownloadPerDay()>user.getMaxDownloadTimes()){
                throw new ServiceException("1009","您已超出今日最大下载次数!");
            }
            User u = new User();
            u.setDownloadPerDay(user.getDownloadPerDay()+1);
            u.setDownloadTotal(user.getDownloadTotal()+1);
            u.setUserId(user.getUserId());
            userMapper.updateByPrimaryKeySelective(u);
            commonResponse.setResult(null);
        } catch (ServiceException e) {
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }
        return commonResponse;
    }

    @RequestMapping(value = "/checkUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    CommonResponse<CheckUpdateVO> checkUpdate(@RequestBody BaseRequest baseRequest) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            CheckUpdateVO vo = new CheckUpdateVO();
            // Android 版本为1.0，IOS为1.0.1 升级为1.1.1
            if(baseRequest.getAppVersion().length() ==5 && !"1.2.3".equals(baseRequest.getAppVersion())){
                vo.setDownloadUrl("https://itunes.apple.com/us/app/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF/id1270152609?l=zh&ls=1&mt=8");
            }
            commonResponse.setResult(vo);
        } catch (ServiceException e) {
            commonResponse.setErrorCode(e.getErrCode());
            commonResponse.setErrorMsg(e.getErrReason());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            commonResponse.setErrorCode("9999");
            commonResponse.setErrorMsg("系统忙请稍后再试");
        }
        return commonResponse;
    }

    @RequestMapping(value = "/brand/{ppid}", method = RequestMethod.POST)
    public @ResponseBody CommonResponse<List<BrandVO>> getBrand(HttpServletRequest request,@RequestBody BaseRequest baseRequest, @PathVariable Integer ppid) {
        CommonResponse commonResponse = new CommonResponse();
        List<SubCategory> subCategories = subCategoryMapper.selectByPpid(ppid);
        if(CollectionUtils.isEmpty(subCategories)){
            return new CommonResponse();
        }
        List<BrandVO> brandVOS = new ArrayList<>();
        for(SubCategory subCategory:subCategories){
            BrandVO brandVO = new BrandVO();
            brandVO.setId(subCategory.getId());
            brandVO.setCityCode(subCategory.getId());
            brandVO.setName(subCategory.getName());
            brandVO.setParentId(ppid);
            brandVO.setPinyin(subCategory.getName());
            brandVO.setShortName(subCategory.getName());
            char letter = brandVO.getName().toUpperCase().charAt(0);
            brandVO.setLetter(String.valueOf(letter));
            if(letter<65 || letter>90 ){
                brandVO.setLetter("#");
            }
            brandVOS.add(brandVO);
        }
        brandVOS = brandVOS.stream().sorted(Comparator.comparing(BrandVO::getLetter).reversed()).collect(
            Collectors.toList());
        commonResponse.setResult(brandVOS);
        return commonResponse;
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
