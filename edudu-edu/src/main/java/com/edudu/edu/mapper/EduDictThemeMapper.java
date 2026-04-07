package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduDictTheme;

/**
 * 任务主题字典Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduDictThemeMapper 
{
    /**
     * 查询任务主题字典
     * 
     * @param id 任务主题字典主键
     * @return 任务主题字典
     */
    public EduDictTheme selectEduDictThemeById(Long id);

    /**
     * 查询任务主题字典列表
     * 
     * @param eduDictTheme 任务主题字典
     * @return 任务主题字典集合
     */
    public List<EduDictTheme> selectEduDictThemeList(EduDictTheme eduDictTheme);

    /**
     * 新增任务主题字典
     * 
     * @param eduDictTheme 任务主题字典
     * @return 结果
     */
    public int insertEduDictTheme(EduDictTheme eduDictTheme);

    /**
     * 修改任务主题字典
     * 
     * @param eduDictTheme 任务主题字典
     * @return 结果
     */
    public int updateEduDictTheme(EduDictTheme eduDictTheme);

    /**
     * 删除任务主题字典
     * 
     * @param id 任务主题字典主键
     * @return 结果
     */
    public int deleteEduDictThemeById(Long id);

    /**
     * 批量删除任务主题字典
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduDictThemeByIds(Long[] ids);
}
