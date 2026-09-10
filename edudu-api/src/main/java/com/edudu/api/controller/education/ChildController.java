package com.edudu.api.controller.education;

import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.utils.DateUtils;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.edu.domain.EduChild;
import com.edudu.edu.service.IEduChildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 孩子管理接口
 */
@Slf4j
@RestController
@RequestMapping("/child")
@RequiredArgsConstructor
@Tag(name = "孩子管理接口")
public class ChildController {

    @Autowired
    private IEduChildService childService;

    /**
     * 获取孩子列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取孩子列表")
    public AjaxResult list() {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduChild query = new EduChild();
        query.setUserId(loginUser.getUserId());
        List<EduChild> list = childService.selectEduChildList(query);

        // 计算年龄
        for (EduChild child : list) {
            if (child.getBirthDate() != null) {
                int age = calculateAge(child.getBirthDate());
                child.setRemark(String.valueOf(age)); // 临时存储年龄
            }
        }

        return AjaxResult.success(list);
    }

    /**
     * 获取孩子详情
     */
    @GetMapping("/{childId}")
    @Operation(summary = "获取孩子详情")
    public AjaxResult getInfo(@PathVariable("childId") Long childId) {
        EduChild child = childService.selectEduChildByChildId(childId);
        if (child == null) {
            return AjaxResult.error("孩子不存在");
        }
        return AjaxResult.success(child);
    }

    /**
     * 添加孩子
     */
    @PostMapping("/add")
    @Operation(summary = "添加孩子")
    public AjaxResult add(@Validated @RequestBody EduChild child) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        child.setUserId(loginUser.getUserId());
        child.setDelFlag("0");
        int result = childService.insertEduChild(child);

        if (result > 0) {
            return AjaxResult.success(child);
        }
        return AjaxResult.error("添加失败");
    }

    /**
     * 修改孩子
     */
    @PostMapping("/{childId}/update")
    @Operation(summary = "修改孩子信息")
    public AjaxResult edit(@PathVariable Long childId, @Validated @RequestBody EduChild child) {
        child.setChildId(childId);
        int result = childService.updateEduChild(child);

        if (result > 0) {
            return AjaxResult.success("修改成功");
        }
        return AjaxResult.error("修改失败");
    }

    /**
     * 删除孩子
     */
    @PostMapping("/{childId}/delete")
    @Operation(summary = "删除孩子")
    public AjaxResult remove(@PathVariable Long childId) {
        int result = childService.deleteEduChildByChildId(childId);

        if (result > 0) {
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }

    /**
     * 计算孩子年龄
     */
    @GetMapping("/age/{childId}")
    @Operation(summary = "计算孩子年龄")
    public AjaxResult calculateAge(@PathVariable Long childId) {
        EduChild child = childService.selectEduChildByChildId(childId);
        if (child == null || child.getBirthDate() == null) {
            return AjaxResult.error("孩子不存在或未设置出生日期");
        }

        int age = calculateAge(child.getBirthDate());
        return AjaxResult.success("age", age);
    }

    /**
     * 计算年龄（根据出生日期）
     */
    private int calculateAge(java.util.Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        java.util.Calendar now = java.util.Calendar.getInstance();
        java.util.Calendar birth = java.util.Calendar.getInstance();
        birth.setTime(birthDate);

        int age = now.get(java.util.Calendar.YEAR) - birth.get(java.util.Calendar.YEAR);
        if (now.get(java.util.Calendar.DAY_OF_YEAR) < birth.get(java.util.Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}
