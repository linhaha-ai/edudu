package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduDictSceneMapper;
import com.edudu.edu.domain.EduDictScene;
import com.edudu.edu.service.IEduDictSceneService;

/**
 * 故事场景字典Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduDictSceneServiceImpl implements IEduDictSceneService 
{
    @Autowired
    private EduDictSceneMapper eduDictSceneMapper;

    /**
     * 查询故事场景字典
     * 
     * @param id 故事场景字典主键
     * @return 故事场景字典
     */
    @Override
    public EduDictScene selectEduDictSceneById(Long id)
    {
        return eduDictSceneMapper.selectEduDictSceneById(id);
    }

    /**
     * 查询故事场景字典列表
     * 
     * @param eduDictScene 故事场景字典
     * @return 故事场景字典
     */
    @Override
    public List<EduDictScene> selectEduDictSceneList(EduDictScene eduDictScene)
    {
        return eduDictSceneMapper.selectEduDictSceneList(eduDictScene);
    }

    /**
     * 新增故事场景字典
     * 
     * @param eduDictScene 故事场景字典
     * @return 结果
     */
    @Override
    public int insertEduDictScene(EduDictScene eduDictScene)
    {
        eduDictScene.setCreateTime(DateUtils.getNowDate());
        return eduDictSceneMapper.insertEduDictScene(eduDictScene);
    }

    /**
     * 修改故事场景字典
     * 
     * @param eduDictScene 故事场景字典
     * @return 结果
     */
    @Override
    public int updateEduDictScene(EduDictScene eduDictScene)
    {
        eduDictScene.setUpdateTime(DateUtils.getNowDate());
        return eduDictSceneMapper.updateEduDictScene(eduDictScene);
    }

    /**
     * 批量删除故事场景字典
     * 
     * @param ids 需要删除的故事场景字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictSceneByIds(Long[] ids)
    {
        return eduDictSceneMapper.deleteEduDictSceneByIds(ids);
    }

    /**
     * 删除故事场景字典信息
     * 
     * @param id 故事场景字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictSceneById(Long id)
    {
        return eduDictSceneMapper.deleteEduDictSceneById(id);
    }
}
