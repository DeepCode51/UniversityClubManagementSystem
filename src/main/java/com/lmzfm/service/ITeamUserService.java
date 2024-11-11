package com.lmzfm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lmzfm.model.TeamUser;

/**
 * <p>
 * 社团成员管理 服务类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
public interface ITeamUserService extends IService<TeamUser> {

    /**
     * 查询社团成员管理分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<TeamUser>
     */
    IPage<TeamUser> findListByPage(Integer page, Integer pageCount);

    /**
     * 添加社团成员管理
     *
     * @param teamUser 社团成员管理
     * @return int
     */
    int add(TeamUser teamUser);

    /**
     * 删除社团成员管理
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改社团成员管理
     *
     * @param teamUser 社团成员管理
     * @return int
     */
    int updateData(TeamUser teamUser);

    /**
     * id查询数据
     *
     * @param id id
     * @return TeamUser
     */
    TeamUser findById(Long id);

    PageInfo<TeamUser> queryPageInfo(int page, int limit,
                                     String tel, String email, Integer userId);

    TeamUser queryTeamUserByTeamIdAndTel( int teamId,
                                          String tel);

    TeamUser queryByUserNameAndPwd(String username,
                                   String password);
}
