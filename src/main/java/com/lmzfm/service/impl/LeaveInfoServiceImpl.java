package com.lmzfm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmzfm.dao.LeaveInfoMapper;
import com.lmzfm.model.LeaveInfo;
import com.lmzfm.service.ILeaveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 在线留言管理 服务实现类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Service
public class LeaveInfoServiceImpl extends ServiceImpl<LeaveInfoMapper, LeaveInfo> implements ILeaveInfoService {
    @Autowired
    private LeaveInfoMapper leaveDao;
    @Override
    public  IPage<LeaveInfo> findListByPage(Integer page, Integer pageCount){
        IPage<LeaveInfo> wherePage = new Page<>(page, pageCount);
        LeaveInfo where = new LeaveInfo();

        return   baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(LeaveInfo leaveInfo){
        return baseMapper.insert(leaveInfo);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(LeaveInfo leaveInfo){
        return baseMapper.updateById(leaveInfo);
    }

    @Override
    public LeaveInfo findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public PageInfo<LeaveInfo> queryList(int page, int limit, String tel) {
        PageHelper.startPage(page,limit);
        List<LeaveInfo> activities = leaveDao.queryLeaveInfoAll(tel);
        PageInfo<LeaveInfo> pageInfo=new PageInfo<>(activities);
        return pageInfo;
    }
}
