package com.lmzfm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmzfm.dao.ActivityMapper;
import com.lmzfm.model.Activity;
import com.lmzfm.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 社团活动管理 服务实现类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Autowired
    private ActivityMapper activityDao;
    @Override
    public  IPage<Activity> findListByPage(Integer page, Integer pageCount){
        IPage<Activity> wherePage = new Page<>(page, pageCount);
        Activity where = new Activity();

        return   baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(Activity activity){
        return baseMapper.insert(activity);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(Activity activity){
        return baseMapper.updateById(activity);
    }

    @Override
    public Activity findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public PageInfo<Activity> queryList(int page, int limit, String name, String tel, Integer userId) {
        PageHelper.startPage(page,limit);
        List<Activity> activities = activityDao.queryActivityInfoList(name, tel, userId);
        PageInfo<Activity> pageInfo=new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public boolean updateStatus(int status, int id) {
        int num= activityDao.updateStatusById(status,id);
        if(num>0){
            return true;
        }
        return false;
    }

    @Override
    public Activity findByTeamId(int teamId) {
        return activityDao.findByTeamId(teamId);
    }
}
