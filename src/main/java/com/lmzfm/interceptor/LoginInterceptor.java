package com.lmzfm.interceptor;

import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor
        implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        //拦截器进行判断放行
        if(request.getMethod().toUpperCase().equals("OPTIONS")){
            return true;
        }

        //如果是图片
        if(request.getRequestURI().contains("images")){
            return true;
        }
        //添加社团
        if(request.getRequestURI().contains("/applyInfo/addInfo")){
            return true;
        }

        //添加留言
        //添加社团
        if(request.getRequestURI().contains("/leaveinfo/addInfo")){
            return true;
        }

        if(request.getRequestURI().contains("fileUpload")){
            return true;
        }

        if(request.getRequestURI().contains("queryTeamAll")){
            return true;
        }
        if(request.getRequestURI().contains("queryTypeList")){
            return true;
        }
        if(request.getRequestURI().contains("queryTeamList")){
            return true;
        }
        if(request.getRequestURI().contains("findTeamById")){
            return true;
        }

        if(request.getRequestURI().contains("findByTeamId")){
            return true;
        }
        //获取token
        String token=request.getHeader("token");
        if(token==null){//没有token
            return false;
        }else{//存在
            //是否有效
            boolean bs=JwtUtil.checkToken(token);
            if(bs){//如果有效
                //token续期
                UserInfo info=new UserInfo();
                info.setType(JwtUtil.getUserType(token));
                info.setId(JwtUtil.getUserId(token));
                info.setUsername(JwtUtil.getMemberIdByJwtToken(token));
                token=JwtUtil.getJwtToken(info);
                response.addHeader("token",token);
                return true;
            }else{
                response.setHeader("code","50008");
                return false;
            }

        }

    }
}
