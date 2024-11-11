package com.lmzfm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmzfm.dao.TeamMapper;
import com.lmzfm.model.Team;
import com.lmzfm.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 社团信息管理 服务实现类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements ITeamService {

    @Autowired
    private TeamMapper teamDao;
    @Override
    public  IPage<Team> findListByPage(Integer page, Integer pageCount){
        IPage<Team> wherePage = new Page<>(page, pageCount);
        Team where = new Team();

        return   baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(Team team){
        return baseMapper.insert(team);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(Team team){
        return baseMapper.updateById(team);
    }

    @Override
    public Team findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public PageInfo<Team> queryTeamList(int page, int limit, Team team) {
        PageHelper.startPage(page,limit);
        //获取记录集合
        List<Team> teams = teamDao.queryTeamList(team);
        PageInfo<Team> pageInfo=new PageInfo<>(teams);
        return pageInfo;
    }

    @Override
    public Team queryTeamInfoByUserId(Integer userId) {
        return teamDao.queryTeamInfoByUserId(userId);
    }
    @Override
    public List<Team> queryMyteamList(String tel) {
        return teamDao.queryMyteamList(tel);
    }
}
