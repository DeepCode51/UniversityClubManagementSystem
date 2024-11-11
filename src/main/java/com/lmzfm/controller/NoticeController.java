package com.lmzfm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lmzfm.model.Notice;
import com.lmzfm.service.INoticeService;
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
 * 公告信息 前端控制器
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Api(tags = {"公告信息"})
@RestController
@RequestMapping("/notice")
public class NoticeController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private INoticeService noticeService;


    @ApiOperation(value = "新增公告信息")
    @PostMapping()
    public int add(@RequestBody Notice notice){
        return noticeService.add(notice);
    }

    @ApiOperation(value = "删除公告信息")
    @DeleteMapping("{id}")
    public int delete(@PathVariable("id") Long id){
        return noticeService.delete(id);
    }

    @ApiOperation(value = "更新公告信息")
    @PutMapping()
    public int update(@RequestBody Notice notice){
        return noticeService.updateData(notice);
    }

    @ApiOperation(value = "查询公告信息分页数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码"),
        @ApiImplicitParam(name = "pageCount", value = "每页条数")
    })
    @GetMapping()
    public IPage<Notice> findListByPage(@RequestParam Integer page,
                                   @RequestParam Integer pageCount){
        return noticeService.findListByPage(page, pageCount);
    }

    @ApiOperation(value = "id查询公告信息")
    @GetMapping("{id}")
    public Notice findById(@PathVariable Long id){
        return noticeService.findById(id);
    }

}
