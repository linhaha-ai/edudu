package com.edudu.edu.service;

import java.util.List;
import com.edudu.common.core.domain.entity.EduUser;

/**
 * 前端用户Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduUserService 
{
    /**
     * 查询前端用户
     * 
     * @param userId 前端用户主键
     * @return 前端用户
     */
    public EduUser selectEduUserByUserId(Long userId);

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
     * 批量删除前端用户
     * 
     * @param userIds 需要删除的前端用户主键集合
     * @return 结果
     */
    public int deleteEduUserByUserIds(Long[] userIds);

    /**
     * 删除前端用户信息
     * 
     * @param userId 前端用户主键
     * @return 结果
     */
    public int deleteEduUserByUserId(Long userId);

    /**
     * 根据手机号查询前端用户
     * 
     * @param phone 手机号
     * @return 前端用户
     */
    public EduUser selectEduUserByPhone(String phone);

    /**
     * 检查手机号是否唯一
     * 
     * @param phone 手机号
     * @return true=唯一 false=不唯一
     */
    public boolean checkPhoneUnique(String phone);

    /**
     * 更新用户登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @param loginDate 登录时间
     * @return 结果
     */
    public int updateLoginInfo(Long userId, String loginIp, java.util.Date loginDate);
}
