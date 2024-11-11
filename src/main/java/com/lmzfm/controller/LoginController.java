package com.lmzfm.controller;

import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.TeamUser;
import com.lmzfm.model.UserInfo;
import com.lmzfm.service.ITeamUserService;
import com.lmzfm.service.IUserinfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class LoginController {
    @Resource
    private IUserinfoService userinfoService;
    @Resource
    private ITeamUserService teamUserService;

    @RequestMapping("/loginIn")
    public Map loginIn(@RequestBody UserInfo info){
        /**
         * 1、获取info对象值
         * 2、通过用户名 密码 类型查询当前的对象是否存在
         *    不存在 返回提示信息
         *    存在：生成token对象 然后返回前端
         */
        Map map=new HashMap();
        if(info.getType()!=2){//如果不是普通的社团成员
            UserInfo userinfo = userinfoService.queryUserInfoByUserNameAndPasswordAndType(info);
            if(userinfo!=null){//存在
                //生成token对象
                String token=JwtUtil.getJwtToken(userinfo);
                map.put("token",token);
                map.put("userId",userinfo.getId());
                map.put("username",userinfo.getUsername());
                map.put("type",userinfo.getType());
                Map map1=new HashMap();
                map1.put("data",map);
                map1.put("code",20000);
                return map1;
            }else{
                map.put("code",300);
                map.put("msg","用户名或密码不正确");
                return map;
            }

        }else{
            //获取用户名 密码 查询teamUser对象是否有当前账号，有登录成功，生成token
            TeamUser tu=teamUserService.queryByUserNameAndPwd(info.getUsername(),info.getPassword());
            if(tu!=null){
                //生成token对象
                UserInfo ui=new UserInfo();
                ui.setId(tu.getId());
                ui.setType(2);
                ui.setUsername(tu.getRealname());
                String token=JwtUtil.getJwtToken(ui);
                map.put("token",token);
                map.put("userId",tu.getId());
                map.put("username",tu.getRealname());
                map.put("type",2);
                Map map1=new HashMap();
                map1.put("data",map);
                map1.put("code",20000);
                return map1;
            }else {
                //如果没有 返回用户名称或密码错误
                map.put("code",300);
                map.put("msg","用户名或密码不正确");
                return map;
            }
        }
    }

    /**
     * 通过token获取用户对象和角色信息
     */
    @RequestMapping("/queryuserInfo")
    public Map queryuserInfo(String token){
        Map map=new HashMap();
        //通过接收到token
        Integer role=JwtUtil.getUserType(token);
        List list=new ArrayList();
        if(role==1){//管理员
            list.add("Admin");
        }else if(role==0){//社团管理员
            list.add("manager");
        }else{
            list.add("users");
        }
        map.put("code",20000);
        //存角色list

        Map data=new HashMap();
        data.put("roles",list);
        map.put("data",data);
        return map;
    }

    /**
     * 退出
     */
    @RequestMapping("/logout")
    public Map logout(){
        Map map=new HashMap();
        String token=null;
        map.put("code",20000);
        map.put("token",token);//
        return map;
    }


}
