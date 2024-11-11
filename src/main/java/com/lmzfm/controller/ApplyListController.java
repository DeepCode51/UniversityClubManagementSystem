package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lmzfm.model.ApplyList;
import com.lmzfm.service.IApplyListService;
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
 * 审核记录表 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"审核记录表"})
@RestController
@RequestMapping("/apply-list")
public class ApplyListController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private IApplyListService applyListService;


    @ApiOperation(value = "新增审核记录表")
    @PostMapping()
    public int add(@RequestBody ApplyList applyList){
        return applyListService.add(applyList);
    }

    @ApiOperation(value = "删除审核记录表")
    @DeleteMapping("{id}")
    public int delete(@PathVariable("id") Long id){
        return applyListService.delete(id);
    }

    @ApiOperation(value = "更新审核记录表")
    @PutMapping()
    public int update(@RequestBody ApplyList applyList){
        return applyListService.updateData(applyList);
    }

    @ApiOperation(value = "查询审核记录表分页数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码"),
        @ApiImplicitParam(name = "pageCount", value = "每页条数")
    })
    @GetMapping()
    public IPage<ApplyList> findListByPage(@RequestParam Integer page,
                                   @RequestParam Integer pageCount){
        return applyListService.findListByPage(page, pageCount);
    }

    @ApiOperation(value = "id查询审核记录表")
    @GetMapping("{id}")
    public ApplyList findById(@PathVariable Long id){
        return applyListService.findById(id);
    }

}
