package com.znz.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.znz.config.AppConfig;
import com.znz.dao.*;
import com.znz.model.*;
import com.znz.service.CategoryService;
import com.znz.util.PartionCodeHoder;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.znz.util.Constants;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/subCategory")
public class SubCategoryController {

    public static final int PAGE_SIZE = 120;
    @Resource
    private SubCategoryMapper     subCategoryMapper;


    @Resource
    private PictureMapper         pictureMapper;

    @Resource
    private PictureCategoryMapper pictureCategoryMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PicRecommendMapper picRecommendMapper;

    @Resource
    private OSSClient ossClient;

    @Resource
    private AppConfig appConfig;

    @Resource
    private CategoryService categoryService;

    final static Cache<String, Object> brandCache = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();
    Map<String,String> brandMap = new HashMap<>();

    public static ExecutorService pool = Executors.newFixedThreadPool(10);

    @RequestMapping(value = "/showCategory")
    public String showCategory(HttpServletRequest request,QueryParam queryParam, Model model,String firstCategoryId,String secondCategoryId)
        throws ParseException, ExecutionException {
        model.addAttribute("totalPage",0);
        if(queryParam.getCurrentPage() == null){
            queryParam.setCurrentPage(1);
        }
        if(queryParam.getPageSize() == null){
            queryParam.setPageSize(PAGE_SIZE);
        }
        Integer brandId = queryParam.getBrandId();
        HttpSession session = request.getSession();
        List<SubCategory> subCategories = subCategoryMapper.selectAll(null);
        UserSession userSession = (UserSession)session.getAttribute(Constants.USER_SESSION);
        if(!PermissionUtil.checkPermisson(request)){  //不是admin
           if(CollectionUtils.isEmpty(userSession.getUserAuths())){
               userSession.setUserAuths(new ArrayList<>());
            }
            List<Integer> auths =userSession.getUserAuths().stream().map(s->Integer.parseInt(s.getAuthId())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(auths)){
                subCategories = subCategories.stream().filter(s-> s.getCategoryLevel()!=0 || auths.contains(s.getId())).collect(Collectors.toList());
            }else {
                subCategories = new ArrayList<>();
            }
        }
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(subCategories) ){
            SubCategoryVO subCategoryVO ;
            for(SubCategory subCategory:subCategories){
                if(queryParam.getFirstSelectedId()==null && subCategory.getCategoryLevel() == 0){
                    if(StringUtils.isNotEmpty(firstCategoryId)){
                        queryParam.setFirstSelectedId(firstCategoryId);
                    }else{
                        queryParam.setFirstSelectedId(String.valueOf(subCategory.getId()));
                    }
                }
                if(queryParam.getSecondSelectedId()==null && String.valueOf(subCategory.getParentId()).equals(queryParam.getFirstSelectedId()) ){
                    if(StringUtils.isNotEmpty(secondCategoryId)){
                        queryParam.setSecondSelectedId(secondCategoryId);
                    }else{
                        queryParam.setSecondSelectedId(String.valueOf(subCategory.getId()));
                    }

                }
                subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setAllFlag(subCategory.getAllFlag());
                subCategoryVO.setCategoryLevel(subCategory.getCategoryLevel());
                if(subCategory.getCategoryLevel() == 2){
                    subCategoryVO.setChildrens(getchildrens(subCategories,subCategory.getId()));
                }
                subCategoryVOs.add(subCategoryVO);
            }
        }

