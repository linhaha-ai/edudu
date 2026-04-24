<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="孩子ID" prop="childId">
        <el-input
          v-model="queryParams.childId"
          placeholder="请输入孩子ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="故事场景" prop="scene">
        <el-input
          v-model="queryParams.scene"
          placeholder="请输入故事场景"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="故事标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入故事标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          clearable
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
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
          v-hasPermi="['edu:storyRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['edu:storyRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storyRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="故事ID" align="center" prop="id" width="80" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="孩子ID" align="center" prop="childId" width="80" />
      <el-table-column label="年龄" align="center" prop="age" width="60" />
      <el-table-column label="故事场景" align="center" prop="scene" width="120" />
      <el-table-column label="教育目标" align="center" prop="goal" width="150" show-overflow-tooltip />
      <el-table-column label="故事标题" align="center" prop="title" width="200" show-overflow-tooltip />
      <el-table-column label="模型类型" align="center" prop="modelType" width="100" />
      <el-table-column label="消耗Token" align="center" prop="tokensUsed" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['edu:storyRecord:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['edu:storyRecord:remove']"
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

    <!-- 故事详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="故事ID">
              <el-input v-model="form.id" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户ID">
              <el-input v-model="form.userId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="孩子ID">
              <el-input v-model="form.childId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input v-model="form.age" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="故事场景">
              <el-input v-model="form.scene" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教育目标">
              <el-input v-model="form.goal" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故事标题">
              <el-input v-model="form.title" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="教育意义">
              <el-input v-model="form.moral" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故事内容">
              <el-input v-model="form.content" type="textarea" :rows="12" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型类型">
              <el-input v-model="form.modelType" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="消耗Token">
              <el-input v-model="form.tokensUsed" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间">
              <el-input v-model="parseTime(form.createTime)" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStoryRecord, getStoryRecord, delStoryRecord } from "@/api/edu/storyRecord"

export default {
  name: "StoryRecord",
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
      // 故事记录表格数据
      storyRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        childId: null,
        scene: null,
        title: null,
      },
      // 表单参数
      form: {},
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询故事记录列表 */
    getList() {
      this.loading = true
      // 处理日期范围
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryParams.params = {
          beginTime: this.dateRange[0],
          endTime: this.dateRange[1]
        }
      } else {
        this.queryParams.params = {}
      }
      listStoryRecord(this.queryParams).then(response => {
        this.storyRecordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        childId: null,
        age: null,
        scene: null,
        goal: null,
        title: null,
        content: null,
        moral: null,
        modelType: null,
        tokensUsed: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 查看详情按钮操作 */
    handleView(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getStoryRecord(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "故事详情"
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除故事记录编号为"' + ids + '"的数据项？').then(function() {
        return delStoryRecord(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('edu/storyRecord/export', {
        ...this.queryParams
      }, `storyRecord_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
