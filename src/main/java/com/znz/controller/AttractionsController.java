package com.znz.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.TAttractionsMapper;
import com.znz.model.TAttractions;
import lombok.extern.slf4j.Slf4j;

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
@RequestMapping("/admin/attractions")
public class AttractionsController {

    @Resource
    private TAttractionsMapper attractionsMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<ProductVO> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                     @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                     @RequestParam(value = "sidx", required = false) String sidx,
                                                     @RequestParam(value = "sord", required = false) String sord,
                                                     @RequestParam(value = "searchField", required = false) String searchField) {
        AttractionsQueryVO attractionsQueryVO = new AttractionsQueryVO();
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
            Integer.parseInt(rows));
        attractionsQueryVO.setPage(pageParameter);
        List<TAttractions> products = attractionsMapper.selectByPage(attractionsQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
            pageParameter.getPageSize(), products);
        return jqGridData;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request,
                                       @Valid @ModelAttribute AttractionsVO attractionsVO) {
        if ("add".equals(attractionsVO.getOper())) {
            return add(request, attractionsVO);
        } else if ("edit".equals(attractionsVO.getOper())) {
            return update(request, attractionsVO);
        } else if ("del".equals(attractionsVO.getOper())) {
            return delete(request, Integer.parseInt(request.getParameter("id")));
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request,
                                      @Valid @ModelAttribute AttractionsVO attractionsVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        }
        TAttractions attractions = attractionsMapper.selectByProdName(attractionsVO.getId());
        if (attractions != null) {
            resultVO.setMsg("景点名称已经存在，请使用新的景点名称");
        } else {
            attractionsVO.setCreateTime(new Date());
            attractionsVO.setUpdateTime(new Date());
            attractionsMapper.insert(attractionsVO);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request,
                                         @Valid @ModelAttribute AttractionsVO attractionsVO) {
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        } else {
            attractionsVO.setUpdateTime(new Date());
            attractionsMapper.updateByPrimaryKeySelective(attractionsVO);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int userId) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
                Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            resultVO.setMsg("无权限操作");
        } else {
            attractionsMapper.deleteByPrimaryKey(userId);
            resultVO.setCode(0);
        }
        return resultVO;
    }
}
