package com.xing.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xing.domain.ResultInfo;
import com.xing.service.CateService;
import com.xing.utils.BeanFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CategoryServlet", urlPatterns = "/category")
public class CategoryServlet extends BaseServlet {
    private CateService service=(CateService) BeanFactoryUtils.getBean("CateService");
    public void findAllCate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ResultInfo info = null;
        try {
            String jsonList=service.findAllCate();
            if(jsonList!=null){
                info=new ResultInfo(true);
                info.setData(jsonList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            info=new ResultInfo(false,"此功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        response.getWriter().print(infoJson);
    }
    //分页
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ResultInfo info = new ResultInfo(true);
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(info);
        response.getWriter().print(s);
    }
}

