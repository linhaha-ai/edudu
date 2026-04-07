package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduUser;

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
}
