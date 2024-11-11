package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.Activity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 社团活动管理 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("activityDao")
public interface ActivityMapper extends BaseMapper<Activity> {

    List<Activity> queryActivityInfoList(@Param("name") String name,
                                         @Param("tel") String tel,
                                         @Param("userId") Integer userId);

    int updateStatusById(@Param("status") Integer status,
                         @Param("id") Integer id);

    Activity findByTeamId(@Param("teamId") int teamId);
}
