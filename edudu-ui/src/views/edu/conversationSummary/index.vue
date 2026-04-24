<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="会话ID" prop="conversationId">
        <el-input
          v-model="queryParams.conversationId"
          placeholder="请输入会话ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['edu:conversationSummary:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['edu:conversationSummary:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="conversationSummaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="摘要ID" align="center" prop="id" width="80" />
      <el-table-column label="会话ID" align="center" prop="conversationId" width="150" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="对话摘要" align="center" prop="summary" min-width="300" show-overflow-tooltip />
      <el-table-column label="消息数" align="center" prop="messageCount" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['edu:conversationSummary:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['edu:conversationSummary:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看AI对话摘要对话框 -->
    <el-dialog title="对话摘要详情" :visible.sync="viewOpen" width="700px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="摘要ID">{{ viewForm.id }}</el-descriptions-item>
        <el-descriptions-item label="会话ID">{{ viewForm.conversationId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ viewForm.userId }}</el-descriptions-item>
        <el-descriptions-item label="消息数">{{ viewForm.messageCount }}</el-descriptions-item>
        <el-descriptions-item label="对话摘要">
          <div style="max-height: 400px; overflow-y: auto; white-space: pre-wrap;">{{ viewForm.summary }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listConversationSummary, getConversationSummary, delConversationSummary } from "@/api/edu/conversationSummary"

export default {
  name: "ConversationSummary",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // AI对话摘要表格数据
      conversationSummaryList: [],
      // 是否显示查看对话框
      viewOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        conversationId: null,
        userId: null
      },
      // 查看表单参数
      viewForm: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询AI对话摘要列表 */
    getList() {
      this.loading = true
      listConversationSummary(this.queryParams).then(response => {
        this.conversationSummaryList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 查看详情按钮 */
    handleView(row) {
      getConversationSummary(row.id).then(response => {
        this.viewForm = response.data
        this.viewOpen = true
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除AI对话摘要编号为"' + ids + '"的数据项？').then(function() {
        return delConversationSummary(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('edu/conversationSummary/export', {
        ...this.queryParams
      }, 'conversationSummary_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
