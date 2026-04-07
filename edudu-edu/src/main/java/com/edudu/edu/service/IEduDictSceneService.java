package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduDictScene;

/**
 * 故事场景字典Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduDictSceneService 
{
    /**
     * 查询故事场景字典
     * 
     * @param id 故事场景字典主键
     * @return 故事场景字典
     */
    public EduDictScene selectEduDictSceneById(Long id);

    /**
     * 查询故事场景字典列表
     * 
     * @param eduDictScene 故事场景字典
     * @return 故事场景字典集合
     */
    public List<EduDictScene> selectEduDictSceneList(EduDictScene eduDictScene);

    /**
     * 新增故事场景字典
     * 
     * @param eduDictScene 故事场景字典
     * @return 结果
     */
    public int insertEduDictScene(EduDictScene eduDictScene);

    /**
     * 修改故事场景字典
     * 
     * @param eduDictScene 故事场景字典
     * @return 结果
     */
    public int updateEduDictScene(EduDictScene eduDictScene);

    /**
     * 批量删除故事场景字典
     * 
     * @param ids 需要删除的故事场景字典主键集合
     * @return 结果
     */
    public int deleteEduDictSceneByIds(Long[] ids);

    /**
     * 删除故事场景字典信息
     * 
     * @param id 故事场景字典主键
     * @return 结果
     */
    public int deleteEduDictSceneById(Long id);
}
