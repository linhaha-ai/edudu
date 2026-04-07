package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduSubscriptionMapper;
import com.edudu.edu.domain.EduSubscription;
import com.edudu.edu.service.IEduSubscriptionService;

/**
 * 订阅Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduSubscriptionServiceImpl implements IEduSubscriptionService 
{
    @Autowired
    private EduSubscriptionMapper eduSubscriptionMapper;

    /**
     * 查询订阅
     * 
     * @param id 订阅主键
     * @return 订阅
     */
    @Override
    public EduSubscription selectEduSubscriptionById(Long id)
    {
        return eduSubscriptionMapper.selectEduSubscriptionById(id);
    }

    /**
     * 查询订阅列表
     * 
     * @param eduSubscription 订阅
     * @return 订阅
     */
    @Override
    public List<EduSubscription> selectEduSubscriptionList(EduSubscription eduSubscription)
    {
        return eduSubscriptionMapper.selectEduSubscriptionList(eduSubscription);
    }

    /**
     * 新增订阅
     * 
     * @param eduSubscription 订阅
     * @return 结果
     */
    @Override
    public int insertEduSubscription(EduSubscription eduSubscription)
    {
        eduSubscription.setCreateTime(DateUtils.getNowDate());
        return eduSubscriptionMapper.insertEduSubscription(eduSubscription);
    }

    /**
     * 修改订阅
     * 
     * @param eduSubscription 订阅
     * @return 结果
     */
    @Override
    public int updateEduSubscription(EduSubscription eduSubscription)
    {
        eduSubscription.setUpdateTime(DateUtils.getNowDate());
        return eduSubscriptionMapper.updateEduSubscription(eduSubscription);
    }

    /**
     * 批量删除订阅
     * 
     * @param ids 需要删除的订阅主键
     * @return 结果
     */
    @Override
    public int deleteEduSubscriptionByIds(Long[] ids)
    {
        return eduSubscriptionMapper.deleteEduSubscriptionByIds(ids);
    }

    /**
     * 删除订阅信息
     * 
     * @param id 订阅主键
     * @return 结果
     */
    @Override
    public int deleteEduSubscriptionById(Long id)
    {
        return eduSubscriptionMapper.deleteEduSubscriptionById(id);
    }
}
