package com.xing.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xing.domain.ResultInfo;
import com.xing.domain.User;
import com.xing.service.UserService;
import com.xing.utils.BeanFactoryUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends BaseServlet {
    private UserService userService =(UserService) BeanFactoryUtils.getBean("UserService");;
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action.equals("register")){
            register(request,response);
        }else if(action.equals("login")){
            login(request,response);
        }else if(action.equals("getUserInfo")){
            getUserInfo(request,response);
        }else if (action.equals("logout")){
            logout(request,response);
        }else if(action.equals("act")){
            act(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }*/

//注册
    public void register(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
        ResultInfo info;
        try {
            //1、获取前端数据
            Map<String, String[]> map = request.getParameterMap();
            //2、封装数据到实体
            User user = new User();
            BeanUtils.populate(user,map);
            //3、调用service层
            boolean flag=userService.addUser(user);
            //生成成功标识
            info=new ResultInfo(true);
        } catch (Exception e) {
            e.printStackTrace();
            info=new ResultInfo(false,"服务器忙");
        }
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(info);
        System.out.println(s);
        response.getWriter().print(s);
    }
    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ResultInfo info;
        ObjectMapper om=new ObjectMapper();
        try {
            //校验验证码
            String ucode=request.getParameter("check");
            HttpSession session = request.getSession();
            String scode = (String)session.getAttribute("scode");
            session.removeAttribute("scode");
            if(ucode==null || "".equals(ucode)){
                info=new ResultInfo(false,"验证码不可为空");
                String s = om.writeValueAsString(info);
                response.getWriter().print(s);
                return;
            }
            if(scode==null || !ucode.equalsIgnoreCase(scode)){
                info=new ResultInfo(false,"验证码错误");
                String s = om.writeValueAsString(info);
                response.getWriter().print(s);
                return;
            }
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user,map);
            User userBean=userService.login(user);
            if(userBean==null){
                info=new ResultInfo(false,"用户名或密码错误");
                String s=om.writeValueAsString(info);
                response.getWriter().print(s);
            }else {
                if(userBean.getStatus().equals("Y")){
                    session.setAttribute("userInfo",userBean);
                    info = new ResultInfo(true);
                    String s=om.writeValueAsString(info);
                    response.getWriter().print(s);
                }else{
                    info = new ResultInfo(false,"账号未激活");
                    String s=om.writeValueAsString(info);
                    response.getWriter().print(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//获取用户登录状态
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ResultInfo info;
        HttpSession session = request.getSession();
        Object userInfo = session.getAttribute("userInfo");
        if(userInfo==null){
            info=new ResultInfo(false);
        }else{
            info=new ResultInfo(true);
            info.setData(userInfo);
        }
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(info);
        //System.out.println(s);
        response.getWriter().print(s);
    }
    //激活
    public void act(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String code = request.getParameter("code");
        boolean flag=userService.active(code);
        if(flag){
            response.sendRedirect(request.getContextPath()+"/login.html");
        }else{
            response.sendRedirect(request.getContextPath()+"/error/500.html");
        }
    }
    //退出
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //退出到首页
        try {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath()+"/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

