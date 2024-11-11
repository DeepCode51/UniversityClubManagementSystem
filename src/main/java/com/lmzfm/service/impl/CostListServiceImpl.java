package com.lmzfm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmzfm.dao.CostListMapper;
import com.lmzfm.model.CostList;
import com.lmzfm.service.ICostListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Service
public class CostListServiceImpl extends ServiceImpl<CostListMapper, CostList> implements ICostListService {
    @Autowired
    private CostListMapper costListDao;
    @Override
    public  IPage<CostList> findListByPage(Integer page, Integer pageCount){
        IPage<CostList> wherePage = new Page<>(page, pageCount);
        CostList where = new CostList();

        return   baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(CostList costList){
        return baseMapper.insert(costList);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(CostList costList){
        return baseMapper.updateById(costList);
    }

    @Override
    public CostList findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public PageInfo<CostList> queryCostListAll(String name, Integer teamId, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<CostList> costLists = costListDao.queryCostListAll(name, teamId);
        PageInfo<CostList> pageInfo=new PageInfo<>(costLists);
        return pageInfo;
    }
}
