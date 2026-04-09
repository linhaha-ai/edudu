package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduParentAnswer;

/**
 * AI回答Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduParentAnswerMapper 
{
    /**
     * 查询AI回答
     * 
     * @param id AI回答主键
     * @return AI回答
     */
    public EduParentAnswer selectEduParentAnswerById(Long id);

    /**
     * 查询AI回答列表
     * 
     * @param eduParentAnswer AI回答
     * @return AI回答集合
     */
    public List<EduParentAnswer> selectEduParentAnswerList(EduParentAnswer eduParentAnswer);

    /**
     * 新增AI回答
     * 
     * @param eduParentAnswer AI回答
     * @return 结果
     */
    public int insertEduParentAnswer(EduParentAnswer eduParentAnswer);

    /**
     * 修改AI回答
     * 
     * @param eduParentAnswer AI回答
     * @return 结果
     */
    public int updateEduParentAnswer(EduParentAnswer eduParentAnswer);

    /**
     * 删除AI回答
     * 
     * @param id AI回答主键
     * @return 结果
     */
    public int deleteEduParentAnswerById(Long id);

    /**
     * 批量删除AI回答
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduParentAnswerByIds(Long[] ids);

    /**
     * 根据问题ID查询AI回答
     */
    public EduParentAnswer selectEduParentAnswerByQuestionId(Long questionId);
}
