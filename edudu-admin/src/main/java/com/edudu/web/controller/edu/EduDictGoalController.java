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
import com.edudu.edu.domain.EduDictGoal;
import com.edudu.edu.service.IEduDictGoalService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 教育目标字典Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/goal")
public class EduDictGoalController extends BaseController
{
    @Autowired
    private IEduDictGoalService eduDictGoalService;

    /**
     * 查询教育目标字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:goal:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduDictGoal eduDictGoal)
    {
        startPage();
        List<EduDictGoal> list = eduDictGoalService.selectEduDictGoalList(eduDictGoal);
        return getDataTable(list);
    }

    /**
     * 导出教育目标字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:goal:export')")
    @Log(title = "教育目标字典", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduDictGoal eduDictGoal)
    {
        List<EduDictGoal> list = eduDictGoalService.selectEduDictGoalList(eduDictGoal);
        ExcelUtil<EduDictGoal> util = new ExcelUtil<EduDictGoal>(EduDictGoal.class);
        util.exportExcel(response, list, "教育目标字典数据");
    }

    /**
     * 获取教育目标字典详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:goal:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduDictGoalService.selectEduDictGoalById(id));
    }

    /**
     * 新增教育目标字典
     */
    @PreAuthorize("@ss.hasPermi('edu:goal:add')")
    @Log(title = "教育目标字典", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduDictGoal eduDictGoal)
    {
        return toAjax(eduDictGoalService.insertEduDictGoal(eduDictGoal));
    }

    /**
     * 修改教育目标字典
     */
    @PreAuthorize("@ss.hasPermi('edu:goal:edit')")
    @Log(title = "教育目标字典", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduDictGoal eduDictGoal)
    {
        return toAjax(eduDictGoalService.updateEduDictGoal(eduDictGoal));
    }

    /**
     * 删除教育目标字典
     */
    @PreAuthorize("@ss.hasPermi('edu:goal:remove')")
    @Log(title = "教育目标字典", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduDictGoalService.deleteEduDictGoalByIds(ids));
    }
}
