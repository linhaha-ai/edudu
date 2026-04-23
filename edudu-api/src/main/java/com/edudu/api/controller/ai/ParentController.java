package com.edudu.api.controller.ai;

import com.edudu.ai.service.ParentAiService;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.utils.DateUtils;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.edu.domain.EduParentAnswer;
import com.edudu.edu.domain.EduParentQuestion;
import com.edudu.edu.service.IEduParentAnswerService;
import com.edudu.edu.service.IEduParentQuestionService;
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
 * AI家长助手接口
 */
@Slf4j
@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
@Tag(name = "AI家长助手接口")
public class ParentController {

    private final ParentAiService parentAiService;

    @Autowired(required = false)
    private IEduParentQuestionService questionService;

    @Autowired(required = false)
    private IEduParentAnswerService answerService;

    /**
     * 提交育儿问题
     */
    @PostMapping("/question")
    @Operation(summary = "提交育儿问题", description = "提交育儿问题，获取AI回答")
    public AjaxResult askQuestion(
            @Parameter(description = "孩子ID") @RequestParam Long childId,
            @Parameter(description = "问题描述") @RequestParam String question) {

        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        // 获取孩子年龄
        int age = 4; // 默认年龄

        // 保存问题
        EduParentQuestion q = new EduParentQuestion();
        q.setUserId(loginUser.getUserId());
        q.setChildId(childId);
        q.setQuestion(question);
        q.setStatus("0");
        questionService.insertEduParentQuestion(q);

        // AI回答（传入userId支持对话记忆和长期记忆）
        String answer = parentAiService.answerQuestion(age, loginUser.getUserId(), question);

        // 保存回答
        EduParentAnswer a = new EduParentAnswer();
        a.setQuestionId(q.getId());
        a.setAnalysis("根据问题分析：");
        a.setSolutions("[\"建议一\",\"建议二\",\"建议三\"]");
        a.setExample("沟通示例：");
        answerService.insertEduParentAnswer(a);

        // 更新问题状态
        q.setStatus("1");
        questionService.updateEduParentQuestion(q);

        Map<String, Object> result = new HashMap<>();
        result.put("question", q);
        result.put("answer", a);

        return AjaxResult.success(result);
    }

    /**
     * 获取问答历史
     */
    @GetMapping("/history")
    @Operation(summary = "获取问答历史")
    public AjaxResult getQuestionHistory(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {

        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduParentQuestion query = new EduParentQuestion();
        query.setUserId(loginUser.getUserId());

        PageHelper.startPage(page, pageSize);
        List<EduParentQuestion> questions = questionService.selectEduParentQuestionList(query);
        PageInfo<EduParentQuestion> pageInfo = new PageInfo<>(questions);

        // 获取每个问题的回答
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (EduParentQuestion q : questions) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", q.getId());
            item.put("userId", q.getUserId());
            item.put("childId", q.getChildId());
            item.put("question", q.getQuestion());
            item.put("createdAt", q.getCreateTime());

            // 获取回答
            EduParentAnswer answer = answerService.selectEduParentAnswerByQuestionId(q.getId());
            if (answer != null) {
                item.put("answer", answer);
            } else {
                item.put("answer", null);
            }
            resultList.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", resultList);
        result.put("total", pageInfo.getTotal());

        return AjaxResult.success(result);
    }

    /**
     * 获取问答详情
     */
    @GetMapping("/question/{questionId}")
    @Operation(summary = "获取问答详情")
    public AjaxResult getQuestionDetail(@PathVariable Long questionId) {
        EduParentQuestion question = questionService.selectEduParentQuestionById(questionId);
        if (question == null) {
            return AjaxResult.error("问题不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", question.getId());
        result.put("userId", question.getUserId());
        result.put("childId", question.getChildId());
        result.put("question", question.getQuestion());
        result.put("createdAt", question.getCreateTime());

        EduParentAnswer answer = answerService.selectEduParentAnswerByQuestionId(questionId);
        result.put("answer", answer);

        return AjaxResult.success(result);
    }

    /**
     * 生成成长报告
     */
    @PostMapping("/report")
    @Operation(summary = "生成成长报告", description = "根据行为数据生成AI成长分析报告")
    public AjaxResult generateGrowthReport(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "行为数据JSON") @RequestParam String data) {

        String result = parentAiService.generateGrowthReport(age, data);
        return AjaxResult.success(result);
    }
}
