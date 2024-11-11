package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 管理员信息表 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("userInfoDao")
public interface UserinfoMapper extends BaseMapper<UserInfo> {
    /**
     * 根据对象属性值查询对象是否存在
     * 用户名 密码 角色
     */
    UserInfo queryUserInfoByUserNameAndPasswordAndType(UserInfo userInfo);

    List<UserInfo> queryUserInfoList(UserInfo userInfo);

    /**
     * 修改密码
     */
    int updatePasswordByIdAndNewPassword(@Param("id") Integer id,
                                         @Param("pwd") String  pwd);

    /**
     * 添加社团团长列表
     * 进能够作为一个社团团长  总管理员不可作为社长
     */
    List<UserInfo> queryUserLists();
}
