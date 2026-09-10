package com.edudu.api.controller.ai;

import com.edudu.ai.service.TaskAiService;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.utils.DateUtils;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.edu.domain.EduTaskDaily;
import com.edudu.edu.domain.EduTaskRecord;
import com.edudu.edu.service.IEduTaskDailyService;
import com.edudu.edu.service.IEduTaskRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AI早教任务接口
 */
@Slf4j
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "AI早教任务接口")
public class TaskController {

    private final TaskAiService taskAiService;

    @Autowired(required = false)
    private IEduTaskDailyService taskDailyService;

    @Autowired(required = false)
    private IEduTaskRecordService taskRecordService;

    /**
     * 生成早教任务
     */
    @PostMapping("/generate")
    @Operation(summary = "生成早教任务", description = "根据孩子年龄和主题生成早教任务")
    public AjaxResult generateTask(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "主题") @RequestParam String theme) {

        String result = taskAiService.generateTask(age, theme);
        return AjaxResult.success(result);
    }

    /**
     * 生成每日任务
     */
    @PostMapping("/daily")
    @Operation(summary = "生成每日任务", description = "根据孩子年龄段自动匹配合适的主题")
    public AjaxResult generateDailyTask(
            @Parameter(description = "孩子年龄") @RequestParam int age) {

        String result = taskAiService.generateTaskByAgeGroup(age);
        return AjaxResult.success(result);
    }

    /**
     * 获取今日任务列表
     */
    @GetMapping("/today")
    @Operation(summary = "获取今日任务")
    public AjaxResult getTodayTasks(@Parameter(description = "孩子ID") @RequestParam Long childId) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduTaskDaily query = new EduTaskDaily();
        query.setChildId(childId);
        query.setTaskDate(DateUtils.getNowDate());
        query.setUserId(loginUser.getUserId());

        List<EduTaskDaily> tasks = taskDailyService.selectEduTaskDailyList(query);

        // 转换为前端需要的格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (EduTaskDaily task : tasks) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", task.getId());
            item.put("childId", task.getChildId());
            item.put("title", task.getTitle());
            item.put("description", task.getGoal());
            item.put("type", "cognitive");
            item.put("duration", task.getDuration());
            item.put("steps", task.getSteps());
            item.put("speakingScript", task.getTips());
            item.put("isCompleted", "1".equals(task.getStatus()));
            item.put("completedAt", task.getCompletedAt());
            result.add(item);
        }

        return AjaxResult.success(result);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{taskId}")
    @Operation(summary = "获取任务详情")
    public AjaxResult getTaskDetail(@PathVariable Long taskId) {
        EduTaskDaily task = taskDailyService.selectEduTaskDailyById(taskId);
        if (task == null) {
            return AjaxResult.error("任务不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", task.getId());
        result.put("childId", task.getChildId());
        result.put("title", task.getTitle());
        result.put("goal", task.getGoal());
        result.put("steps", task.getSteps());
        result.put("tips", task.getTips());
        result.put("duration", task.getDuration());
        result.put("status", task.getStatus());
        result.put("completedAt", task.getCompletedAt());

        return AjaxResult.success(result);
    }

    /**
     * 完成任务
     */
    @PostMapping("/{taskId}/complete")
    @Operation(summary = "完成任务")
    public AjaxResult completeTask(
            @PathVariable Long taskId,
            @Parameter(description = "完成备注") @RequestParam(required = false) String note) {

        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduTaskDaily task = taskDailyService.selectEduTaskDailyById(taskId);
        if (task == null) {
            return AjaxResult.error("任务不存在");
        }

        // 更新任务状态
        task.setStatus("1");
        task.setCompletedAt(DateUtils.getNowDate());
        taskDailyService.updateEduTaskDaily(task);

        // 记录完成
        EduTaskRecord record = new EduTaskRecord();
        record.setUserId(loginUser.getUserId());
        record.setChildId(task.getChildId());
        record.setTaskId(task.getTaskId());
        record.setTaskTitle(task.getTitle());
        record.setStatus("1");
        record.setFeedback(note);
        record.setCompletedAt(DateUtils.getNowDate());
        record.setRecordDate(DateUtils.getNowDate());
        taskRecordService.insertEduTaskRecord(record);

        return AjaxResult.success("任务完成");
    }

    /**
     * 获取任务统计
     */
    @GetMapping("/stats")
    @Operation(summary = "获取任务统计")
    public AjaxResult getTaskStats(@Parameter(description = "孩子ID") @RequestParam Long childId) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        // 获取今日任务
        EduTaskDaily todayQuery = new EduTaskDaily();
        todayQuery.setChildId(childId);
        todayQuery.setTaskDate(DateUtils.getNowDate());
        todayQuery.setUserId(loginUser.getUserId());
        List<EduTaskDaily> todayTasks = taskDailyService.selectEduTaskDailyList(todayQuery);

        // 获取已完成今日任务
        long todayCompleted = todayTasks.stream()
                .filter(t -> "1".equals(t.getStatus()))
                .count();

        // 获取本周统计
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        List<Map<String, Object>> weekStats = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            Date day = cal.getTime();
            EduTaskDaily dayQuery = new EduTaskDaily();
            dayQuery.setChildId(childId);
            dayQuery.setTaskDate(day);
            List<EduTaskDaily> dayTasks = taskDailyService.selectEduTaskDailyList(dayQuery);

            Map<String, Object> dayStat = new HashMap<>();
            dayStat.put("date", DateUtils.parseDateToStr("yyyy-MM-dd", day));
            dayStat.put("completed", dayTasks.stream().filter(t -> "1".equals(t.getStatus())).count());
            dayStat.put("total", dayTasks.size());
            weekStats.add(dayStat);
        }

        // 统计结果
        Map<String, Object> result = new HashMap<>();
        result.put("todayTotal", todayTasks.size());
        result.put("todayCompleted", todayCompleted);
        result.put("weekStats", weekStats);
        result.put("typeStats", new ArrayList<>());
        result.put("streakDays", 0);

        return AjaxResult.success(result);
    }
}
