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
      <el-form-item label="孩子年龄" prop="age">
        <el-input
          v-model="queryParams.age"
          placeholder="请输入孩子年龄"
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
      <el-form-item label="教育目标" prop="goal">
        <el-input
          v-model="queryParams.goal"
          placeholder="请输入教育目标"
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
      <el-form-item label="教育意义" prop="moral">
        <el-input
          v-model="queryParams.moral"
          placeholder="请输入教育意义"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消耗token数" prop="tokensUsed">
        <el-input
          v-model="queryParams.tokensUsed"
          placeholder="请输入消耗token数"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['edu:storeRecord:add']"
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
          v-hasPermi="['edu:storeRecord:edit']"
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
          v-hasPermi="['edu:storeRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['edu:storeRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storeRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="故事ID" align="center" prop="id" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="孩子ID" align="center" prop="childId" />
      <el-table-column label="孩子年龄" align="center" prop="age" />
      <el-table-column label="故事场景" align="center" prop="scene" />
      <el-table-column label="教育目标" align="center" prop="goal" />
      <el-table-column label="故事标题" align="center" prop="title" />
      <el-table-column label="故事内容" align="center" prop="content" />
      <el-table-column label="教育意义" align="center" prop="moral" />
      <el-table-column label="AI模型类型" align="center" prop="modelType" />
      <el-table-column label="消耗token数" align="center" prop="tokensUsed" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['edu:storeRecord:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['edu:storeRecord:remove']"
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

    <!-- 添加或修改故事记录对话框 -->
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
            <el-form-item label="孩子年龄" prop="age">
              <el-input v-model="form.age" placeholder="请输入孩子年龄" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故事场景" prop="scene">
              <el-input v-model="form.scene" placeholder="请输入故事场景" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="教育目标" prop="goal">
              <el-input v-model="form.goal" placeholder="请输入教育目标" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故事标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入故事标题" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故事内容">
              <editor v-model="form.content" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="教育意义" prop="moral">
              <el-input v-model="form.moral" placeholder="请输入教育意义" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="消耗token数" prop="tokensUsed">
              <el-input v-model="form.tokensUsed" placeholder="请输入消耗token数" />
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
import { listStoreRecord, getStoreRecord, delStoreRecord, addStoreRecord, updateStoreRecord } from "@/api/edu/storeRecord"

export default {
  name: "StoreRecord",
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
      storeRecordList: [],
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
        age: null,
        scene: null,
        goal: null,
        title: null,
        content: null,
        moral: null,
        modelType: null,
        tokensUsed: null,
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
        title: [
          { required: true, message: "故事标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "故事内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询故事记录列表 */
    getList() {
      this.loading = true
      listStoreRecord(this.queryParams).then(response => {
        this.storeRecordList = response.rows
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
      this.title = "添加故事记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getStoreRecord(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改故事记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStoreRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addStoreRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除故事记录编号为"' + ids + '"的数据项？').then(function() {
        return delStoreRecord(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('edu/storeRecord/export', {
        ...this.queryParams
      }, `storeRecord_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
