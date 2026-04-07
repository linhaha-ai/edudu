package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduDictGrowthType;

/**
 * 成长记录类型字典Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduDictGrowthTypeMapper 
{
    /**
     * 查询成长记录类型字典
     * 
     * @param id 成长记录类型字典主键
     * @return 成长记录类型字典
     */
    public EduDictGrowthType selectEduDictGrowthTypeById(Long id);

    /**
     * 查询成长记录类型字典列表
     * 
     * @param eduDictGrowthType 成长记录类型字典
     * @return 成长记录类型字典集合
     */
    public List<EduDictGrowthType> selectEduDictGrowthTypeList(EduDictGrowthType eduDictGrowthType);

    /**
     * 新增成长记录类型字典
     * 
     * @param eduDictGrowthType 成长记录类型字典
     * @return 结果
     */
    public int insertEduDictGrowthType(EduDictGrowthType eduDictGrowthType);

    /**
     * 修改成长记录类型字典
     * 
     * @param eduDictGrowthType 成长记录类型字典
     * @return 结果
     */
    public int updateEduDictGrowthType(EduDictGrowthType eduDictGrowthType);

    /**
     * 删除成长记录类型字典
     * 
     * @param id 成长记录类型字典主键
     * @return 结果
     */
    public int deleteEduDictGrowthTypeById(Long id);

    /**
     * 批量删除成长记录类型字典
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduDictGrowthTypeByIds(Long[] ids);
}
