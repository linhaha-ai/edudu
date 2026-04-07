package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduStoryRecord;

/**
 * 故事记录Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduStoryRecordMapper 
{
    /**
     * 查询故事记录
     * 
     * @param id 故事记录主键
     * @return 故事记录
     */
    public EduStoryRecord selectEduStoryRecordById(Long id);

    /**
     * 查询故事记录列表
     * 
     * @param eduStoryRecord 故事记录
     * @return 故事记录集合
     */
    public List<EduStoryRecord> selectEduStoryRecordList(EduStoryRecord eduStoryRecord);

    /**
     * 新增故事记录
     * 
     * @param eduStoryRecord 故事记录
     * @return 结果
     */
    public int insertEduStoryRecord(EduStoryRecord eduStoryRecord);

    /**
     * 修改故事记录
     * 
     * @param eduStoryRecord 故事记录
     * @return 结果
     */
    public int updateEduStoryRecord(EduStoryRecord eduStoryRecord);

    /**
     * 删除故事记录
     * 
     * @param id 故事记录主键
     * @return 结果
     */
    public int deleteEduStoryRecordById(Long id);

    /**
     * 批量删除故事记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduStoryRecordByIds(Long[] ids);
}
