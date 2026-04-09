package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduParentAnswerMapper;
import com.edudu.edu.domain.EduParentAnswer;
import com.edudu.edu.service.IEduParentAnswerService;

/**
 * AI回答Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduParentAnswerServiceImpl implements IEduParentAnswerService 
{
    @Autowired
    private EduParentAnswerMapper eduParentAnswerMapper;

    /**
     * 查询AI回答
     * 
     * @param id AI回答主键
     * @return AI回答
     */
    @Override
    public EduParentAnswer selectEduParentAnswerById(Long id)
    {
        return eduParentAnswerMapper.selectEduParentAnswerById(id);
    }

    /**
     * 查询AI回答列表
     * 
     * @param eduParentAnswer AI回答
     * @return AI回答
     */
    @Override
    public List<EduParentAnswer> selectEduParentAnswerList(EduParentAnswer eduParentAnswer)
    {
        return eduParentAnswerMapper.selectEduParentAnswerList(eduParentAnswer);
    }

    /**
     * 新增AI回答
     * 
     * @param eduParentAnswer AI回答
     * @return 结果
     */
    @Override
    public int insertEduParentAnswer(EduParentAnswer eduParentAnswer)
    {
        eduParentAnswer.setCreateTime(DateUtils.getNowDate());
        return eduParentAnswerMapper.insertEduParentAnswer(eduParentAnswer);
    }

    /**
     * 修改AI回答
     * 
     * @param eduParentAnswer AI回答
     * @return 结果
     */
    @Override
    public int updateEduParentAnswer(EduParentAnswer eduParentAnswer)
    {
        return eduParentAnswerMapper.updateEduParentAnswer(eduParentAnswer);
    }

    /**
     * 批量删除AI回答
     * 
     * @param ids 需要删除的AI回答主键
     * @return 结果
     */
    @Override
    public int deleteEduParentAnswerByIds(Long[] ids)
    {
        return eduParentAnswerMapper.deleteEduParentAnswerByIds(ids);
    }

    /**
     * 删除AI回答信息
     * 
     * @param id AI回答主键
     * @return 结果
     */
    @Override
    public int deleteEduParentAnswerById(Long id) {
        return eduParentAnswerMapper.deleteEduParentAnswerById(id);
    }

    /**
     * 根据问题ID查询AI回答
     */
    @Override
    public EduParentAnswer selectEduParentAnswerByQuestionId(Long questionId) {
        return eduParentAnswerMapper.selectEduParentAnswerByQuestionId(questionId);
    }
}
