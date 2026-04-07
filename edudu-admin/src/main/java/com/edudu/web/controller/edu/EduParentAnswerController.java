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
import com.edudu.edu.domain.EduParentAnswer;
import com.edudu.edu.service.IEduParentAnswerService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * AI回答Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/answer")
public class EduParentAnswerController extends BaseController
{
    @Autowired
    private IEduParentAnswerService eduParentAnswerService;

    /**
     * 查询AI回答列表
     */
    @PreAuthorize("@ss.hasPermi('edu:answer:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduParentAnswer eduParentAnswer)
    {
        startPage();
        List<EduParentAnswer> list = eduParentAnswerService.selectEduParentAnswerList(eduParentAnswer);
        return getDataTable(list);
    }

    /**
     * 导出AI回答列表
     */
    @PreAuthorize("@ss.hasPermi('edu:answer:export')")
    @Log(title = "AI回答", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduParentAnswer eduParentAnswer)
    {
        List<EduParentAnswer> list = eduParentAnswerService.selectEduParentAnswerList(eduParentAnswer);
        ExcelUtil<EduParentAnswer> util = new ExcelUtil<EduParentAnswer>(EduParentAnswer.class);
        util.exportExcel(response, list, "AI回答数据");
    }

    /**
     * 获取AI回答详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:answer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduParentAnswerService.selectEduParentAnswerById(id));
    }

    /**
     * 新增AI回答
     */
    @PreAuthorize("@ss.hasPermi('edu:answer:add')")
    @Log(title = "AI回答", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduParentAnswer eduParentAnswer)
    {
        return toAjax(eduParentAnswerService.insertEduParentAnswer(eduParentAnswer));
    }

    /**
     * 修改AI回答
     */
    @PreAuthorize("@ss.hasPermi('edu:answer:edit')")
    @Log(title = "AI回答", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduParentAnswer eduParentAnswer)
    {
        return toAjax(eduParentAnswerService.updateEduParentAnswer(eduParentAnswer));
    }

    /**
     * 删除AI回答
     */
    @PreAuthorize("@ss.hasPermi('edu:answer:remove')")
    @Log(title = "AI回答", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduParentAnswerService.deleteEduParentAnswerByIds(ids));
    }
}
