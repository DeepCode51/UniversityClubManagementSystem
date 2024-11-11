package com.lmzfm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmzfm.model.ApplyList;

/**
 * <p>
 * 审核记录表 服务类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
public interface IApplyListService extends IService<ApplyList> {

    /**
     * 查询审核记录表分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<ApplyList>
     */
    IPage<ApplyList> findListByPage(Integer page, Integer pageCount);

    /**
     * 添加审核记录表
     *
     * @param applyList 审核记录表
     * @return int
     */
    int add(ApplyList applyList);

    /**
     * 删除审核记录表
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改审核记录表
     *
     * @param applyList 审核记录表
     * @return int
     */
    int updateData(ApplyList applyList);

    /**
     * id查询数据
     *
     * @param id id
     * @return ApplyList
     */
    ApplyList findById(Long id);
}
