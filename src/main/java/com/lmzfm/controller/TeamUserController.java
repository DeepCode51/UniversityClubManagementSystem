package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.Team;
import com.lmzfm.model.TeamUser;
import com.lmzfm.model.UserInfo;
import com.lmzfm.service.ITeamService;
import com.lmzfm.service.ITeamUserService;
import com.lmzfm.util.JsonObject;
import com.lmzfm.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 社团成员管理 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"社团成员管理"})
@RestController
@RequestMapping("/teamuser")
public class TeamUserController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ITeamUserService teamUserService;
    @Resource
    private ITeamService teamService;

    @RequestMapping("/queryTeamUserList")
    public JsonObject queryTeamUserList(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int limit,
                                        String tel, String email,
                                        HttpServletRequest request) {
        //通过头部获取token
        String token = request.getHeader("token");
        //通过token获取用户id
        Integer userId = JwtUtil.getUserId(token);
        //创建返回对象模型
        JsonObject object = new JsonObject();
        //获取结果集
        PageInfo<TeamUser> pageInfo = teamUserService.queryPageInfo(page, limit, tel, email, userId);
        object.setCode(20000);
        object.setData(pageInfo.getList());
        object.setTotal(pageInfo.getTotal());
        return object;
    }


    @ApiOperation(value = "新增社团成员管理")
    @RequestMapping("/addInfo")
    public R add(@RequestBody TeamUser teamUser,
                 HttpServletRequest request){
        String token=request.getHeader("token");
        Integer userId=JwtUtil.getUserId(token);
        teamUser.setJoinTime(new Date());//当前时间  加入时间
        //根据用户id查询所属的社团id
        Team team=teamService.queryTeamInfoByUserId(userId);
        //把获取的对象id加入到社团用户的对象中
        teamUser.setTeamId(team.getId());
        int num= teamUserService.add(teamUser);

        if(num>0){
            return R.ok();
        }
        return R.fail("添加失败");
    }

    @ApiOperation(value = "删除社团成员管理")
    @RequestMapping("/deleteById")
    @Transactional
    public R delete(Long id) {
        TeamUser teamUser = teamUserService.getById(id);
        int num = teamUserService.delete(id);
        if (num > 0) {
            //社团人数减1
            Team team = teamService.getById(teamUser.getTeamId());
            team.setMember(team.getMember()-1);
            teamService.updateById(team);
            return R.ok();
        }
        return R.fail("删除失败");
    }


    @ApiOperation(value = "更新社团成员管理")
    @RequestMapping("/updateInfo")
    public R update(@RequestBody TeamUser teamUser) {
        int num = teamUserService.updateData(teamUser);
        if (num > 0) {
            return R.ok();
        }
        return R.fail("编辑失败");
    }

    @ApiOperation(value = "缴纳社团费用")
    @RequestMapping("/submitPay")
    public R submitPay(HttpServletRequest request) {
        String token=request.getHeader("token");
        Integer userId=JwtUtil.getUserId(token);
        TeamUser teamUser = new TeamUser();
        teamUser.setId(userId);
        TeamUser user = teamUserService.getById(userId);
        if ("已缴费".equals(user.getState())){
            return R.fail("已缴费，请勿重复缴费！");
        }
        teamUser.setState("已缴费");
        int num = teamUserService.updateData(teamUser);
        if (num > 0) {
            return R.ok();
        }
        return R.fail("缴费失败！");
    }

}
