package com.znz.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
@RequestMapping("/admin/product")
public class ProductController {

    @Resource
    private TProductMapper productMapper;

    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<ProductVO> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                     @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                     @RequestParam(value = "sidx", required = false) String sidx,
                                                     @RequestParam(value = "sord", required = false) String sord,
                                                     @RequestParam(value = "searchField", required = false) String searchField) {
        ProductQueryVO productQueryVO = new ProductQueryVO();
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page),
            Integer.parseInt(rows));
        productQueryVO.setPage(pageParameter);
        List<TProduct> products = productMapper.selectByPage(productQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter.getCurrentPage(),
            pageParameter.getPageSize(), products);
        return jqGridData;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int userId) {
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

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request,
                                       @Valid @ModelAttribute ProductVO productVO) {
        if ("add".equals(productVO.getOper())) {
            return add(request, productVO);
        } else if ("edit".equals(productVO.getOper())) {
            return update(request, productVO);
        } else if ("del".equals(productVO.getOper())) {
            return delete(request, Integer.parseInt(request.getParameter("id")));
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

}
