package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.Activity;
import com.lmzfm.model.Team;
import com.lmzfm.service.IActivityService;
import com.lmzfm.service.ITeamService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 社团活动管理 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"社团活动管理"})
@RestController
@RequestMapping("/activity")
public class ActivityController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private IActivityService activityService;

    @Resource
    private ITeamService teamService;

    @RequestMapping("/queryActivityInfoList")
    public JsonObject queryActivityInfoList(String name, String tel,
                                             @RequestParam(defaultValue="1") int page,
                                             @RequestParam(defaultValue="20") int limit,
                                             HttpServletRequest request){
        String token=request.getHeader("token");
        Integer type= JwtUtil.getUserType(token);
        Integer userId=null;
        if(type==0){//如果是一个社团的管理员
            //获取当前用户账号的id
            userId=JwtUtil.getUserId(token);
        }
        //创建返回结果集对象
        JsonObject jsonObject=new JsonObject();
        PageInfo<Activity> pageInfo = activityService.queryList(page,limit,name,tel,userId);
        jsonObject.setCode(20000);
        jsonObject.setTotal(pageInfo.getTotal());
        jsonObject.setData(pageInfo.getList());
        return jsonObject;
    }

    @RequestMapping("/updateStatusById")
    public R updateStatusById(int id){
        int status=1;//默认只要审核就通过
        boolean b = activityService.updateStatus(status, id);
        if(b){
            return R.ok();
        }
        return  R.fail("失败");
    }

    @ApiOperation(value = "新增社团活动管理")
    @RequestMapping("/addInfo")
    public R add(@RequestBody Activity activity,
                 HttpServletRequest request){
        String token=request.getHeader("token");
        Integer userId=JwtUtil.getUserId(token);
        Team team=teamService.queryTeamInfoByUserId(userId);
        activity.setTeamId(team.getId());
        activity.setCreateTime(new Date());
        activity.setUserId(userId);
        activity.setStatus(0);
        int num= activityService.add(activity);
        if(num>0){
            return R.ok();
        }
        return  R.fail("失败");
    }

    @ApiOperation(value = "删除社团活动管理")
    @RequestMapping("/deleteById")
    public R delete(Long id){
        int num= activityService.delete(id);
        if(num>0){
            return R.ok();
        }
        return  R.fail("失败");

    }

    @ApiOperation(value = "更新社团活动管理")
    @RequestMapping("/updateInfo")
    public R update(@RequestBody Activity activity){
        int num= activityService.updateData(activity);
        if(num>0){
            return R.ok();
        }
        return  R.fail("失败");
    }

    @RequestMapping("/findByTeamId")
    public Map findByTeamId(int id){
        Activity act = activityService.findByTeamId(id);
        Map map=new HashMap();
        map.put("code",20000);
        map.put("data",act);
        return map;
    }


}
