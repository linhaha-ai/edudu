package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduStoryInteraction;

/**
 * 故事互动问题Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduStoryInteractionService 
{
    /**
     * 查询故事互动问题
     * 
     * @param id 故事互动问题主键
     * @return 故事互动问题
     */
    public EduStoryInteraction selectEduStoryInteractionById(Long id);

    /**
     * 查询故事互动问题列表
     * 
     * @param eduStoryInteraction 故事互动问题
     * @return 故事互动问题集合
     */
    public List<EduStoryInteraction> selectEduStoryInteractionList(EduStoryInteraction eduStoryInteraction);

    /**
     * 新增故事互动问题
     * 
     * @param eduStoryInteraction 故事互动问题
     * @return 结果
     */
    public int insertEduStoryInteraction(EduStoryInteraction eduStoryInteraction);

    /**
     * 修改故事互动问题
     * 
     * @param eduStoryInteraction 故事互动问题
     * @return 结果
     */
    public int updateEduStoryInteraction(EduStoryInteraction eduStoryInteraction);

    /**
     * 批量删除故事互动问题
     * 
     * @param ids 需要删除的故事互动问题主键集合
     * @return 结果
     */
    public int deleteEduStoryInteractionByIds(Long[] ids);

    /**
     * 删除故事互动问题信息
     * 
     * @param id 故事互动问题主键
     * @return 结果
     */
    public int deleteEduStoryInteractionById(Long id);
}
