//package com.edudu.api.controller.education;
//
//import com.edudu.common.core.domain.AjaxResult;
//import com.edudu.system.domain.GrowthRecord;
//import com.edudu.system.service.IGrowthRecordService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 成长记录接口
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/growth")
//@RequiredArgsConstructor
//@Tag(name = "成长记录接口")
//public class GrowthController {
//
//    private final IGrowthRecordService growthRecordService;
//
//    /**
//     * 获取成长记录列表
//     */
//    @GetMapping("/list")
//    @Operation(summary = "获取成长记录列表")
//    public AjaxResult list(
//            @Parameter(description = "孩子ID") @RequestParam Long childId,
//            @Parameter(description = "记录类型") @RequestParam(required = false) String type,
//            @Parameter(description = "记录日期") @RequestParam(required = false) String recordDate) {
//        GrowthRecord record = new GrowthRecord();
//        record.setChildId(childId);
//        record.setType(type);
//        return AjaxResult.success(growthRecordService.selectGrowthRecordList(record));
//    }
//
//    /**
//     * 添加成长记录
//     */
//    @PostMapping
//    @Operation(summary = "添加成长记录")
//    public AjaxResult add(@Validated @RequestBody GrowthRecord record) {
//        return AjaxResult.success(growthRecordService.insertGrowthRecord(record));
//    }
//
//    /**
//     * 修改成长记录
//     */
//    @PutMapping
//    @Operation(summary = "修改成长记录")
//    public AjaxResult edit(@Validated @RequestBody GrowthRecord record) {
//        return AjaxResult.success(growthRecordService.updateGrowthRecord(record));
//    }
//
//    /**
//     * 删除成长记录
//     */
//    @DeleteMapping("/{id}")
//    @Operation(summary = "删除成长记录")
//    public AjaxResult remove(@PathVariable Long id) {
//        return AjaxResult.success(growthRecordService.deleteGrowthRecordById(id));
//    }
//
//    /**
//     * 生成成长报告
//     */
//    @GetMapping("/report/{childId}")
//    @Operation(summary = "生成成长报告")
//    public AjaxResult generateReport(
//            @PathVariable Long childId,
//            @Parameter(description = "报告周期") @RequestParam(required = false) String period) {
//        // TODO: 调用AI服务生成报告
//        return AjaxResult.success("报告生成中...");
//    }
//}
