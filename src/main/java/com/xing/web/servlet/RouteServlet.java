package com.xing.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xing.domain.ResultInfo;
import com.xing.domain.Route;
import com.xing.service.RouteService;
import com.xing.utils.BeanFactoryUtils;
import com.xing.utils.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RouteServlet", urlPatterns = "/route")
public class RouteServlet extends BaseServlet {
    private RouteService service=(RouteService) BeanFactoryUtils.getBean("RouteService");
    //分页展示路线信息
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultInfo info;
        try {
            String rname = request.getParameter("rname");
            String cid = request.getParameter("cid");
            //int cid = Integer.parseInt(c);
            String pageNumber = request.getParameter("pageNumber");
            //int pageNumber= Integer.parseInt(pn);
            PageBean<Route> pb=service.findByPage(cid,pageNumber,rname);
            info=new ResultInfo(true);
            info.setData(pb);
        } catch (Exception e) {
            info=new ResultInfo(false,"功能维护中");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(info);
        //System.out.println(s);
        response.getWriter().print(s);
    }
    //查询路线详细信息
    public void findDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            String cid = request.getParameter("cid");
            String rid = request.getParameter("rid");
            //1、查询路线详细信息
            Route route=service.findDetail(cid,rid);
            info=new ResultInfo(true);
            info.setData(route);
        } catch (Exception e) {
            e.printStackTrace();
            info=new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        //System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }
    //排行榜条件查询
    public void findByRequired(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            String rname = request.getParameter("rname");
            String price1=request.getParameter("price1");
            String price2=request.getParameter("price2");
            int pageNumber=Integer.parseInt(request.getParameter("pageNumber"));
            //System.out.println(rname+"  "+price1+"  "+price2+"  "+pageNumber);
            int pageSize=4;
            PageBean<Route> pb=service.findByRequired(rname,price1,price2,pageSize,pageNumber);
            info=new ResultInfo(true);
            info.setData(pb);
        } catch (Exception e) {
            info=new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(info);
        System.out.println(s);
        response.getWriter().print(s);
    }
}

