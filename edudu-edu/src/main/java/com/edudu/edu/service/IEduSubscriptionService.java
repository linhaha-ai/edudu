package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduSubscription;

/**
 * 订阅Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduSubscriptionService 
{
    /**
     * 查询订阅
     * 
     * @param id 订阅主键
     * @return 订阅
     */
    public EduSubscription selectEduSubscriptionById(Long id);

    /**
     * 查询订阅列表
     * 
     * @param eduSubscription 订阅
     * @return 订阅集合
     */
    public List<EduSubscription> selectEduSubscriptionList(EduSubscription eduSubscription);

    /**
     * 新增订阅
     * 
     * @param eduSubscription 订阅
     * @return 结果
     */
    public int insertEduSubscription(EduSubscription eduSubscription);

    /**
     * 修改订阅
     * 
     * @param eduSubscription 订阅
     * @return 结果
     */
    public int updateEduSubscription(EduSubscription eduSubscription);

    /**
     * 批量删除订阅
     * 
     * @param ids 需要删除的订阅主键集合
     * @return 结果
     */
    public int deleteEduSubscriptionByIds(Long[] ids);

    /**
     * 删除订阅信息
     * 
     * @param id 订阅主键
     * @return 结果
     */
    public int deleteEduSubscriptionById(Long id);
}
