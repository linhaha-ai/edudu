package com.edudu.edu.mapper;

import com.edudu.edu.domain.EduUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 前端用户Mapper接口
 */
public interface EduUserMapper extends BaseMapper<EduUser> {

    /**
     * 根据手机号查询用户
     */
    EduUser selectByPhone(@Param("phone") String phone);

    /**
     * 根据UnionID查询用户
     */
    EduUser selectByUnionId(@Param("unionId") String unionId);
}
