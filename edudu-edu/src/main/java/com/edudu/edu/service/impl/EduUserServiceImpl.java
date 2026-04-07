package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduUserMapper;
import com.edudu.edu.domain.EduUser;
import com.edudu.edu.service.IEduUserService;

/**
 * 前端用户Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduUserServiceImpl implements IEduUserService 
{
    @Autowired
    private EduUserMapper eduUserMapper;

    /**
     * 查询前端用户
     * 
     * @param userId 前端用户主键
     * @return 前端用户
     */
    @Override
    public EduUser selectEduUserByUserId(Long userId)
    {
        return eduUserMapper.selectEduUserByUserId(userId);
    }

    /**
     * 查询前端用户列表
     * 
     * @param eduUser 前端用户
     * @return 前端用户
     */
    @Override
    public List<EduUser> selectEduUserList(EduUser eduUser)
    {
        return eduUserMapper.selectEduUserList(eduUser);
    }

    /**
     * 新增前端用户
     * 
     * @param eduUser 前端用户
     * @return 结果
     */
    @Override
    public int insertEduUser(EduUser eduUser)
    {
        eduUser.setCreateTime(DateUtils.getNowDate());
        return eduUserMapper.insertEduUser(eduUser);
    }

    /**
     * 修改前端用户
     * 
     * @param eduUser 前端用户
     * @return 结果
     */
    @Override
    public int updateEduUser(EduUser eduUser)
    {
        eduUser.setUpdateTime(DateUtils.getNowDate());
        return eduUserMapper.updateEduUser(eduUser);
    }

    /**
     * 批量删除前端用户
     * 
     * @param userIds 需要删除的前端用户主键
     * @return 结果
     */
    @Override
    public int deleteEduUserByUserIds(Long[] userIds)
    {
        return eduUserMapper.deleteEduUserByUserIds(userIds);
    }

    /**
     * 删除前端用户信息
     * 
     * @param userId 前端用户主键
     * @return 结果
     */
    @Override
    public int deleteEduUserByUserId(Long userId)
    {
        return eduUserMapper.deleteEduUserByUserId(userId);
    }
}
