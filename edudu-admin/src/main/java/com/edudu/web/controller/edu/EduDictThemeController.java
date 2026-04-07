package com.edudu.web.controller.edu;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.edudu.common.annotation.Log;
import com.edudu.common.core.controller.BaseController;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.enums.BusinessType;
import com.edudu.edu.domain.EduDictTheme;
import com.edudu.edu.service.IEduDictThemeService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 任务主题字典Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/theme")
public class EduDictThemeController extends BaseController
{
    @Autowired
    private IEduDictThemeService eduDictThemeService;

    /**
     * 查询任务主题字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:theme:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduDictTheme eduDictTheme)
    {
        startPage();
        List<EduDictTheme> list = eduDictThemeService.selectEduDictThemeList(eduDictTheme);
        return getDataTable(list);
    }

    /**
     * 导出任务主题字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:theme:export')")
    @Log(title = "任务主题字典", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduDictTheme eduDictTheme)
    {
        List<EduDictTheme> list = eduDictThemeService.selectEduDictThemeList(eduDictTheme);
        ExcelUtil<EduDictTheme> util = new ExcelUtil<EduDictTheme>(EduDictTheme.class);
        util.exportExcel(response, list, "任务主题字典数据");
    }

    /**
     * 获取任务主题字典详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:theme:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduDictThemeService.selectEduDictThemeById(id));
    }

    /**
     * 新增任务主题字典
     */
    @PreAuthorize("@ss.hasPermi('edu:theme:add')")
    @Log(title = "任务主题字典", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduDictTheme eduDictTheme)
    {
        return toAjax(eduDictThemeService.insertEduDictTheme(eduDictTheme));
    }

    /**
     * 修改任务主题字典
     */
    @PreAuthorize("@ss.hasPermi('edu:theme:edit')")
    @Log(title = "任务主题字典", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduDictTheme eduDictTheme)
    {
        return toAjax(eduDictThemeService.updateEduDictTheme(eduDictTheme));
    }

    /**
     * 删除任务主题字典
     */
    @PreAuthorize("@ss.hasPermi('edu:theme:remove')")
    @Log(title = "任务主题字典", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduDictThemeService.deleteEduDictThemeByIds(ids));
    }
}
