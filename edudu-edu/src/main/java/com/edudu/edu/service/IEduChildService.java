package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduChild;

/**
 * 孩子信息Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduChildService 
{
    /**
     * 查询孩子信息
     * 
     * @param childId 孩子信息主键
     * @return 孩子信息
     */
    public EduChild selectEduChildByChildId(Long childId);

    /**
     * 查询孩子信息列表
     * 
     * @param eduChild 孩子信息
     * @return 孩子信息集合
     */
    public List<EduChild> selectEduChildList(EduChild eduChild);

    /**
     * 新增孩子信息
     * 
     * @param eduChild 孩子信息
     * @return 结果
     */
    public int insertEduChild(EduChild eduChild);

    /**
     * 修改孩子信息
     * 
     * @param eduChild 孩子信息
     * @return 结果
     */
    public int updateEduChild(EduChild eduChild);

    /**
     * 批量删除孩子信息
     * 
     * @param childIds 需要删除的孩子信息主键集合
     * @return 结果
     */
    public int deleteEduChildByChildIds(Long[] childIds);

    /**
     * 删除孩子信息信息
     * 
     * @param childId 孩子信息主键
     * @return 结果
     */
    public int deleteEduChildByChildId(Long childId);
}
