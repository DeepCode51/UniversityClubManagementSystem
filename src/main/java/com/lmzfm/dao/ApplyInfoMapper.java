package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.ApplyInfo;
import com.lmzfm.model.ApplyList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 社团入团申请记录,申请生成可入团 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("applyDao")
public interface ApplyInfoMapper extends BaseMapper<ApplyInfo> {

    List<ApplyInfo> queryAppList(ApplyInfo info);

    List<ApplyList> queryAppListByAppId(@Param("appId") Integer appId);

    boolean updateStatus(ApplyInfo info);

    int insertAppList(ApplyList applyList);

    ApplyInfo queryApplyInfoByTeamIdAndTel(@Param("teamId") int teamId,
                                           @Param("tel") String tel);
}
