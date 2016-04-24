package com.znz.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.TTravelZyxMapper;
import com.znz.model.TTravelZyx;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.znz.dao.TTravelGtyMapper;
import com.znz.model.TTravelGty;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/travelZyx")
public class TravelZyxController {

    @Resource
    private TTravelZyxMapper travelZyxMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<TTravelZyx> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                      @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                      @RequestParam(value = "sidx", required = false) String sidx,
                                                      @RequestParam(value = "sord", required = false) String sord,
                                                      @RequestParam(value = "filters", required = false) String filters) {
        TravelZyxQueryVO queryQueryVO = new TravelZyxQueryVO();
        queryQueryVO.setSortName(sidx);
        queryQueryVO.setSord(sord);
        if (StringUtils.isNotEmpty(filters)) {
            SearchFilter searchFilter = JSON.parseObject(filters, SearchFilter.class);
            List<SearchField> rules = searchFilter.getRules();
            for (SearchField field : rules) {
                if (StringUtils.isEmpty(field.getData())) {
                    continue;
                }
                if (field.getField().equals("pch")) {
                    queryQueryVO.setPch("%" + field.getData() + "%");
                } else if (field.getField().equals("cpid")) {
                    queryQueryVO.setCpid("%" + field.getData() + "%");
                } else if (field.getField().equals("days")) {
                    queryQueryVO.setDays( field.getData() );
                } else if (field.getField().equals("jtfs")) {
                    queryQueryVO.setJtfs("%" + field.getData() + "%");
                } else if (field.getField().equals("cfd")) {
                    queryQueryVO.setCfd("%" + field.getData() + "%");
                } else if (field.getField().equals("mdd")) {
                    queryQueryVO.setMdd("%" + field.getData() + "%");
                }
            }
        }
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
            Integer.parseInt(rows));
        queryQueryVO.setPage(pageParameter);
        List<TTravelZyx> travelGties = travelZyxMapper.selectByPage(queryQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
            pageParameter.getTotalCount(), travelGties);
        return jqGridData;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(@RequestParam(value = "oper", defaultValue = "1") String oper,
                                       HttpServletRequest request,
                                       @ModelAttribute TTravelZyx travelZyx) {
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }
        if ("add".equals(oper)) {
            return add(request, travelZyx);
        } else if ("edit".equals(oper)) {
            return update(request, travelZyx);
        } else if ("del".equals(oper)) {
            return delete(request, travelZyx.getLineid());
        } else {
            return resultVO;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request,
                                      @Valid @ModelAttribute TTravelZyx travelZyx) {
        ResultVO resultVO = new ResultVO();
        travelZyx.setLineid(UUID.randomUUID().toString());
        travelZyx.setCjsj(new Date());
        travelZyx.setXgsj(new Date());
        travelZyxMapper.insert(travelZyx);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute TTravelZyx travelZyx) {
        ResultVO resultVO = new ResultVO();
        travelZyx.setXgsj(new Date());
        travelZyxMapper.updateByPrimaryKeySelective(travelZyx);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable String userId) {
        ResultVO resultVO = new ResultVO();
        travelZyxMapper.deleteByPrimaryKey(userId);
        resultVO.setCode(0);
        return resultVO;
    }
}
