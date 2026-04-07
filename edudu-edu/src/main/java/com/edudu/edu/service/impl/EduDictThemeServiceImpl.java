package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduDictThemeMapper;
import com.edudu.edu.domain.EduDictTheme;
import com.edudu.edu.service.IEduDictThemeService;

/**
 * 任务主题字典Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduDictThemeServiceImpl implements IEduDictThemeService 
{
    @Autowired
    private EduDictThemeMapper eduDictThemeMapper;

    /**
     * 查询任务主题字典
     * 
     * @param id 任务主题字典主键
     * @return 任务主题字典
     */
    @Override
    public EduDictTheme selectEduDictThemeById(Long id)
    {
        return eduDictThemeMapper.selectEduDictThemeById(id);
    }

    /**
     * 查询任务主题字典列表
     * 
     * @param eduDictTheme 任务主题字典
     * @return 任务主题字典
     */
    @Override
    public List<EduDictTheme> selectEduDictThemeList(EduDictTheme eduDictTheme)
    {
        return eduDictThemeMapper.selectEduDictThemeList(eduDictTheme);
    }

    /**
     * 新增任务主题字典
     * 
     * @param eduDictTheme 任务主题字典
     * @return 结果
     */
    @Override
    public int insertEduDictTheme(EduDictTheme eduDictTheme)
    {
        eduDictTheme.setCreateTime(DateUtils.getNowDate());
        return eduDictThemeMapper.insertEduDictTheme(eduDictTheme);
    }

    /**
     * 修改任务主题字典
     * 
     * @param eduDictTheme 任务主题字典
     * @return 结果
     */
    @Override
    public int updateEduDictTheme(EduDictTheme eduDictTheme)
    {
        eduDictTheme.setUpdateTime(DateUtils.getNowDate());
        return eduDictThemeMapper.updateEduDictTheme(eduDictTheme);
    }

    /**
     * 批量删除任务主题字典
     * 
     * @param ids 需要删除的任务主题字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictThemeByIds(Long[] ids)
    {
        return eduDictThemeMapper.deleteEduDictThemeByIds(ids);
    }

    /**
     * 删除任务主题字典信息
     * 
     * @param id 任务主题字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictThemeById(Long id)
    {
        return eduDictThemeMapper.deleteEduDictThemeById(id);
    }
}
