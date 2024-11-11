package com.lmzfm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmzfm.model.CostList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lmz
 * @since 2023-04-03
 */
@Component("costListDao")
public interface CostListMapper extends BaseMapper<CostList> {

    List<CostList> queryCostListAll(@Param("name") String name,
                                    @Param("teamId") Integer teamId);
}
