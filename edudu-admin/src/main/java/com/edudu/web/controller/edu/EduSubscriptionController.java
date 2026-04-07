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
import com.edudu.edu.domain.EduSubscription;
import com.edudu.edu.service.IEduSubscriptionService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 订阅Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/subscription")
public class EduSubscriptionController extends BaseController
{
    @Autowired
    private IEduSubscriptionService eduSubscriptionService;

    /**
     * 查询订阅列表
     */
    @PreAuthorize("@ss.hasPermi('edu:subscription:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduSubscription eduSubscription)
    {
        startPage();
        List<EduSubscription> list = eduSubscriptionService.selectEduSubscriptionList(eduSubscription);
        return getDataTable(list);
    }

    /**
     * 导出订阅列表
     */
    @PreAuthorize("@ss.hasPermi('edu:subscription:export')")
    @Log(title = "订阅", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduSubscription eduSubscription)
    {
        List<EduSubscription> list = eduSubscriptionService.selectEduSubscriptionList(eduSubscription);
        ExcelUtil<EduSubscription> util = new ExcelUtil<EduSubscription>(EduSubscription.class);
        util.exportExcel(response, list, "订阅数据");
    }

    /**
     * 获取订阅详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:subscription:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduSubscriptionService.selectEduSubscriptionById(id));
    }

    /**
     * 新增订阅
     */
    @PreAuthorize("@ss.hasPermi('edu:subscription:add')")
    @Log(title = "订阅", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduSubscription eduSubscription)
    {
        return toAjax(eduSubscriptionService.insertEduSubscription(eduSubscription));
    }

    /**
     * 修改订阅
     */
    @PreAuthorize("@ss.hasPermi('edu:subscription:edit')")
    @Log(title = "订阅", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduSubscription eduSubscription)
    {
        return toAjax(eduSubscriptionService.updateEduSubscription(eduSubscription));
    }

    /**
     * 删除订阅
     */
    @PreAuthorize("@ss.hasPermi('edu:subscription:remove')")
    @Log(title = "订阅", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduSubscriptionService.deleteEduSubscriptionByIds(ids));
    }
}
