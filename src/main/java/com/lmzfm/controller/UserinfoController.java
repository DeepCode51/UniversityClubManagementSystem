package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.TeamUser;
import com.lmzfm.model.UserInfo;
import com.lmzfm.service.ITeamUserService;
import com.lmzfm.service.IUserinfoService;
import com.lmzfm.util.JsonObject;
import com.lmzfm.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 管理员信息表 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"管理员信息表"})
@RestController
@RequestMapping("/userinfo")
public class UserinfoController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private IUserinfoService userinfoService;
    @Resource
    private ITeamUserService teamUserService;

    @RequestMapping("/queryUserInfoList")
    public JsonObject queryUserInfoList(){
        List<UserInfo> list = userinfoService.list();
        JsonObject object=new JsonObject();
        object.setData(list);
        object.setCode(20000);
        return object;
    }

    @ApiOperation(value = "新增")
    @RequestMapping("/addInfo")
    public R add(@RequestBody UserInfo typeInfo){
        //新增后的返回参数  如果是1 成功 0 失败
        typeInfo.setPassword("123456");
        int num= userinfoService.add(typeInfo);
        if(num>0){
            return R.ok();
        }
        return R.fail("添加失败");
    }

    @ApiOperation(value = "删除")
    @RequestMapping("/deleteById")
    public R delete(Long id){
        int num= userinfoService.delete(id);
        if(num>0){
            return R.ok();
        }
        return R.fail("删除失败");
    }

    @ApiOperation(value = "更新")
    @RequestMapping("/updateInfo")
    public R update(@RequestBody UserInfo userInfo,HttpServletRequest request){
        String token=request.getHeader("token");
        Integer type= JwtUtil.getUserType(token);
        int num;
        if(type!=2){//如果是一个社团的管理员
            //获取当前用户账号的id
            num= userinfoService.updateData(userInfo);
        }else {
            TeamUser info = new TeamUser();
            info.setId(userInfo.getId());
            info.setUsername(userInfo.getUsername());
            info.setRealname(userInfo.getRealname());
            info.setSex(userInfo.getSex());
            info.setEmail(userInfo.getEmail());
            info.setTel(userInfo.getTel());
            num = teamUserService.updateData(info);
        }
        if(num>0){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    @RequestMapping("/queryUserInfoListAll")
    public JsonObject queryUserInfoListAll(String username,String tel,
                                           @RequestParam(defaultValue="1") int page,
                                           @RequestParam(defaultValue="15") int limit,
                                           HttpServletRequest request){
        String token=request.getHeader("token");
        Integer type= JwtUtil.getUserType(token);
        Integer userId=null;
        UserInfo info=new UserInfo();
        if(username!=null){
            info.setUsername(username);
        }
        if(tel!=null){
            info.setTel(tel);
        }
        if(type==0){//如果是一个社团的管理员
            //获取当前用户账号的id
            userId=JwtUtil.getUserId(token);
            info.setId(userId);
        }
        //创建返回结果集对象
        JsonObject jsonObject=new JsonObject();
        PageInfo<UserInfo> pageInfo = userinfoService.queryList(page,limit,info);
        jsonObject.setCode(20000);
        jsonObject.setTotal(pageInfo.getTotal());
        jsonObject.setData(pageInfo.getList());
        return jsonObject;
    }

    @ApiOperation(value = "个人信息")
    @RequestMapping("/queryUserInfo")
    public R queryUserInfo(HttpServletRequest request){
        String token=request.getHeader("token");
        Integer type= JwtUtil.getUserType(token);
        Integer userId=null;
        UserInfo info=new UserInfo();
        userId=JwtUtil.getUserId(token);

        if(type!=2){//如果是一个社团的管理员
            //获取当前用户账号的id
            info = userinfoService.getById(userId);
        }else {
            TeamUser user = teamUserService.getById(userId);
            info.setId(user.getId());
            info.setUsername(user.getUsername());
            info.setRealname(user.getRealname());
            info.setSex(user.getSex());
            info.setEmail(user.getEmail());
            info.setTel(user.getTel());
            info.setType(2);
        }
        return R.ok(info);
    }

    /**
     * 修改密码
     */
    @RequestMapping("/updatePassword")
    public R updatePassword(@RequestBody UserInfo userInfo){
        int num=userinfoService.updatePassWord(userInfo.getId(),userInfo.getPassword());
        if(num>0){
            return R.ok();
        }
        return R.fail("修改密码失败");
    }

}
