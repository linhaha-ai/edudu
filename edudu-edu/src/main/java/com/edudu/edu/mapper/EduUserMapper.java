package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.common.core.domain.entity.EduUser;

/**
 * 前端用户Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduUserMapper 
{
    /**
     * 查询前端用户
     * 
     * @param userId 前端用户主键
     * @return 前端用户
     */
    public EduUser selectEduUserByUserId(Long userId);

    /**
     * 根据手机号查询前端用户
     * 
     * @param phone 手机号
     * @return 前端用户
     */
    public EduUser selectEduUserByPhone(String phone);

    /**
     * 查询前端用户列表
     * 
     * @param eduUser 前端用户
     * @return 前端用户集合
     */
    public List<EduUser> selectEduUserList(EduUser eduUser);

    /**
     * 新增前端用户
     * 
     * @param eduUser 前端用户
     * @return 结果
     */
    public int insertEduUser(EduUser eduUser);

    /**
     * 修改前端用户
     * 
     * @param eduUser 前端用户
     * @return 结果
     */
    public int updateEduUser(EduUser eduUser);

    /**
     * 删除前端用户
     * 
     * @param userId 前端用户主键
     * @return 结果
     */
    public int deleteEduUserByUserId(Long userId);

    /**
     * 批量删除前端用户
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduUserByUserIds(Long[] userIds);

    /**
     * 根据OpenID查询前端用户
     * 
     * @param openid 微信OpenID
     * @return 前端用户
     */
    public EduUser selectEduUserByOpenid(String openid);
}
