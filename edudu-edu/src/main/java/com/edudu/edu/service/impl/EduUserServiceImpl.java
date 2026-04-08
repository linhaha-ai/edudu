package com.edudu.edu.service.impl;

import java.util.Date;
import java.util.List;
import com.edudu.common.utils.DateUtils;
import com.edudu.common.utils.StringUtils;
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

    /**
     * 根据手机号查询前端用户
     * 
     * @param phone 手机号
     * @return 前端用户
     */
    @Override
    public EduUser selectEduUserByPhone(String phone)
    {
        return eduUserMapper.selectEduUserByPhone(phone);
    }

    /**
     * 检查手机号是否唯一
     * 
     * @param phone 手机号
     * @return true=唯一 false=不唯一
     */
    @Override
    public boolean checkPhoneUnique(String phone)
    {
        if (StringUtils.isEmpty(phone))
        {
            return false;
        }
        EduUser user = eduUserMapper.selectEduUserByPhone(phone);
        return StringUtils.isNull(user);
    }

    /**
     * 更新用户登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @param loginDate 登录时间
     * @return 结果
     */
    @Override
    public int updateLoginInfo(Long userId, String loginIp, Date loginDate)
    {
        EduUser user = new EduUser();
        user.setUserId(userId);
        user.setLoginIp(loginIp);
        user.setLoginDate(loginDate);
        return eduUserMapper.updateEduUser(user);
    }
}
