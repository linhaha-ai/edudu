package com.edudu.api.controller.education;

import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.utils.DateUtils;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.edu.domain.EduGrowthRecord;
import com.edudu.edu.domain.EduGrowthReport;
import com.edudu.edu.service.IEduGrowthRecordService;
import com.edudu.edu.service.IEduGrowthReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 成长记录接口
 */
@Slf4j
@RestController
@RequestMapping("/growth")
@Tag(name = "成长记录接口")
public class GrowthController {

    @Autowired(required = false)
    private IEduGrowthRecordService growthRecordService;

    @Autowired(required = false)
    private IEduGrowthReportService growthReportService;

    /**
     * 获取成长记录列表
     */
    @GetMapping("/records")
    @Operation(summary = "获取成长记录列表")
    public AjaxResult getRecords(
            @Parameter(description = "孩子ID") @RequestParam Long childId,
            @Parameter(description = "记录类型") @RequestParam(required = false) String type,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {

        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduGrowthRecord query = new EduGrowthRecord();
        query.setChildId(childId);
        query.setUserId(loginUser.getUserId());
        if (type != null && !type.isEmpty()) {
            query.setType(type);
        }

        PageHelper.startPage(page, pageSize);
        List<EduGrowthRecord> list = growthRecordService.selectEduGrowthRecordList(query);
        PageInfo<EduGrowthRecord> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", pageInfo.getTotal());

        return AjaxResult.success(result);
    }

    /**
     * 添加成长记录
     */
    @PostMapping("/record/add")
    @Operation(summary = "添加成长记录")
    public AjaxResult addRecord(@RequestBody EduGrowthRecord record) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        record.setUserId(loginUser.getUserId());
        int result = growthRecordService.insertEduGrowthRecord(record);
        if (result > 0) {
            return AjaxResult.success(record);
        }
        return AjaxResult.error("添加失败");
    }

    /**
     * 更新成长记录
     */
    @PostMapping("/record/{recordId}/update")
    @Operation(summary = "更新成长记录")
    public AjaxResult updateRecord(@PathVariable Long recordId, @RequestBody EduGrowthRecord record) {
        record.setId(recordId);
        int result = growthRecordService.updateEduGrowthRecord(record);
        if (result > 0) {
            return AjaxResult.success("更新成功");
        }
        return AjaxResult.error("更新失败");
    }

    /**
     * 删除成长记录
     */
    @PostMapping("/record/{recordId}/delete")
    @Operation(summary = "删除成长记录")
    public AjaxResult deleteRecord(@PathVariable Long recordId) {
        int result = growthRecordService.deleteEduGrowthRecordById(recordId);
        if (result > 0) {
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }

    /**
     * 获取成长统计
     */
    @GetMapping("/stats")
    @Operation(summary = "获取成长统计")
    public AjaxResult getStats(
            @Parameter(description = "孩子ID") @RequestParam Long childId,
            @Parameter(description = "周期") @RequestParam(defaultValue = "week") String period) {

        EduGrowthRecord query = new EduGrowthRecord();
        query.setChildId(childId);
        List<EduGrowthRecord> records = growthRecordService.selectEduGrowthRecordList(query);

        // 统计
        double sleepAvg = 0;
        int dietCount = 0;
        int learningHours = 0;
        int sleepCount = 0;

        for (EduGrowthRecord record : records) {
            try {
                switch (record.getType()) {
                    case "sleep":
                        sleepAvg += Double.parseDouble(record.getValue());
                        sleepCount++;
                        break;
                    case "diet":
                        dietCount++;
                        break;
                    case "learning":
                        learningHours += Integer.parseInt(record.getValue());
                        break;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        if (sleepCount > 0) {
            sleepAvg = sleepAvg / sleepCount;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sleepAvg", sleepAvg);
        result.put("dietCount", dietCount);
        result.put("learningHours", learningHours);
        result.put("records", new ArrayList<>());

        return AjaxResult.success(result);
    }

    /**
     * 生成AI成长报告
     */
    @PostMapping("/report/generate")
    @Operation(summary = "生成AI成长报告")
    public AjaxResult generateReport(@RequestBody Map<String, Object> params) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        Long childId = Long.valueOf(params.get("childId").toString());
        String period = params.getOrDefault("period", "week").toString();

        EduGrowthReport report = new EduGrowthReport();
        report.setUserId(loginUser.getUserId());
        report.setChildId(childId);
        report.setPeriod(period);
        report.setSummary("本周孩子表现良好，继续保持！");
        report.setReportDate(DateUtils.getNowDate());
        growthReportService.insertEduGrowthReport(report);

        Map<String, Object> result = new HashMap<>();
        result.put("id", report.getId());
        result.put("childId", childId);
        result.put("period", period);
        result.put("content", report.getSummary());
        result.put("aiSuggestions", Arrays.asList("继续保持", "加强互动"));

        return AjaxResult.success(result);
    }

    /**
     * 获取成长报告列表
     */
    @GetMapping("/reports")
    @Operation(summary = "获取成长报告列表")
    public AjaxResult getReports(@Parameter(description = "孩子ID") @RequestParam Long childId) {
        List<EduGrowthReport> reports = growthReportService.selectEduGrowthReportListByChildId(childId);
        return AjaxResult.success(reports);
    }
}
