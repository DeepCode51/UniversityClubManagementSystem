package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.lmzfm.jwt.JwtUtil;
import com.lmzfm.model.Team;
import com.lmzfm.model.TeamUser;
import com.lmzfm.model.TypeInfo;
import com.lmzfm.service.ITeamService;
import com.lmzfm.service.ITeamUserService;
import com.lmzfm.service.ITypeInfoService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 社团信息管理 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"社团信息管理"})
@RestController
@RequestMapping("/team")
public class TeamController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ITeamService teamService;
    @Resource
    private ITeamUserService teamUserService;
    @Resource
    private ITypeInfoService typeInfoService;
    @Resource
    private IUserinfoService userinfoService;

    @RequestMapping("/queryTeamList")
    public JsonObject queryTeamList(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int limit,
                                    Team team){
        //创建返回对象模型
        JsonObject object=new  JsonObject();
        //获取结果集
        PageInfo<Team> pageInfo = teamService.queryTeamList(page, limit, team);
        object.setCode(20000);
        object.setData(pageInfo.getList());
        object.setTotal(pageInfo.getTotal());
        return object;
    }
    @RequestMapping("/fileUpload")
    public R fileUpload(@RequestParam(value = "file")
                        MultipartFile file){
        //判断上传内容是否为空
        if(file.isEmpty()){
            System.out.println("图片不存在");
        }
        //获取图片名称
        String fileName = file.getOriginalFilename();
        //获取文件的后缀
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        //随机生成一个文件名称
        fileName= UUID.randomUUID()+suffixName;
        //设置文件上传路径
        String filePath="/Users/abc/Desktop/images/";
        //上传
        File dest=new File(filePath,fileName);
        //判断文件路径是否存在，不存在 创建
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
        } catch (IOException ioException) {
            System.out.println(ioException);
            System.out.println("上传出错");
        }
        //设置最终的文件名称 返回前端
        String name="/images/"+fileName;
        return R.ok(name,null);
    }

    @ApiOperation(value = "新增社团信息管理")
    @RequestMapping("/addInfo")
    public R add(@RequestBody Team team){

        int num=teamService.add(team);
        if(num>0){
            return R.ok();
        }
        return R.fail("添加失败");
    }

    @ApiOperation(value = "删除社团信息管理")
    @RequestMapping("/deleteByID")
    public R delete( Long id){

        int num= teamService.delete(id);
        if(num>0){
            return R.ok();
        }
        return R.fail("删除失败");
    }

    @ApiOperation(value = "更新社团信息管理")
    @RequestMapping("/updateInfo")
    public R update(@RequestBody Team team){
        int num= teamService.updateData(team);
        if(num>0){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    @ApiOperation(value = "查询社团信息管理分页数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码"),
        @ApiImplicitParam(name = "pageCount", value = "每页条数")
    })
    @GetMapping()
    public IPage<Team> findListByPage(@RequestParam Integer page,
                                   @RequestParam Integer pageCount){
        return teamService.findListByPage(page, pageCount);
    }

    @ApiOperation(value = "id查询社团信息管理")
    @GetMapping("{id}")
    public Team findById(@PathVariable Long id){
        return teamService.findById(id);
    }

    /*
     ***********
     * 以下是前端页面所需接口
     */
    @RequestMapping("/queryTeamAll")
    public JsonObject queryTeamList(){
        JsonObject object=new  JsonObject();
        PageInfo<Team> pageInfo = teamService.queryTeamList(1, 100, null);
        object.setCode(20000);
        object.setData(pageInfo.getList());
        object.setTotal(pageInfo.getTotal());
        return object;
    }


    @RequestMapping("/findTeamById")
    public Map findTeamById(int id){
        Team team = teamService.findById(new Long(id));
        if (team.getType()!=null){
            TypeInfo byId = typeInfoService.getById(team.getType());
            team.setTypeName(byId.getName());
        }
        team.setUserInfo(userinfoService.getById(team.getUserId()));
        Map map=new HashMap();
        map.put("code",20000);
        map.put("data",team);
        return map;
    }
    @RequestMapping("/queryMyTeamList")
    public JsonObject queryMyTeamList(HttpServletRequest request){
        String token=request.getHeader("token");
        Integer userId= JwtUtil.getUserId(token);
        //根据用户id 获取用户对象
        TeamUser teamUser=teamUserService.getById(userId);
        //通过对象获取电话
        List<Team> list= teamService.queryMyteamList(teamUser.getTel());
        //创建返回结果集对象
        JsonObject jsonObject=new JsonObject();
        jsonObject.setCode(20000);
        jsonObject.setData(list);
        return jsonObject;
    }

    @ApiOperation(value = "开启或关闭缴费功能")
    @RequestMapping("/open")
    public R open(String pay,HttpServletRequest request){
        String token=request.getHeader("token");
        Integer userId= JwtUtil.getUserId(token);
        //根据用户id 获取用户对象
        TeamUser teamUser=teamUserService.getById(userId);
        Team team = teamService.getById(teamUser.getTeamId());
        if (pay.equals("")){
            System.out.println("空");
            return R.ok(team.getPay());
        }
        team.setPay(pay);
        int num= teamService.updateData(team);
        if(num>0){
            return R.ok(team.getPay());
        }
        return R.fail("修改失败");
    }


}

