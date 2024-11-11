package com.lmzfm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lmzfm.model.Team;

import java.util.List;

/**
 * <p>
 * 社团信息管理 服务类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
public interface ITeamService extends IService<Team> {

    /**
     * 查询社团信息管理分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<Team>
     */
    IPage<Team> findListByPage(Integer page, Integer pageCount);

    /**
     * 添加社团信息管理
     *
     * @param team 社团信息管理
     * @return int
     */
    int add(Team team);

    /**
     * 删除社团信息管理
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改社团信息管理
     *
     * @param team 社团信息管理
     * @return int
     */
    int updateData(Team team);

    /**
     * id查询数据
     *
     * @param id id
     * @return Team
     */
    Team findById(Long id);


    PageInfo<Team> queryTeamList(int page, int limit, Team team);

    Team queryTeamInfoByUserId(Integer userId);

    List<Team> queryMyteamList(String tel);
}
