package com.lmzfm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmzfm.dao.NoticeMapper;
import com.lmzfm.model.Notice;
import com.lmzfm.service.INoticeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公告信息 服务实现类
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public  IPage<Notice> findListByPage(Integer page, Integer pageCount){
        IPage<Notice> wherePage = new Page<>(page, pageCount);
        Notice where = new Notice();

        return   baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(Notice notice){
        return baseMapper.insert(notice);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(Notice notice){
        return baseMapper.updateById(notice);
    }

    @Override
    public Notice findById(Long id){
        return  baseMapper.selectById(id);
    }
}
