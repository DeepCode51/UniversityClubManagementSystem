package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.lmzfm.model.TypeInfo;
import com.lmzfm.service.ITypeInfoService;
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

/**
 * <p>
 * 社团分类和介绍 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"社团分类和介绍"})
@RestController
@RequestMapping("/typeInfo")
//@CrossOrigin
public class TypeInfoController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ITypeInfoService typeInfoService;

    @RequestMapping("queryTypeList")
    public JsonObject queryTypeList(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "15") Integer limit,
                                    String name){
        JsonObject object=new JsonObject();
        TypeInfo info=new TypeInfo();
        info.setName(name);
        PageInfo<TypeInfo> pageInfo=typeInfoService.queryTypeList(page,limit,info);
        object.setCode(20000);
        object.setTotal(pageInfo.getTotal());
        object.setData(pageInfo.getList());
        return object;
    }

    @ApiOperation(value = "新增社团分类和介绍")
    @RequestMapping("/addInfo")
    public R add(@RequestBody TypeInfo typeInfo){
        int num= typeInfoService.add(typeInfo);
        if(num>0){
            return R.ok();
        }
        return R.fail("类型添加失败");
    }

    @ApiOperation(value = "删除社团分类和介绍")
    @RequestMapping("/deleteById")
    public R delete(Long id){
        int num= typeInfoService.delete(id);
        if(num>0){
            return R.ok();
        }
        return R.fail("删除失败");
    }

    @ApiOperation(value = "更新社团分类和介绍")
    @RequestMapping("/updateInfo")
    public R update(@RequestBody TypeInfo typeInfo){
        int num= typeInfoService.updateData(typeInfo);
        if(num>0){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    @ApiOperation(value = "查询社团分类和介绍分页数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码"),
        @ApiImplicitParam(name = "pageCount", value = "每页条数")
    })
    @GetMapping()
    public IPage<TypeInfo> findListByPage(@RequestParam Integer page,
                                   @RequestParam Integer pageCount){
        return typeInfoService.findListByPage(page, pageCount);
    }

    @ApiOperation(value = "id查询社团分类和介绍")
    @GetMapping("{id}")
    public TypeInfo findById(@PathVariable Long id){
        return typeInfoService.findById(id);
    }

}
