package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduChildMapper;
import com.edudu.edu.domain.EduChild;
import com.edudu.edu.service.IEduChildService;

/**
 * 孩子信息Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduChildServiceImpl implements IEduChildService 
{
    @Autowired
    private EduChildMapper eduChildMapper;

    /**
     * 查询孩子信息
     * 
     * @param childId 孩子信息主键
     * @return 孩子信息
     */
    @Override
    public EduChild selectEduChildByChildId(Long childId)
    {
        return eduChildMapper.selectEduChildByChildId(childId);
    }

    /**
     * 查询孩子信息列表
     * 
     * @param eduChild 孩子信息
     * @return 孩子信息
     */
    @Override
    public List<EduChild> selectEduChildList(EduChild eduChild)
    {
        return eduChildMapper.selectEduChildList(eduChild);
    }

    /**
     * 新增孩子信息
     * 
     * @param eduChild 孩子信息
     * @return 结果
     */
    @Override
    public int insertEduChild(EduChild eduChild)
    {
        eduChild.setCreateTime(DateUtils.getNowDate());
        return eduChildMapper.insertEduChild(eduChild);
    }

    /**
     * 修改孩子信息
     * 
     * @param eduChild 孩子信息
     * @return 结果
     */
    @Override
    public int updateEduChild(EduChild eduChild)
    {
        eduChild.setUpdateTime(DateUtils.getNowDate());
        return eduChildMapper.updateEduChild(eduChild);
    }

    /**
     * 批量删除孩子信息
     * 
     * @param childIds 需要删除的孩子信息主键
     * @return 结果
     */
    @Override
    public int deleteEduChildByChildIds(Long[] childIds)
    {
        return eduChildMapper.deleteEduChildByChildIds(childIds);
    }

    /**
     * 删除孩子信息信息
     * 
     * @param childId 孩子信息主键
     * @return 结果
     */
    @Override
    public int deleteEduChildByChildId(Long childId)
    {
        return eduChildMapper.deleteEduChildByChildId(childId);
    }
}
