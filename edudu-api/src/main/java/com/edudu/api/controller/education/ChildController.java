//package com.edudu.api.controller.education;
//
//import com.edudu.common.core.domain.AjaxResult;
//import com.edudu.system.domain.Child;
//import com.edudu.system.service.IChildService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 孩子管理接口
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/child")
//@RequiredArgsConstructor
//@Tag(name = "孩子管理接口")
//public class ChildController {
//
//    private final IChildService childService;
//
//    /**
//     * 获取孩子列表
//     */
//    @GetMapping("/list")
//    @Operation(summary = "获取孩子列表")
//    public AjaxResult list(@Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
//        return AjaxResult.success(childService.selectChildList(new Child()));
//    }
//
//    /**
//     * 获取孩子详情
//     */
//    @GetMapping("/{childId}")
//    @Operation(summary = "获取孩子详情")
//    public AjaxResult getInfo(@PathVariable("childId") Long childId) {
//        return AjaxResult.success(childService.selectChildById(childId));
//    }
//
//    /**
//     * 添加孩子
//     */
//    @PostMapping
//    @Operation(summary = "添加孩子")
//    public AjaxResult add(@Validated @RequestBody Child child) {
//        return AjaxResult.success(childService.insertChild(child));
//    }
//
//    /**
//     * 修改孩子
//     */
//    @PutMapping
//    @Operation(summary = "修改孩子")
//    public AjaxResult edit(@Validated @RequestBody Child child) {
//        return AjaxResult.success(childService.updateChild(child));
//    }
//
//    /**
//     * 删除孩子
//     */
//    @DeleteMapping("/{childId}")
//    @Operation(summary = "删除孩子")
//    public AjaxResult remove(@PathVariable Long childId) {
//        return AjaxResult.success(childService.deleteChildById(childId));
//    }
//
//    /**
//     * 计算孩子年龄
//     */
//    @GetMapping("/age/{childId}")
//    @Operation(summary = "计算孩子年龄")
//    public AjaxResult calculateAge(@PathVariable Long childId) {
//        Child child = childService.selectChildById(childId);
//        if (child == null || child.getBirthDate() == null) {
//            return AjaxResult.error("孩子不存在或未设置出生日期");
//        }
//        int age = childService.calculateAge(child.getBirthDate());
//        return AjaxResult.success("age", age);
//    }
//}