        final String  temp = queryParam.getSecondSelectedId();
        Set<Integer> forthCategorys = new HashSet<>();
        List<Set<Integer>> categoryConditions = new ArrayList<>();
        boolean noFourthSelectedId = false;
        if(StringUtils.isEmpty(queryParam.getFourthSelectedId())) {
            noFourthSelectedId = true;
            Set<Integer> thirdCategorys =    subCategories.stream().filter(s-> String.valueOf(s.getParentId()).equals(temp) ).map(s->s.getId()).collect(Collectors.toSet());//三级类
            Set<Integer> set =  subCategories.stream().filter(s->thirdCategorys.contains(s.getParentId())).map(s->s.getId()).collect(Collectors.toSet()) ;//根据3级别类查找4级类
            categoryConditions.add(set);
        }else{
            forthCategorys = new HashSet(Arrays.asList( queryParam.getFourthSelectedId().split("[,;]")));
            String[] ids = queryParam.getFourthSelectedId().split("[;]");
            Set<Integer> set ;
            for(String x:ids){
                set = new HashSet<>();
                String[] arr = x.split(",");
                for(String item:arr){
                    if(StringUtils.isNumeric(item)){
                        set.add(Integer.parseInt(item));
                    }
                }
                if(!CollectionUtils.isEmpty(set)){
                    categoryConditions.add(set);
                }

            }

        }
        PageParameter pageParameter = new PageParameter(queryParam.getCurrentPage(), queryParam.getPageSize());
        FileQueryVO fileQueryVO = new FileQueryVO();
        if(queryParam.getRecommendId()!=null){
            fileQueryVO.setRecommendId(queryParam.getRecommendId());
        }
        model.addAttribute("currentPage",queryParam.getCurrentPage());
        Integer partionCode = subCategories.stream().filter(s-> String.valueOf(s.getId()).equals(queryParam.getSecondSelectedId())).map(s->s.getPartionCode()).findAny().orElse(null);
        PartionCodeHoder.set(String.valueOf(partionCode));
        fileQueryVO.setPartionCode(partionCode);
        if(!CollectionUtils.isEmpty(categoryConditions) && categoryConditions.stream().allMatch(s->s.size()>0)){
            fileQueryVO.setPage(pageParameter);
            fileQueryVO.setCategoryConditions(categoryConditions);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(StringUtils.isNoneBlank(queryParam.getStartTime())){
                fileQueryVO.setStartTime(simpleDateFormat.parse(queryParam.getStartTime()));
            }
            if(StringUtils.isNoneBlank(queryParam.getEndTime())){
                fileQueryVO.setEndTime(simpleDateFormat.parse(queryParam.getEndTime()));
            }
            if(brandId!=null){
                fileQueryVO.getCategoryConditions().add(Sets.newHashSet(brandId));
            }
            if(StringUtils.isNoneBlank(queryParam.getDelFlag())){
                //删除
                deletePictrues(fileQueryVO);
            }else{
                List<Picture> pictures;
                if("3610".equals(queryParam.getFirstSelectedId())){
                    fileQueryVO.setSortFiled("sort");
                }
                if(noFourthSelectedId && brandId==null){
                    pictures = pictureMapper.selectBySimplePage(fileQueryVO);
                }else{
                    List<Integer> ids = fileQueryVO.getCategoryConditions().stream().flatMap(s->s.stream()).collect(Collectors.toList());
                    pictures =  pictureMapper.selectByPage(fileQueryVO);
                }
                int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                        / pageParameter.getPageSize();
                model.addAttribute("totalPage",totalPage);
                model.addAttribute("totalCount",pageParameter.getTotalCount());
                model.addAttribute("pictures",pictures);
            }
        }
        String key = queryParam.getSecondSelectedId();
        String brandName = brandMap.get(key);
        boolean brandFlag = false;
        if(StringUtils.isNotBlank(brandName)){
            brandFlag = true;
        }else{
            SubCategory subCategory = subCategoryMapper.selectSingleByPpid(Integer.parseInt(queryParam.getSecondSelectedId()));
            if(subCategory!=null){
                brandFlag = true;
                brandMap.put(key,subCategory.getName());
            }
        }
        List<User> users = userMapper.selectByFirstCategory(queryParam.getFirstSelectedId());
        model.addAttribute("subCategoryVOs",subCategoryVOs);
        model.addAttribute("firstSelectedId",queryParam.getFirstSelectedId());
        model.addAttribute("secondSelectedId",queryParam.getSecondSelectedId());
        model.addAttribute("thirdSelectedId",queryParam.getThirdSelectedId());
        model.addAttribute("fourthSet",forthCategorys);
        model.addAttribute("startTime",queryParam.getStartTime());
        model.addAttribute("endTime",queryParam.getEndTime());
        model.addAttribute("users",users);
        model.addAttribute("recommendId",queryParam.getRecommendId());
        model.addAttribute("brandId",queryParam.getBrandId());
        model.addAttribute("brandName",queryParam.getBrandName());
        model.addAttribute("brandFlag",brandFlag);
        PartionCodeHoder.clear();
        return "admin/showCategory";
    }

    private void deletePictrues(FileQueryVO fileQueryVO) {
        FileQueryVO queryVO = new FileQueryVO();
        queryVO.setCategoryConditions(fileQueryVO.getCategoryConditions());
        PageParameter page = new PageParameter(fileQueryVO.getPage().getCurrentPage(),1000);
        queryVO.setPage(page);
        queryVO.setStartTime(fileQueryVO.getStartTime());
        queryVO.setEndTime(fileQueryVO.getEndTime());
        queryVO.setRecommendId(fileQueryVO.getRecommendId());
        queryVO.setPartionCode(fileQueryVO.getPartionCode());
        List<Picture>  pictures =  pictureMapper.selectByPage(queryVO);
        while(!CollectionUtils.isEmpty(pictures)){
            List<Long> ids = pictures.stream().map(s->s.getId()).collect(Collectors.toList());
            pictureMapper.deleteByPrimaryKeys(ids);
            pictureCategoryMapper.deleteByPictrueIds(ids);
            deleteFiles(pictures);
            PageParameter pageParameter = queryVO.getPage();
            int curentPage = pageParameter.getCurrentPage()+1;
            pageParameter.setCurrentPage(curentPage);
            pictures =  pictureMapper.selectByPage(queryVO);
        }


    }

    private void deleteFiles(List<Picture> pictures) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> keys = new ArrayList<>();
                    for(Picture p:pictures){
                        keys.add(p.getFilePath());
                        String attach = p.getAttach();
                        if (StringUtils.isNoneBlank(attach)) {
                            List<String> attachs = Arrays.stream(attach.split(",")).map(s->s.substring(0,s.indexOf("|"))).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(attachs)) {
                                keys.addAll(attachs);
                            }
                        }
                    }
                    deleteFile(keys);
                }catch (Exception e){
                    log.error(e.getLocalizedMessage(),e);
                }
            }
        });
    }

    private List<SubCategoryVO> getchildrens(List<SubCategory> subCategories, int parentId) {
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        SubCategoryVO subCategoryVO;
        for (SubCategory subCategory : subCategories) {
            if (subCategory.getParentId() == parentId) {
                subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setAllFlag(subCategory.getAllFlag());
                subCategoryVO.setCategoryLevel(subCategory.getCategoryLevel());
                subCategoryVOs.add(subCategoryVO);
            }
        }
        subCategoryVOs.stream().sorted(Comparator.comparing(SubCategoryVO::getSortId));
        return subCategoryVOs;
    }

    private List<SubCategory> getchildren(List<SubCategory> subCategories, int parentId) {
        List<SubCategory> subCategories1 = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            if (subCategory.getParentId() == parentId) {
                subCategories1.add(subCategory);
            }
        }
        return subCategories1;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int id) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        } else {
            List<SubCategory> subCategories = subCategoryMapper.selectByParentId(id);

            if(!CollectionUtils.isEmpty(subCategories)){
                resultVO.setMsg("该类别下有子类，请删除子类");
            }else{
                SubCategory subCategory = subCategoryMapper.selectByPrimaryKey(id);
                if(subCategory.getCategoryLevel() == 3){
                    Integer partionCdoe =  categoryService.getPartionCodeBy3(subCategory.getParentId());
                    PartionCodeHoder.set(String.valueOf(partionCdoe));
                    List<Long> list = pictureCategoryMapper.selectByCategoryId(id);
                    if(!CollectionUtils.isEmpty(list)){
                        resultVO.setMsg("该类别下图片，请删除该类别下的图片");
                    }else{
                        subCategoryMapper.deleteByPrimaryKey(id);
                        resultVO.setCode(0);
                    }
                }else{
                    subCategoryMapper.deleteByPrimaryKey(id);
                    resultVO.setCode(0);
                }
            }
        }
        return resultVO;
    }

    private void getAllChildCategory(List<SubCategory> subCategories1, Integer parentId ,List<SubCategory> childs) {
        for(SubCategory subCategory:subCategories1){
                List<SubCategory> childList = getchildren(subCategories1,subCategory.getId());
                if(!CollectionUtils.isEmpty(childList) ){
                    if(childList.get(0).getCategoryLevel() == 3){
                        for(SubCategory child:childList){
                            childs.add(child);
                        }
                    }else{
                        getAllChildCategory(childList,subCategory.getId(),childs);
                    }
                }else{
                    break;
                }
         }
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(HttpServletRequest request,
                      @Valid @ModelAttribute SubCategoryVO subCategoryVO) {

        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }


        SubCategory subCategory = new SubCategory();
        BeanUtils.copyProperties(subCategoryVO, subCategory);
        subCategory.setId(null);
        subCategory.setAllFlag("N");
        if(subCategory.getCategoryLevel() == 1){
            Integer partionCode = subCategoryMapper.selectMaxPartionCode() ;
            if(partionCode==null){
                partionCode = 0;
            }
            partionCode = partionCode + 1;
            subCategory.setPartionCode(partionCode);
        }
        subCategoryMapper.insert(subCategory);
        return "redirect:"+Constants.INDEX_PAGE+"admin/subCategory/showCategory?firstCategoryId="+subCategoryVO.getFirstSelectedId()+"&secondCategoryId="+subCategoryVO.getSecondSelectedId();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request ,Integer id,String oldName,String name) {
        UserSession userSession = (UserSession) request.getSession()
            .getAttribute(Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
            resultVO.setCode(1);
        } else {
            SubCategory subCategory = new SubCategory();
            subCategory.setName(name);
            subCategory.setId(id);
            subCategoryMapper.updateByPrimaryKeySelective(subCategory);
            resultVO.setCode(0);
        }
        return resultVO;
    }



    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public @ResponseBody ResultVO sort(HttpServletRequest request, String param) {
        ResultVO resultVO = new ResultVO();
        String[] array = param.split(";");
        for (String s : array) {
            SubCategory subCategory = new SubCategory();
            String[] subArray = s.split(":");
            subCategory.setId(Integer.parseInt(subArray[0]));
            subCategory.setSortId(Integer.parseInt(subArray[1]));
            subCategoryMapper.updateByPrimaryKeySelective(subCategory);
        }
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/validName", method = RequestMethod.POST)
    public @ResponseBody ResultVO validName(String name){
        ResultVO resultVO = new ResultVO();
        SubCategory temp = subCategoryMapper.selectByName(name);
        if (temp != null) {
            resultVO.setMsg("类别名称已经存在，请使用其它类别名称");
        } else {
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/recommend/{id}", method = RequestMethod.GET)
    public @ResponseBody ResultVO recommend(HttpServletRequest request, @PathVariable Long id,Integer secondSelectedId) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        UserSession userSession = (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        Integer partionCode = categoryService.getPartionCodeBy2(secondSelectedId);
        PartionCodeHoder.set(String.valueOf(partionCode));
        Picture record =pictureMapper.selectByPrimaryKey(id);
        Integer userId = userSession.getUser().getUserId();
        PicRecommend picRecommend = new PicRecommend();
        picRecommend.setPictureId(id);
        picRecommend.setUserId(userSession.getUser().getUserId());
        picRecommend.setPartionCode(null);
        picRecommend.setCreateTime(new Date());
        picRecommend.setPartionCode(partionCode);
        picRecommendMapper.insert(picRecommend);
        if(StringUtils.isNoneBlank(record.getRecId())){
            record.setRecId(record.getRecId()+","+userId);
        }else{
            record.setRecId(String.valueOf(userId));
        }
        Picture updatePicture = new Picture();
        updatePicture.setId(id);
        updatePicture.setRecId(record.getRecId());
        pictureMapper.updateByPrimaryKeySelective(updatePicture);
        resultVO.setCode(0);
        PartionCodeHoder.clear();
        return resultVO;
    }

    @RequestMapping(value = "/cancelRecommend/{id}", method = RequestMethod.GET)
    public @ResponseBody ResultVO cancelRecommend(HttpServletRequest request, @PathVariable Long id,Integer secondSelectedId) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        Integer partionCode = categoryService.getPartionCodeBy2(secondSelectedId);
        PartionCodeHoder.set(String.valueOf(partionCode));
        UserSession userSession = (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        Integer userId = userSession.getUser().getUserId();
        picRecommendMapper.delete(id,userId,partionCode);

        Picture record =pictureMapper.selectByPrimaryKey(id);
        if(StringUtils.isNoneBlank(record.getRecId())){
            List list =  Arrays.stream(record.getRecId().split(",")).filter(s->!s.equals(String.valueOf(userId))).collect(Collectors.toList());
            String recId  = StringUtils.join(list,",");
            record.setRecId(recId);
            Picture updatePicture = new Picture();
            updatePicture.setId(id);
            updatePicture.setRecId(record.getRecId());
            pictureMapper.updateByPrimaryKeySelective(updatePicture);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    public void deleteFile( List<String> keys) {
        if (!CollectionUtils.isEmpty(keys)) {
            if (!CollectionUtils.isEmpty(keys)) {
                if (keys.size() > 1000) {
                    int count = (keys.size() + 1000) / 1000;
                    List<String> childs;
                    for (int i = 0; i < count; i++) {
                        int endIndex = (i + 1) * 1000;
                        if (endIndex > keys.size()) {
                            endIndex = keys.size();
                        }
                        childs = keys.subList(i * 1000, endIndex);
                        System.out.println(childs.get(childs.size() - 1));
                        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(appConfig.getBucketName());
                        deleteObjectsRequest.setKeys(childs);
                        log.info("delete from oss :keys ：+"+keys);
                        ossClient.deleteObjects(deleteObjectsRequest);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(appConfig.getBucketName());
                    deleteObjectsRequest.setKeys(keys);
                    log.info("delete from oss1 :keys ：+"+keys);
                    ossClient.deleteObjects(deleteObjectsRequest);
                }

            }
        }
    }

    @RequestMapping(value = "/brand/{ppid}", method = RequestMethod.GET)
    public @ResponseBody List<BrandVO> getBrand(HttpServletRequest request, @PathVariable Integer ppid) {
        List<SubCategory> subCategories = subCategoryMapper.selectByPpid(ppid);
        if(CollectionUtils.isEmpty(subCategories)){
            return new ArrayList();
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
                brandVO.setLetter("_");
            }
            brandVOS.add(brandVO);
        }
        return brandVOS;
    }


}
