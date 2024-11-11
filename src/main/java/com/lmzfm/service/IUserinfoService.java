package com.lmzfm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lmzfm.model.UserInfo;

import java.util.List;

/**
 * <p>
 * 管理员信息表 服务类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
public interface IUserinfoService extends IService<UserInfo> {

    /**
     * 查询管理员信息表分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<Userinfo>
     */
    IPage<UserInfo> findListByPage(Integer page, Integer pageCount);

    /**
     * 添加管理员信息表
     *
     * @param userinfo 管理员信息表
     * @return int
     */
    int add(UserInfo userinfo);

    /**
     * 删除管理员信息表
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改管理员信息表
     *
     * @param userinfo 管理员信息表
     * @return int
     */
    int updateData(UserInfo userinfo);

    /**
     * id查询数据
     *
     * @param id id
     * @return Userinfo
     */
    UserInfo findById(Long id);

    /**
     * 根据对象属性值查询对象是否存在
     * 用户名 密码 角色
     */
    UserInfo queryUserInfoByUserNameAndPasswordAndType(UserInfo userInfo);

    PageInfo<UserInfo> queryList(int page, int limit,
                                 UserInfo info);

    /**
     * 修改密码
     */
    int updatePassWord(Integer id,String newPassWord);

    /**
     * 添加社团团长列表
     * 进能够作为一个社团团长  总管理员不可作为社长
     */
    List<UserInfo> queryUserLists();
}
