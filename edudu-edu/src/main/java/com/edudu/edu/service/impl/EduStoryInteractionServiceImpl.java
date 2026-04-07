package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduStoryInteractionMapper;
import com.edudu.edu.domain.EduStoryInteraction;
import com.edudu.edu.service.IEduStoryInteractionService;

/**
 * 故事互动问题Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduStoryInteractionServiceImpl implements IEduStoryInteractionService 
{
    @Autowired
    private EduStoryInteractionMapper eduStoryInteractionMapper;

    /**
     * 查询故事互动问题
     * 
     * @param id 故事互动问题主键
     * @return 故事互动问题
     */
    @Override
    public EduStoryInteraction selectEduStoryInteractionById(Long id)
    {
        return eduStoryInteractionMapper.selectEduStoryInteractionById(id);
    }

    /**
     * 查询故事互动问题列表
     * 
     * @param eduStoryInteraction 故事互动问题
     * @return 故事互动问题
     */
    @Override
    public List<EduStoryInteraction> selectEduStoryInteractionList(EduStoryInteraction eduStoryInteraction)
    {
        return eduStoryInteractionMapper.selectEduStoryInteractionList(eduStoryInteraction);
    }

    /**
     * 新增故事互动问题
     * 
     * @param eduStoryInteraction 故事互动问题
     * @return 结果
     */
    @Override
    public int insertEduStoryInteraction(EduStoryInteraction eduStoryInteraction)
    {
        eduStoryInteraction.setCreateTime(DateUtils.getNowDate());
        return eduStoryInteractionMapper.insertEduStoryInteraction(eduStoryInteraction);
    }

    /**
     * 修改故事互动问题
     * 
     * @param eduStoryInteraction 故事互动问题
     * @return 结果
     */
    @Override
    public int updateEduStoryInteraction(EduStoryInteraction eduStoryInteraction)
    {
        return eduStoryInteractionMapper.updateEduStoryInteraction(eduStoryInteraction);
    }

    /**
     * 批量删除故事互动问题
     * 
     * @param ids 需要删除的故事互动问题主键
     * @return 结果
     */
    @Override
    public int deleteEduStoryInteractionByIds(Long[] ids)
    {
        return eduStoryInteractionMapper.deleteEduStoryInteractionByIds(ids);
    }

    /**
     * 删除故事互动问题信息
     * 
     * @param id 故事互动问题主键
     * @return 结果
     */
    @Override
    public int deleteEduStoryInteractionById(Long id)
    {
        return eduStoryInteractionMapper.deleteEduStoryInteractionById(id);
    }
}
