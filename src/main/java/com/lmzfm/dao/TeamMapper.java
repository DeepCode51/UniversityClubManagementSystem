package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.Team;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 社团信息管理 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("teamDao")
public interface TeamMapper extends BaseMapper<Team> {
    List<Team> queryTeamList(Team team);

    Team queryTeamInfoByUserId(@Param("userId") Integer userId);

    Team queryById(Long id);

    List<Team> queryMyteamList(@Param("tel") String tel);

}
