package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.TypeInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 社团分类和介绍 Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("typeDao")
public interface TypeInfoMapper extends BaseMapper<TypeInfo> {
    List<TypeInfo> queryTypeList(TypeInfo info);
}
