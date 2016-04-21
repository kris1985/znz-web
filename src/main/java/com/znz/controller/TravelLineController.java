package com.znz.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.TTravelLineMapper;
import com.znz.model.TTravelLine;
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
@RequestMapping("/admin/travleLine")
public class TravelLineController {

    @Resource
    private TTravelLineMapper travelLineMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<TProduct> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                    @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                    @RequestParam(value = "sidx", required = false) String sidx,
                                                    @RequestParam(value = "sord", required = false) String sord,
                                                    @RequestParam(value = "lineType", required = true) String lineType,
                                                    @RequestParam(value = "filters", required = false) String filters) {
        System.out.println("-------------------------------"+lineType);
        TravelLineQueryQueryVO queryQueryVO = new TravelLineQueryQueryVO();
        queryQueryVO.setSortName(sidx);
        queryQueryVO.setSord(sord);
        if(StringUtils.isNotEmpty(filters)){
            SearchFilter searchFilter = JSON.parseObject(filters, SearchFilter.class);
            List<SearchField> rules = searchFilter.getRules();
            for (SearchField field:rules){
                if(StringUtils.isEmpty(field.getData())){
                    continue;
                }
                if(field.getField().equals("prodNo")){
                    queryQueryVO.setProdNo(field.getData());
                }else if(field.getField().equals("spzx")){
                    queryQueryVO.setSpzx(field.getData());
                }else if(field.getField().equals("days")){
                    queryQueryVO.setDays(field.getData());
                }else if(field.getField().equals("hotel")){
                    queryQueryVO.setHotel("%" + field.getData() + "%");
                }else if(field.getField().equals("jtfs")){
                    queryQueryVO.setJtfs("%" + field.getData() + "%");
                }
            }
        }
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
                Integer.parseInt(rows));
        queryQueryVO.setPage(pageParameter);
        List<TTravelLine> products = travelLineMapper.selectByPage(queryQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
                pageParameter.getTotalCount(), products);
        return jqGridData;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request,
                                       @Valid @ModelAttribute TravelLineVO hotelVO) {
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
                                      @Valid @ModelAttribute TravelLineVO hotelVO) {
            ResultVO resultVO = new ResultVO();
            hotelVO.setId(UUID.randomUUID().toString());
            hotelVO.setCreateTime(new Date());
            hotelVO.setUpdateTime(new Date());
            travelLineMapper.insert(hotelVO);
            resultVO.setCode(0);
            return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute TravelLineVO hotelVO) {
        ResultVO resultVO = new ResultVO();
        hotelVO.setUpdateTime(new Date());
        travelLineMapper.updateByPrimaryKeySelective(hotelVO);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable String userId) {
        ResultVO resultVO = new ResultVO();
        travelLineMapper.deleteByPrimaryKey(userId);
        resultVO.setCode(0);
        return resultVO;
    }
}
