package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.TeamUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 社团成员管理 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("teamUserDao")
public interface TeamUserMapper extends BaseMapper<TeamUser> {

    List<TeamUser> queryTeamUserList(@Param("realname") String realname,
                                     @Param("tel") String tel,
                                     @Param("userId")Integer userId);


    TeamUser queryTeamUserByTeamIdAndTel(@Param("teamId") int teamId,
                                         @Param("tel")  String tel);


    TeamUser queryByUserNameAndPwd(@Param("username") String username,
                                   @Param("password") String password);
}
