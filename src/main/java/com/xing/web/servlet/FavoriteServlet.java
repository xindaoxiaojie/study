package com.xing.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xing.dao.RouteDao;
import com.xing.domain.Favorite;
import com.xing.domain.ResultInfo;
import com.xing.domain.Route;
import com.xing.domain.User;
import com.xing.service.FavoriteService;
import com.xing.utils.BeanFactoryUtils;
import com.xing.utils.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FavoriteServlet", urlPatterns = "/favorite")
public class FavoriteServlet extends BaseServlet {
    private FavoriteService service = (FavoriteService) BeanFactoryUtils.getBean("FavoriteService");

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        //1、判断是否登录
        try {
            User userInfo = (User) request.getSession().getAttribute("userInfo");
            if (userInfo == null) {
                info = new ResultInfo(false);
                info.setData(-1);
                ObjectMapper om = new ObjectMapper();
                String s = om.writeValueAsString(info);
                response.getWriter().print(s);
                return;
            }
            String rid = request.getParameter("rid");
            int uid = userInfo.getUid();
            boolean flag = service.addFavorite(rid, uid);
            RouteDao routeDao = (RouteDao) BeanFactoryUtils.getBean("RouteDao");
            Route route = routeDao.findRouteByRid(rid);
            info = new ResultInfo(flag);
            System.out.println(route.getCount());
            info.setData(route.getCount());
            ObjectMapper om = new ObjectMapper();
            String s = om.writeValueAsString(info);
            response.getWriter().print(s);
        } catch (Exception e) {
            info = new ResultInfo(false, "当前功能正在维护");
            ObjectMapper om = new ObjectMapper();
            String s = om.writeValueAsString(info);
            response.getWriter().print(s);
        }

    }

    //查询该路线是否被收藏
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            String rid = request.getParameter("rid");
            User userInfo = (User) request.getSession().getAttribute("userInfo");
            if (userInfo != null) {
                boolean flag = service.isFavorite(rid, userInfo.getUid());
                info = new ResultInfo(flag);
                ObjectMapper om = new ObjectMapper();
                String s = om.writeValueAsString(info);
                response.getWriter().print(s);
            } else {
                info = new ResultInfo(false);
                ObjectMapper om = new ObjectMapper();
                String s = om.writeValueAsString(info);
                response.getWriter().print(s);
            }
        } catch (Exception e) {
            info = new ResultInfo(false, "当前功能正在维护");
            ObjectMapper om = new ObjectMapper();
            String s = om.writeValueAsString(info);
            response.getWriter().print(s);
        }
    }

    //按收藏数分页展示收藏
    public void findFavoriteByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            String pageNumber = request.getParameter("pageNumber");
            int pageSize = 8;
            PageBean<Route> pb = service.findFavoriteByPage(Integer.parseInt(pageNumber), pageSize);
            info = new ResultInfo(true);
            info.setData(pb);
        } catch (Exception e) {
            info = new ResultInfo(false, "此功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(info);
        //System.out.println(s);
        response.getWriter().print(s);
    }

    //分页展示我的收藏
    public void findMyFavoriteByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            String pageNumber = request.getParameter("pageNumber");
            User userInfo = (User) request.getSession().getAttribute("userInfo");
            int uid = userInfo.getUid();
            int pageSize = 4;
            PageBean<Favorite> pb = service.findMyFavoriteByPage(Integer.parseInt(pageNumber), pageSize, uid);
            info = new ResultInfo(true);
            info.setData(pb);
        } catch (Exception e) {
            info = new ResultInfo(false, "当前功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(info);
        //System.out.println(s);
        response.getWriter().print(s);
    }
}

