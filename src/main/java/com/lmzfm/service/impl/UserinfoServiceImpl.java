package com.lmzfm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmzfm.dao.UserinfoMapper;
import com.lmzfm.model.UserInfo;
import com.lmzfm.service.IUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, UserInfo> implements IUserinfoService {
    @Autowired
    private UserinfoMapper userInfoDao;
    @Override
    public  IPage<UserInfo> findListByPage(Integer page, Integer pageCount){
        IPage<UserInfo> wherePage = new Page<>(page, pageCount);
        UserInfo where = new UserInfo();

        return   baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(UserInfo userinfo){
        return baseMapper.insert(userinfo);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(UserInfo userinfo){
        return baseMapper.updateById(userinfo);
    }

    @Override
    public UserInfo findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public UserInfo queryUserInfoByUserNameAndPasswordAndType(UserInfo userInfo) {
        return userInfoDao.queryUserInfoByUserNameAndPasswordAndType(userInfo);
    }

    @Override
    public PageInfo<UserInfo> queryList(int page, int limit, UserInfo info) {
        PageHelper.startPage(page,limit);
        List<UserInfo> activities = userInfoDao.queryUserInfoList(info);
        PageInfo<UserInfo> pageInfo=new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public int updatePassWord(Integer id, String newPassWord) {
        return userInfoDao.updatePasswordByIdAndNewPassword(id,newPassWord);
    }

    /**
     * 添加社团团长列表
     * 进能够作为一个社团团长  总管理员不可作为社长
     */
    public List<UserInfo> queryUserLists(){
        return userInfoDao.queryUserLists();
    }
}
