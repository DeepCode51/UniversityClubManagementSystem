package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.LeaveInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 在线留言管理 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("leaveDao")
public interface LeaveInfoMapper extends BaseMapper<LeaveInfo> {

    List<LeaveInfo> queryLeaveInfoAll(@Param("tel") String tel);
}
