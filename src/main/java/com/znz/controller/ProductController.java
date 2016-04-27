package com.znz.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.znz.dao.TProductMapper;
import com.znz.model.TProduct;
import com.znz.util.Constants;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/product")
public class ProductController {

    @Resource
    private TProductMapper productMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<TProduct> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                     @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                     @RequestParam(value = "sidx", required = false) String sidx,
                                                     @RequestParam(value = "sord", required = false) String sord,
                                                     @RequestParam(value = "filters", required = false) String filters) {
        ProductQueryVO productQueryVO = new ProductQueryVO();
        productQueryVO.setSortName(sidx);
        productQueryVO.setSord(sord);
        if(StringUtils.isNotEmpty(filters)){
            SearchFilter searchFilter = JSON.parseObject(filters,SearchFilter.class);
            List<SearchField> rules = searchFilter.getRules();
            for (SearchField field:rules){
                if(StringUtils.isEmpty(field.getData())){
                    continue;
                }
                if(field.getField().equals("prodNo")){
                    productQueryVO.setProdNo("%"+field.getData()+"%");
                }else if(field.getField().equals("start")){
                    productQueryVO.setStart("%"+field.getData()+"%");
                }else if(field.getField().equals("destination")){
                    productQueryVO.setDestination("%"+field.getData()+"%");
                }else if(field.getField().equals("prodName")){
                    productQueryVO.setProdName("%"+field.getData()+"%");
                }
            }
        }
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
            Integer.parseInt(rows));
        productQueryVO.setPage(pageParameter);
        List<TProduct> products = productMapper.selectByPage(productQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
            pageParameter.getTotalCount(), products);
        return jqGridData;
    }



    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request,
                                       @Valid @ModelAttribute ProductVO productVO) {
        if ("add".equals(productVO.getOper())) {
            return add(request, productVO);
        } else if ("edit".equals(productVO.getOper())) {
            return update(request, productVO);
        } else if ("del".equals(productVO.getOper())) {
            return delete(request, request.getParameter("id"));
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request,
                                      @Valid @ModelAttribute ProductVO tProduct) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }
        TProduct product = productMapper.selectByPrimaryKey(tProduct.getId());
        if (product != null) {
            resultVO.setMsg("产品编码已经存在，请使用新的编码");
        } else {
            tProduct.setId(UUID.randomUUID().toString());
            tProduct.setCreateTime(new Date());
            tProduct.setUpdateTime(new Date());
            productMapper.insert(tProduct);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute ProductVO tProduct) {
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        } else {
            tProduct.setUpdateTime(new Date());
            productMapper.updateByPrimaryKeySelective(tProduct);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable String userId) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
                Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        } else {
            productMapper.deleteByPrimaryKey(userId);
            resultVO.setCode(0);
        }
        return resultVO;
    }

}
