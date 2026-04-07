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
import com.edudu.edu.domain.EduSubscriptionPlan;
import com.edudu.edu.service.IEduSubscriptionPlanService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 订阅计划配置Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/plan")
public class EduSubscriptionPlanController extends BaseController
{
    @Autowired
    private IEduSubscriptionPlanService eduSubscriptionPlanService;

    /**
     * 查询订阅计划配置列表
     */
    @PreAuthorize("@ss.hasPermi('edu:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduSubscriptionPlan eduSubscriptionPlan)
    {
        startPage();
        List<EduSubscriptionPlan> list = eduSubscriptionPlanService.selectEduSubscriptionPlanList(eduSubscriptionPlan);
        return getDataTable(list);
    }

    /**
     * 导出订阅计划配置列表
     */
    @PreAuthorize("@ss.hasPermi('edu:plan:export')")
    @Log(title = "订阅计划配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduSubscriptionPlan eduSubscriptionPlan)
    {
        List<EduSubscriptionPlan> list = eduSubscriptionPlanService.selectEduSubscriptionPlanList(eduSubscriptionPlan);
        ExcelUtil<EduSubscriptionPlan> util = new ExcelUtil<EduSubscriptionPlan>(EduSubscriptionPlan.class);
        util.exportExcel(response, list, "订阅计划配置数据");
    }

    /**
     * 获取订阅计划配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:plan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduSubscriptionPlanService.selectEduSubscriptionPlanById(id));
    }

    /**
     * 新增订阅计划配置
     */
    @PreAuthorize("@ss.hasPermi('edu:plan:add')")
    @Log(title = "订阅计划配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduSubscriptionPlan eduSubscriptionPlan)
    {
        return toAjax(eduSubscriptionPlanService.insertEduSubscriptionPlan(eduSubscriptionPlan));
    }

    /**
     * 修改订阅计划配置
     */
    @PreAuthorize("@ss.hasPermi('edu:plan:edit')")
    @Log(title = "订阅计划配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduSubscriptionPlan eduSubscriptionPlan)
    {
        return toAjax(eduSubscriptionPlanService.updateEduSubscriptionPlan(eduSubscriptionPlan));
    }

    /**
     * 删除订阅计划配置
     */
    @PreAuthorize("@ss.hasPermi('edu:plan:remove')")
    @Log(title = "订阅计划配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduSubscriptionPlanService.deleteEduSubscriptionPlanByIds(ids));
    }
}
