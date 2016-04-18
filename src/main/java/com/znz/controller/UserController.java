package com.znz.controller;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.Constants;
import com.znz.util.UserType;
import com.znz.vo.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserMapper     userMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody List<UserVO> list() {
        List<User> users = userMapper.selectAllUser(1);
        List<UserVO> userVOs = null;
        UserVO userVO = null;
        if (!CollectionUtils.isEmpty(users)) {
            userVOs = new ArrayList<UserVO>();
            for (User user : users) {
                userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userVOs.add(userVO);
            }
        }

        return userVOs;
    }

    @RequestMapping(value = "/list2")
    public @ResponseBody JqGridData<UserVO> list2(@RequestParam(value = "page", defaultValue = "1") String page,
                                                  @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                  @RequestParam(value = "sidx", required = false) String sidx,
                                                  @RequestParam(value = "sord", required = false) String sord,
                                                  @RequestParam(value = "searchField", required = false) String searchField) {
        UserQueryVO userQueryVO = new UserQueryVO();
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page), Integer.parseInt(rows));
        userQueryVO.setPage(pageParameter);
        List<User> users = userMapper.selectByPage(userQueryVO);
        List<UserVO> userlist = null;
        UserVO userVO;
        if (!CollectionUtils.isEmpty(users)) {
            userlist = new ArrayList<UserVO>();
            for (User user : users) {
                userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userlist.add(userVO);
            }
        }
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        System.out.println("pageParameter:" + pageParameter);
        JqGridData jqGridData = new JqGridData(total, pageParameter
            .getCurrentPage(),pageParameter.getPageSize(), userlist);
        return jqGridData;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int userId) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }else{
            userMapper.deleteByPrimaryKey(userId);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request, @Valid @ModelAttribute UserAddVO userAddVO) {
        if("add".equals(userAddVO.getOper())){
            return add(request,userAddVO);
        } else if("edit".equals(userAddVO.getOper())){
            return update(request, userAddVO);
        }else if("del".equals(userAddVO.getOper())){
            return delete(request, Integer.parseInt(request.getParameter("id")));
        }else {
            return null;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request, @Valid @ModelAttribute UserAddVO userAddVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }
        User temUser = userMapper.selectByUser(userAddVO.getUserName());
        if (temUser != null) {
            resultVO.setMsg("用户名已经存在，请使用新的用户名");
        }else{
            User user = new User();
            BeanUtils.copyProperties(userAddVO, user);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setUserType(UserType.NORMAL.getType());//普通用户
            user.setDownloadPerDay(0);
            user.setDownloadTotal(0);
            userMapper.insert(user);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.GET)
    public String modify(@PathVariable int userId, Model model) {
        User user = userMapper.selectByPrimaryKey(userId);
        model.addAttribute("user", user);
        return "/admin/userUpdate";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request, @Valid @ModelAttribute UserAddVO userAddVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        } else{
            User user = new User();
            BeanUtils.copyProperties(userAddVO, user);
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/auths/{userId}", method = RequestMethod.GET)
    public @ResponseBody List<AuthFileVO> listAuthByUser(HttpServletRequest request,
                                                         @PathVariable int userId) {
        String rootPath = request.getSession().getServletContext()
            .getRealPath(Constants.UPLOAD_ROOT_PATH);
        List<AuthFileVO> allAuths = getAuth(rootPath);
        List<AuthFileVO> retAuths = new ArrayList<AuthFileVO>();
        List<UserAuth> userAuths = userAuthMapper.listByUserId(userId);

        if (!CollectionUtils.isEmpty(userAuths)) {
            for (UserAuth userAuth : userAuths) {
                AuthFileVO authFileVO = new AuthFileVO();
                log.info("-1--" + authFileVO.getAuthName());
                authFileVO.setAuthName(userAuth.getFilePath());
                authFileVO.setChecked(1);
                authFileVO.setCheckBox("<input type=\"checkbox\"  name=\"auths\" checked value="
                                       + userAuth.getFilePath() + ">" + userAuth.getFilePath());
                if (!retAuths.contains(authFileVO)) {
                    retAuths.add(authFileVO);
                }
            }
        }
        if (!CollectionUtils.isEmpty(allAuths)) {
            for (AuthFileVO vo : allAuths) {
                log.info("-2--" + vo.getAuthName());
                vo.setCheckBox("<input type=\"checkbox\"  name=\"auths\"  value="
                        + vo.getAuthName() + ">" + vo.getAuthName());
                vo.setChecked(0);
                boolean contains = retAuths.contains(vo);
                if (!contains) {
                    retAuths.add(vo);
                }
            }
        }
            Collections.sort(retAuths, new Comparator<AuthFileVO>() {
                @Override
                public int compare(AuthFileVO o1, AuthFileVO o2) {
                    if (o1.getChecked() == o2.getChecked()) {
                        return 0;
                    } else if (o1.getChecked() < o2.getChecked()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        return retAuths;
    }

    @RequestMapping(value = "/auths", method = RequestMethod.GET)
    public @ResponseBody List<AuthFileVO> lisAlltAuth(HttpServletRequest request) {
        String rootPath = request.getSession().getServletContext()
            .getRealPath(Constants.UPLOAD_ROOT_PATH);
        List<AuthFileVO> auths = getAuth(rootPath);
        return auths;
    }

    private List<AuthFileVO> getAuth(String rootPath) {
        File root = new File(rootPath);
        File[] files = root.listFiles();
        List<AuthFileVO> auths = new ArrayList<AuthFileVO>();
        if (files != null && files.length > 0) {
            AuthFileVO vo = null;
            for (File f : files) {
                vo = new AuthFileVO();
                vo.setAuthName(f.getName());
                auths.add(vo);
            }
        }
        return auths;
    }

    private boolean checkPermisson(UserSession userSession) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            return false;
        }
        return true;
    }
}
