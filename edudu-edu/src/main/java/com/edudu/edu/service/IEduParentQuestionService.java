package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduParentQuestion;

/**
 * 家长问题Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduParentQuestionService 
{
    /**
     * 查询家长问题
     * 
     * @param id 家长问题主键
     * @return 家长问题
     */
    public EduParentQuestion selectEduParentQuestionById(Long id);

    /**
     * 查询家长问题列表
     * 
     * @param eduParentQuestion 家长问题
     * @return 家长问题集合
     */
    public List<EduParentQuestion> selectEduParentQuestionList(EduParentQuestion eduParentQuestion);

    /**
     * 新增家长问题
     * 
     * @param eduParentQuestion 家长问题
     * @return 结果
     */
    public int insertEduParentQuestion(EduParentQuestion eduParentQuestion);

    /**
     * 修改家长问题
     * 
     * @param eduParentQuestion 家长问题
     * @return 结果
     */
    public int updateEduParentQuestion(EduParentQuestion eduParentQuestion);

    /**
     * 批量删除家长问题
     * 
     * @param ids 需要删除的家长问题主键集合
     * @return 结果
     */
    public int deleteEduParentQuestionByIds(Long[] ids);

    /**
     * 删除家长问题信息
     * 
     * @param id 家长问题主键
     * @return 结果
     */
    public int deleteEduParentQuestionById(Long id);
}
