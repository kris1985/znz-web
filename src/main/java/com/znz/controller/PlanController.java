package com.znz.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.TPlanMapper;
import com.znz.model.TPlan;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.znz.dao.TTravelZyxMapper;
import com.znz.model.TTravelZyx;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/plan")
public class PlanController {

    @Resource
    private TPlanMapper planMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<TPlan> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                      @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                      @RequestParam(value = "sidx", required = false) String sidx,
                                                      @RequestParam(value = "sord", required = false) String sord,
                                                      @RequestParam(value = "filters", required = false) String filters) {
        PlanQueryVO queryQueryVO = new PlanQueryVO();
        queryQueryVO.setSortName(sidx);
        queryQueryVO.setSord(sord);
        if (StringUtils.isNotEmpty(filters)) {
            SearchFilter searchFilter = JSON.parseObject(filters, SearchFilter.class);
            List<SearchField> rules = searchFilter.getRules();
            for (SearchField field : rules) {
                if (StringUtils.isEmpty(field.getData())) {
                    continue;
                }
                if (field.getField().equals("name")) {
                    queryQueryVO.setName("%" + field.getData() + "%");
                } else if (field.getField().equals("pt")) {
                    queryQueryVO.setPt("%" + field.getData() + "%");
                } else if (field.getField().equals("hyrq")) {
                    queryQueryVO.setHyrq("%" + field.getData() + "%");
                } else if (field.getField().equals("mdd")) {
                    queryQueryVO.setMdd("%" + field.getData() + "%");
                } else if (field.getField().equals("lx")) {
                    queryQueryVO.setLx("%" + field.getData() + "%");
                }else if (field.getField().equals("jhrq")) {
                    queryQueryVO.setJhrq("%" + field.getData() + "%");
                }
            }
        }
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
            Integer.parseInt(rows));
        queryQueryVO.setPage(pageParameter);
        List<TTravelZyx> travelGties = planMapper.selectByPage(queryQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
            pageParameter.getTotalCount(), travelGties);
        return jqGridData;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(@RequestParam(value = "oper", defaultValue = "1") String oper,
                                       HttpServletRequest request,
                                       @ModelAttribute TPlan plan) {
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }
        if ("add".equals(oper)) {
            return add(request, plan);
        } else if ("edit".equals(oper)) {
            return update(request, plan);
        } else if ("del".equals(oper)) {
            return delete(request, request.getParameter("id"));
        } else {
            return resultVO;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request,
                                      @Valid @ModelAttribute TPlan  plan) {
        ResultVO resultVO = new ResultVO();
        plan.setUid(UUID.randomUUID().toString());
        plan.setCjsj(new Date());
        plan.setCgsj(new Date());
        planMapper.insert(plan);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute TPlan  plan) {
        ResultVO resultVO = new ResultVO();
        plan.setCgsj(new Date());
        planMapper.updateByPrimaryKeySelective(plan);
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable String userId) {
        ResultVO resultVO = new ResultVO();
        planMapper.deleteByPrimaryKey(userId);
        resultVO.setCode(0);
        return resultVO;
    }
}
