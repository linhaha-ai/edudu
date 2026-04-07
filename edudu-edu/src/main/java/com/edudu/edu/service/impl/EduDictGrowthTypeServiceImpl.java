package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduDictGrowthTypeMapper;
import com.edudu.edu.domain.EduDictGrowthType;
import com.edudu.edu.service.IEduDictGrowthTypeService;

/**
 * 成长记录类型字典Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduDictGrowthTypeServiceImpl implements IEduDictGrowthTypeService 
{
    @Autowired
    private EduDictGrowthTypeMapper eduDictGrowthTypeMapper;

    /**
     * 查询成长记录类型字典
     * 
     * @param id 成长记录类型字典主键
     * @return 成长记录类型字典
     */
    @Override
    public EduDictGrowthType selectEduDictGrowthTypeById(Long id)
    {
        return eduDictGrowthTypeMapper.selectEduDictGrowthTypeById(id);
    }

    /**
     * 查询成长记录类型字典列表
     * 
     * @param eduDictGrowthType 成长记录类型字典
     * @return 成长记录类型字典
     */
    @Override
    public List<EduDictGrowthType> selectEduDictGrowthTypeList(EduDictGrowthType eduDictGrowthType)
    {
        return eduDictGrowthTypeMapper.selectEduDictGrowthTypeList(eduDictGrowthType);
    }

    /**
     * 新增成长记录类型字典
     * 
     * @param eduDictGrowthType 成长记录类型字典
     * @return 结果
     */
    @Override
    public int insertEduDictGrowthType(EduDictGrowthType eduDictGrowthType)
    {
        eduDictGrowthType.setCreateTime(DateUtils.getNowDate());
        return eduDictGrowthTypeMapper.insertEduDictGrowthType(eduDictGrowthType);
    }

    /**
     * 修改成长记录类型字典
     * 
     * @param eduDictGrowthType 成长记录类型字典
     * @return 结果
     */
    @Override
    public int updateEduDictGrowthType(EduDictGrowthType eduDictGrowthType)
    {
        eduDictGrowthType.setUpdateTime(DateUtils.getNowDate());
        return eduDictGrowthTypeMapper.updateEduDictGrowthType(eduDictGrowthType);
    }

    /**
     * 批量删除成长记录类型字典
     * 
     * @param ids 需要删除的成长记录类型字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictGrowthTypeByIds(Long[] ids)
    {
        return eduDictGrowthTypeMapper.deleteEduDictGrowthTypeByIds(ids);
    }

    /**
     * 删除成长记录类型字典信息
     * 
     * @param id 成长记录类型字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictGrowthTypeById(Long id)
    {
        return eduDictGrowthTypeMapper.deleteEduDictGrowthTypeById(id);
    }
}
