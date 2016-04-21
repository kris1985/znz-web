package com.znz.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.znz.dao.THotelMapper;
import com.znz.model.THotel;
import com.znz.model.TProduct;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.znz.dao.TAttractionsMapper;
import com.znz.model.TAttractions;
import com.znz.util.Constants;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/hotel")
public class HotelController {

    @Resource
    private THotelMapper hotelMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<THotel> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                    @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                    @RequestParam(value = "sidx", required = false) String sidx,
                                                    @RequestParam(value = "sord", required = false) String sord,
                                                    @RequestParam(value = "filters", required = false) String filters) {
        HotelQueryQueryVO queryQueryVO = new HotelQueryQueryVO();
        queryQueryVO.setSortName(sidx);
        queryQueryVO.setSord(sord);
        if(StringUtils.isNotEmpty(filters)){
            SearchFilter searchFilter = JSON.parseObject(filters, SearchFilter.class);
            List<SearchField> rules = searchFilter.getRules();
            for (SearchField field:rules){
                if(StringUtils.isEmpty(field.getData())){
                    continue;
                }
                if(field.getField().equals("hotelName")){
                    queryQueryVO.setHotelName("%" + field.getData() + "%");
                }else if(field.getField().equals("areaName")){
                    queryQueryVO.setAreaName(field.getData());
                }else if(field.getField().equals("xj")){
                    queryQueryVO.setXj(field.getData());
                }
            }
        }
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
                Integer.parseInt(rows));
        queryQueryVO.setPage(pageParameter);
        List<THotel> hotels = hotelMapper.selectByPage(queryQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
                pageParameter.getTotalCount(), hotels);
        return jqGridData;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request,
                                       @Valid @ModelAttribute HotelVO hotelVO) {
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }
        if ("add".equals(hotelVO.getOper())) {
            return add(request, hotelVO);
        } else if ("edit".equals(hotelVO.getOper())) {
            return update(request, hotelVO);
        } else if ("del".equals(hotelVO.getOper())) {
            return delete(request, request.getParameter("id"));
        } else {
            return resultVO;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request,
                                      @Valid @ModelAttribute HotelVO hotelVO) {
            ResultVO resultVO = new ResultVO();
            hotelVO.setId(UUID.randomUUID().toString());
            hotelVO.setCreateTime(new Date());
            hotelVO.setUpdateTime(new Date());
            hotelMapper.insert(hotelVO);
            resultVO.setCode(0);
            return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute HotelVO hotelVO) {
        ResultVO resultVO = new ResultVO();
        hotelVO.setUpdateTime(new Date());
        hotelMapper.updateByPrimaryKeySelective(hotelVO);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable String userId) {
        ResultVO resultVO = new ResultVO();
        hotelMapper.deleteByPrimaryKey(userId);
        resultVO.setCode(0);
        return resultVO;
    }
}
