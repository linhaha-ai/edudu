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
import com.edudu.edu.domain.EduStoryInteraction;
import com.edudu.edu.service.IEduStoryInteractionService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 故事互动问题Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/interaction")
public class EduStoryInteractionController extends BaseController
{
    @Autowired
    private IEduStoryInteractionService eduStoryInteractionService;

    /**
     * 查询故事互动问题列表
     */
    @PreAuthorize("@ss.hasPermi('edu:interaction:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduStoryInteraction eduStoryInteraction)
    {
        startPage();
        List<EduStoryInteraction> list = eduStoryInteractionService.selectEduStoryInteractionList(eduStoryInteraction);
        return getDataTable(list);
    }

    /**
     * 导出故事互动问题列表
     */
    @PreAuthorize("@ss.hasPermi('edu:interaction:export')")
    @Log(title = "故事互动问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduStoryInteraction eduStoryInteraction)
    {
        List<EduStoryInteraction> list = eduStoryInteractionService.selectEduStoryInteractionList(eduStoryInteraction);
        ExcelUtil<EduStoryInteraction> util = new ExcelUtil<EduStoryInteraction>(EduStoryInteraction.class);
        util.exportExcel(response, list, "故事互动问题数据");
    }

    /**
     * 获取故事互动问题详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:interaction:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduStoryInteractionService.selectEduStoryInteractionById(id));
    }

    /**
     * 新增故事互动问题
     */
    @PreAuthorize("@ss.hasPermi('edu:interaction:add')")
    @Log(title = "故事互动问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduStoryInteraction eduStoryInteraction)
    {
        return toAjax(eduStoryInteractionService.insertEduStoryInteraction(eduStoryInteraction));
    }

    /**
     * 修改故事互动问题
     */
    @PreAuthorize("@ss.hasPermi('edu:interaction:edit')")
    @Log(title = "故事互动问题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduStoryInteraction eduStoryInteraction)
    {
        return toAjax(eduStoryInteractionService.updateEduStoryInteraction(eduStoryInteraction));
    }

    /**
     * 删除故事互动问题
     */
    @PreAuthorize("@ss.hasPermi('edu:interaction:remove')")
    @Log(title = "故事互动问题", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduStoryInteractionService.deleteEduStoryInteractionByIds(ids));
    }
}
