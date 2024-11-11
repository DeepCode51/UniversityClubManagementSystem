package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.CostList;
import com.lmzfm.model.Team;
import com.lmzfm.service.ICostListService;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {""})
@RestController
@RequestMapping("/costlist")
public class CostListController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ICostListService costListService;
    @Resource
    private ITeamService teamService;

    @ApiOperation(value = "新增")
    @RequestMapping("/addInfo")
    public R add(@RequestBody CostList costList,
                 HttpServletRequest request){
        String token=request.getHeader("token");
        Integer userId= JwtUtil.getUserId(token);
        Team team=teamService.queryTeamInfoByUserId(userId);
        costList.setTeamId(team.getId());//获取当前社团id
        costList.setCtime(new Date());//当前时间

        int num= costListService.add(costList);
        if(num>0){
            return R.ok();
        }
        return R.fail("添加失败");
    }

    @ApiOperation(value = "删除")
    @RequestMapping("/deleteById")
    public R delete(Long id){
        int num= costListService.delete(id);
        if(num>0){
            return R.ok();
        }
        return R.fail("删除失败");
    }

    @ApiOperation(value = "更新")
    @RequestMapping("/updateInfo")
    public R update(@RequestBody CostList costList){
        int num= costListService.updateData(costList);
        if(num>0){
            return R.ok();
        }
        return R.fail("修改失败");
    }


    @RequestMapping("/queryCostList")
    public JsonObject queryCostList(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "15") Integer limit,
                                    String name,
                                    HttpServletRequest request){
        String token=request.getHeader("token");
        Integer userId= JwtUtil.getUserId(token);
        Team team=teamService.queryTeamInfoByUserId(userId);
        Integer teamId=null;
        if(team!=null){
            teamId=team.getId();
        }
        JsonObject object=new JsonObject();
        PageInfo<CostList> pageInfo = costListService.queryCostListAll(name,teamId ,page, limit);
        object.setCode(20000);
        object.setTotal(pageInfo.getTotal());
        object.setData(pageInfo.getList());
        return object;
    }

}
