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
import com.edudu.edu.domain.EduParentQuestion;
import com.edudu.edu.service.IEduParentQuestionService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 家长问题Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/question")
public class EduParentQuestionController extends BaseController
{
    @Autowired
    private IEduParentQuestionService eduParentQuestionService;

    /**
     * 查询家长问题列表
     */
    @PreAuthorize("@ss.hasPermi('edu:question:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduParentQuestion eduParentQuestion)
    {
        startPage();
        List<EduParentQuestion> list = eduParentQuestionService.selectEduParentQuestionList(eduParentQuestion);
        return getDataTable(list);
    }

    /**
     * 导出家长问题列表
     */
    @PreAuthorize("@ss.hasPermi('edu:question:export')")
    @Log(title = "家长问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduParentQuestion eduParentQuestion)
    {
        List<EduParentQuestion> list = eduParentQuestionService.selectEduParentQuestionList(eduParentQuestion);
        ExcelUtil<EduParentQuestion> util = new ExcelUtil<EduParentQuestion>(EduParentQuestion.class);
        util.exportExcel(response, list, "家长问题数据");
    }

    /**
     * 获取家长问题详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:question:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduParentQuestionService.selectEduParentQuestionById(id));
    }

    /**
     * 新增家长问题
     */
    @PreAuthorize("@ss.hasPermi('edu:question:add')")
    @Log(title = "家长问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduParentQuestion eduParentQuestion)
    {
        return toAjax(eduParentQuestionService.insertEduParentQuestion(eduParentQuestion));
    }

    /**
     * 修改家长问题
     */
    @PreAuthorize("@ss.hasPermi('edu:question:edit')")
    @Log(title = "家长问题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduParentQuestion eduParentQuestion)
    {
        return toAjax(eduParentQuestionService.updateEduParentQuestion(eduParentQuestion));
    }

    /**
     * 删除家长问题
     */
    @PreAuthorize("@ss.hasPermi('edu:question:remove')")
    @Log(title = "家长问题", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduParentQuestionService.deleteEduParentQuestionByIds(ids));
    }
}
