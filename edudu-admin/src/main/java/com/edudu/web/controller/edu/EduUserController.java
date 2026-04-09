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
import com.edudu.common.core.domain.entity.EduUser;
import com.edudu.edu.service.IEduUserService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 前端用户Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/user")
public class EduUserController extends BaseController
{
    @Autowired
    private IEduUserService eduUserService;

    /**
     * 查询前端用户列表
     */
    @PreAuthorize("@ss.hasPermi('edu:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduUser eduUser)
    {
        startPage();
        List<EduUser> list = eduUserService.selectEduUserList(eduUser);
        return getDataTable(list);
    }

    /**
     * 导出前端用户列表
     */
    @PreAuthorize("@ss.hasPermi('edu:user:export')")
    @Log(title = "前端用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduUser eduUser)
    {
        List<EduUser> list = eduUserService.selectEduUserList(eduUser);
        ExcelUtil<EduUser> util = new ExcelUtil<EduUser>(EduUser.class);
        util.exportExcel(response, list, "前端用户数据");
    }

    /**
     * 获取前端用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(eduUserService.selectEduUserByUserId(userId));
    }

    /**
     * 新增前端用户
     */
    @PreAuthorize("@ss.hasPermi('edu:user:add')")
    @Log(title = "前端用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduUser eduUser)
    {
        return toAjax(eduUserService.insertEduUser(eduUser));
    }

    /**
     * 修改前端用户
     */
    @PreAuthorize("@ss.hasPermi('edu:user:edit')")
    @Log(title = "前端用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduUser eduUser)
    {
        return toAjax(eduUserService.updateEduUser(eduUser));
    }

    /**
     * 删除前端用户
     */
    @PreAuthorize("@ss.hasPermi('edu:user:remove')")
    @Log(title = "前端用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(eduUserService.deleteEduUserByUserIds(userIds));
    }
}
