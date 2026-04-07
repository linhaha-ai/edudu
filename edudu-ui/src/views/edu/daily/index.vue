<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="任务模板ID" prop="taskId">
        <el-input
          v-model="queryParams.taskId"
          placeholder="请输入任务模板ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="训练目标" prop="goal">
        <el-input
          v-model="queryParams.goal"
          placeholder="请输入训练目标"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="注意事项" prop="tips">
        <el-input
          v-model="queryParams.tips"
          placeholder="请输入注意事项"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="建议时长" prop="duration">
        <el-input
          v-model="queryParams.duration"
          placeholder="请输入建议时长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务日期" prop="taskDate">
        <el-date-picker clearable
          v-model="queryParams.taskDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择任务日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="完成时间" prop="completedAt">
        <el-date-picker clearable
          v-model="queryParams.completedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['edu:daily:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['edu:daily:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['edu:daily:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['edu:daily:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dailyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="分配ID" align="center" prop="id" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="孩子ID" align="center" prop="childId" />
      <el-table-column label="任务模板ID" align="center" prop="taskId" />
      <el-table-column label="任务名称" align="center" prop="title" />
      <el-table-column label="训练目标" align="center" prop="goal" />
      <el-table-column label="操作步骤" align="center" prop="steps" />
      <el-table-column label="注意事项" align="center" prop="tips" />
      <el-table-column label="建议时长" align="center" prop="duration" />
      <el-table-column label="任务日期" align="center" prop="taskDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="完成时间" align="center" prop="completedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['edu:daily:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['edu:daily:remove']"
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

    <!-- 添加或修改每日任务分配对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="form.userId" placeholder="请输入用户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="孩子ID" prop="childId">
              <el-input v-model="form.childId" placeholder="请输入孩子ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务模板ID" prop="taskId">
              <el-input v-model="form.taskId" placeholder="请输入任务模板ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务名称" prop="title">
              <el-input v-model="form.title" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="训练目标" prop="goal">
              <el-input v-model="form.goal" placeholder="请输入训练目标" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作步骤" prop="steps">
              <el-input v-model="form.steps" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="注意事项" prop="tips">
              <el-input v-model="form.tips" placeholder="请输入注意事项" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="建议时长" prop="duration">
              <el-input v-model="form.duration" placeholder="请输入建议时长" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务日期" prop="taskDate">
              <el-date-picker clearable
                v-model="form.taskDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择任务日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="完成时间" prop="completedAt">
              <el-date-picker clearable
                v-model="form.completedAt"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择完成时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDaily, getDaily, delDaily, addDaily, updateDaily } from "@/api/edu/daily"

export default {
  name: "Daily",
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
      // 每日任务分配表格数据
      dailyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        childId: null,
        taskId: null,
        title: null,
        goal: null,
        steps: null,
        tips: null,
        duration: null,
        taskDate: null,
        status: null,
        completedAt: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        childId: [
          { required: true, message: "孩子ID不能为空", trigger: "blur" }
        ],
        taskId: [
          { required: true, message: "任务模板ID不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "任务名称不能为空", trigger: "blur" }
        ],
        taskDate: [
          { required: true, message: "任务日期不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询每日任务分配列表 */
    getList() {
      this.loading = true
      listDaily(this.queryParams).then(response => {
        this.dailyList = response.rows
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
        taskId: null,
        title: null,
        goal: null,
        steps: null,
        tips: null,
        duration: null,
        taskDate: null,
        status: null,
        completedAt: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加每日任务分配"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDaily(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改每日任务分配"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDaily(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDaily(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除每日任务分配编号为"' + ids + '"的数据项？').then(function() {
        return delDaily(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('edu/daily/export', {
        ...this.queryParams
      }, `daily_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
