package com.znz.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.TLinePriceMapper;
import com.znz.model.TLinePrice;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.znz.dao.THotelMapper;
import com.znz.model.THotel;
import com.znz.model.TProduct;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/linePrice")
public class LinePriceController {

    @Resource
    private TLinePriceMapper linePriceMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<TLinePrice> list2( @RequestParam(value = "lineId", required = false) String lineId) {
        List<TLinePrice> linePrices = linePriceMapper.selectByLineId(lineId);
        JqGridData jqGridData = new JqGridData(1, 1,
                linePrices.size(), linePrices);
        return jqGridData;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request,
                                       @Valid @ModelAttribute LinePriceVO linePriceVO) {
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }
        if ("add".equals(linePriceVO.getOper())) {
            return add(request, linePriceVO);
        } else if ("edit".equals(linePriceVO.getOper())) {
            return update(request, linePriceVO);
        } else if ("del".equals(linePriceVO.getOper())) {
            return delete(request, request.getParameter("id"));
        } else {
            return resultVO;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request,
                                      @Valid @ModelAttribute LinePriceVO linePriceVO) {
            ResultVO resultVO = new ResultVO();
            linePriceVO.setId(UUID.randomUUID().toString());
            linePriceVO.setCreateTime(new Date());
            linePriceVO.setUpdateTime(new Date());
            linePriceMapper.insert(linePriceVO);
            resultVO.setCode(0);
            return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute LinePriceVO linePriceVO) {
        ResultVO resultVO = new ResultVO();
        linePriceVO.setUpdateTime(new Date());
        linePriceMapper.updateByPrimaryKeySelective(linePriceVO);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable String userId) {
        ResultVO resultVO = new ResultVO();
        linePriceMapper.deleteByPrimaryKey(userId);
        resultVO.setCode(0);
        return resultVO;
    }
}
