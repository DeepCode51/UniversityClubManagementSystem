package com.lmzfm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lmzfm.model.CostList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
public interface ICostListService extends IService<CostList> {

    /**
     * 查询分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<CostList>
     */
    IPage<CostList> findListByPage(Integer page, Integer pageCount);

    /**
     * 添加
     *
     * @param costList
     * @return int
     */
    int add(CostList costList);

    /**
     * 删除
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改
     *
     * @param costList
     * @return int
     */
    int updateData(CostList costList);

    /**
     * id查询数据
     *
     * @param id id
     * @return CostList
     */
    CostList findById(Long id);

    PageInfo<CostList> queryCostListAll(String name,
                                        Integer teamId,
                                        Integer page,
                                        Integer limit);
}
