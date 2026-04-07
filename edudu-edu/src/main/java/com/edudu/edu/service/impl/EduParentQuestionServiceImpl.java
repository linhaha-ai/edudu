package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduParentQuestionMapper;
import com.edudu.edu.domain.EduParentQuestion;
import com.edudu.edu.service.IEduParentQuestionService;

/**
 * 家长问题Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduParentQuestionServiceImpl implements IEduParentQuestionService 
{
    @Autowired
    private EduParentQuestionMapper eduParentQuestionMapper;

    /**
     * 查询家长问题
     * 
     * @param id 家长问题主键
     * @return 家长问题
     */
    @Override
    public EduParentQuestion selectEduParentQuestionById(Long id)
    {
        return eduParentQuestionMapper.selectEduParentQuestionById(id);
    }

    /**
     * 查询家长问题列表
     * 
     * @param eduParentQuestion 家长问题
     * @return 家长问题
     */
    @Override
    public List<EduParentQuestion> selectEduParentQuestionList(EduParentQuestion eduParentQuestion)
    {
        return eduParentQuestionMapper.selectEduParentQuestionList(eduParentQuestion);
    }

    /**
     * 新增家长问题
     * 
     * @param eduParentQuestion 家长问题
     * @return 结果
     */
    @Override
    public int insertEduParentQuestion(EduParentQuestion eduParentQuestion)
    {
        eduParentQuestion.setCreateTime(DateUtils.getNowDate());
        return eduParentQuestionMapper.insertEduParentQuestion(eduParentQuestion);
    }

    /**
     * 修改家长问题
     * 
     * @param eduParentQuestion 家长问题
     * @return 结果
     */
    @Override
    public int updateEduParentQuestion(EduParentQuestion eduParentQuestion)
    {
        eduParentQuestion.setUpdateTime(DateUtils.getNowDate());
        return eduParentQuestionMapper.updateEduParentQuestion(eduParentQuestion);
    }

    /**
     * 批量删除家长问题
     * 
     * @param ids 需要删除的家长问题主键
     * @return 结果
     */
    @Override
    public int deleteEduParentQuestionByIds(Long[] ids)
    {
        return eduParentQuestionMapper.deleteEduParentQuestionByIds(ids);
    }

    /**
     * 删除家长问题信息
     * 
     * @param id 家长问题主键
     * @return 结果
     */
    @Override
    public int deleteEduParentQuestionById(Long id)
    {
        return eduParentQuestionMapper.deleteEduParentQuestionById(id);
    }
}
