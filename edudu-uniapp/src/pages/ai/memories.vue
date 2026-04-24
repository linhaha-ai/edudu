
&lt;template&gt;
  &lt;view class="memories-page"&gt;
    &lt;view class="page-header"&gt;
      &lt;text class="page-title"&gt;AI记忆管理&lt;/text&gt;
      &lt;button class="add-btn" @click="showAddDialog = true"&gt;+ 添加记忆&lt;/button&gt;
    &lt;/view&gt;

    &lt;view class="summary-card"&gt;
      &lt;text class="summary-title"&gt;🤖 孩子画像&lt;/text&gt;
      &lt;text v-if="summary" class="summary-content"&gt;{{ summary }}&lt;/text&gt;
      &lt;text v-else class="summary-empty"&gt;还没有生成孩子画像，多和AI聊聊天吧~&lt;/text&gt;
    &lt;/view&gt;

    &lt;view class="category-tabs"&gt;
      &lt;view
        v-for="cat in categories"
        :key="cat.key"
        class="category-tab"
        :class="{ active: currentCategory === cat.key }"
        @click="currentCategory = cat.key"
      &gt;
        &lt;text class="tab-icon"&gt;{{ cat.icon }}&lt;/text&gt;
        &lt;text class="tab-label"&gt;{{ cat.label }}&lt;/text&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view class="memories-list"&gt;
      &lt;view v-for="memory in filteredMemories" :key="memory.id" class="memory-item"&gt;
        &lt;view class="memory-header"&gt;
          &lt;text class="memory-key"&gt;{{ memory.key }}&lt;/text&gt;
          &lt;view class="memory-meta"&gt;
            &lt;text v-for="i in memory.importance" :key="i" class="star"&gt;⭐&lt;/text&gt;
            &lt;text class="memory-time"&gt;{{ formatDate(memory.createdAt) }}&lt;/text&gt;
          &lt;/view&gt;
        &lt;/view&gt;
        &lt;text class="memory-value"&gt;{{ memory.value }}&lt;/text&gt;
        &lt;view class="memory-actions"&gt;
          &lt;text class="delete-btn" @click="deleteMemory(memory.id)"&gt;删除&lt;/text&gt;
        &lt;/view&gt;
      &lt;/view&gt;

      &lt;view v-if="filteredMemories.length === 0" class="empty-state"&gt;
        &lt;text class="empty-icon"&gt;💭&lt;/text&gt;
        &lt;text class="empty-text"&gt;这里还没有记忆&lt;/text&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view v-if="showAddDialog" class="dialog-overlay" @click="showAddDialog = false"&gt;
      &lt;view class="dialog-content" @click.stop&gt;
        &lt;text class="dialog-title"&gt;添加记忆&lt;/text&gt;

        &lt;view class="form-item"&gt;
          &lt;text class="form-label"&gt;分类&lt;/text&gt;
          &lt;picker mode="selector" :range="categoryOptions" @change="onCategoryChange"&gt;
            &lt;view class="picker"&gt;
              &lt;text&gt;{{ categoryOptions[newMemory.categoryIndex] }}&lt;/text&gt;
              &lt;text class="arrow"&gt;›&lt;/text&gt;
            &lt;/view&gt;
          &lt;/picker&gt;
        &lt;/view&gt;

        &lt;view class="form-item"&gt;
          &lt;text class="form-label"&gt;标签&lt;/text&gt;
          &lt;input class="form-input" v-model="newMemory.key" placeholder="例如：喜欢的颜色" /&gt;
        &lt;/view&gt;

        &lt;view class="form-item"&gt;
          &lt;text class="form-label"&gt;内容&lt;/text&gt;
          &lt;textarea class="form-textarea" v-model="newMemory.value" placeholder="描述这个记忆..." :maxlength="200" /&gt;
        &lt;/view&gt;

        &lt;view class="form-item"&gt;
          &lt;text class="form-label"&gt;重要性：{{ newMemory.importance }}星&lt;/text&gt;
          &lt;view class="importance-selector"&gt;
            &lt;text
              v-for="i in 5"
              :key="i"
              class="star-btn"
              :class="{ active: i &lt;= newMemory.importance }"
              @click="newMemory.importance = i"
            &gt;
              {{ i &lt;= newMemory.importance ? '⭐' : '☆' }}
            &lt;/text&gt;
          &lt;/view&gt;
        &lt;/view&gt;

        &lt;view class="dialog-actions"&gt;
          &lt;button class="btn btn-cancel" @click="showAddDialog = false"&gt;取消&lt;/button&gt;
          &lt;button class="btn btn-primary" @click="addMemory"&gt;添加&lt;/button&gt;
        &lt;/view&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view class="bottom-safe"&gt;&lt;/view&gt;
  &lt;/view&gt;
&lt;/template&gt;

&lt;script setup lang="ts"&gt;
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { getUserMemories, getConversationSummary, createUserMemory, deleteUserMemory as deleteMemoryApi } from '@/api/ai'
import type { UserMemory } from '@/api/ai'
import { showSuccess, showConfirm, showError } from '@/utils'

const userStore = useUserStore()
const childStore = useChildStore()

const currentCategory = ref('all')
const showAddDialog = ref(false)
const summary = ref('')
const memories = ref&lt;UserMemory[]&gt;([])

const categories = [
  { key: 'all', label: '全部', icon: '📋' },
  { key: 'preference', label: '喜好', icon: '❤️' },
  { key: 'skill', label: '能力', icon: '✨' },
  { key: 'routine', label: '习惯', icon: '📅' },
  { key: 'health', label: '健康', icon: '🏥' },
  { key: 'other', label: '其他', icon: '📝' }
]

const categoryOptions = categories.filter(c =&gt; c.key !== 'all').map(c =&gt; c.label)

const newMemory = ref({
  categoryIndex: 0,
  key: '',
  value: '',
  importance: 3
})

const filteredMemories = computed(() =&gt; {
  if (currentCategory.value === 'all') return memories.value
  return memories.value.filter(m =&gt; m.category === currentCategory.value)
})

const formatDate = (dateStr: string) =&gt; {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const onCategoryChange = (e: any) =&gt; {
  newMemory.value.categoryIndex = e.detail.value
}

const loadData = async () =&gt; {
  if (!userStore.currentChildId) return

  try {
    const [memoriesRes, summaryRes] = await Promise.all([
      getUserMemories(userStore.currentChildId),
      getConversationSummary(userStore.currentChildId)
    ])

    if (memoriesRes.code === 200) {
      memories.value = memoriesRes.data || []
    }
    if (summaryRes.code === 200 &amp;&amp; summaryRes.data) {
      summary.value = summaryRes.data.summary
    }
  } catch (error) {
    // 加载失败时使用模拟数据
    memories.value = [
      {
        id: 1,
        childId: userStore.currentChildId,
        key: '喜欢的故事主题',
        value: '喜欢小兔子和公主的故事，对魔法元素特别感兴趣',
        category: 'preference',
        importance: 5,
        createdAt: new Date().toISOString()
      },
      {
        id: 2,
        childId: userStore.currentChildId,
        key: '擅长的活动',
        value: '拼图能力很强，可以独立完成24片拼图',
        category: 'skill',
        importance: 4,
        createdAt: new Date(Date.now() - 86400000).toISOString()
      }
    ]
    summary.value = '孩子喜欢小兔子和公主的故事，对颜色和数字很感兴趣，最近在学习分享和情绪管理。'
  }
}

const addMemory = async () =&gt; {
  if (!newMemory.value.key.trim() || !newMemory.value.value.trim()) {
    showError('请填写完整信息')
    return
  }

  const categoryKeys = categories.filter(c =&gt; c.key !== 'all').map(c =&gt; c.key)
  const category = categoryKeys[newMemory.value.categoryIndex]

  try {
    await createUserMemory({
      childId: userStore.currentChildId!,
      key: newMemory.value.key,
      value: newMemory.value.value,
      category,
      importance: newMemory.value.importance
    })

    showSuccess('添加成功')
    showAddDialog.value = false
    newMemory.value = { categoryIndex: 0, key: '', value: '', importance: 3 }
    loadData()
  } catch (error) {
    showError('添加失败')
  }
}

const deleteMemory = async (id: number) =&gt; {
  const confirm = await showConfirm('提示', '确定要删除这条记忆吗？')
  if (!confirm) return

  try {
    await deleteMemoryApi(id)
    showSuccess('删除成功')
    loadData()
  } catch (error) {
    showError('删除失败')
  }
}

onMounted(() =&gt; {
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }
  childStore.fetchChildren()
  loadData()
})
&lt;/script&gt;

&lt;style lang="scss" scoped&gt;
.memories-page {
  min-height: 100vh;
  background: #F8F8F8;
  padding: 32rpx;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.page-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #1F2937;
}

.add-btn {
  padding: 12rpx 24rpx;
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
  border-radius: 40rpx;
  font-size: 26rpx;
  border: none;
}

.summary-card {
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
}

.summary-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #92400E;
  display: block;
  margin-bottom: 16rpx;
}

.summary-content {
  font-size: 26rpx;
  color: #78350F;
  line-height: 1.8;
}

.summary-empty {
  font-size: 26rpx;
  color: #B45309;
}

.category-tabs {
  display: flex;
  gap: 12rpx;
  margin-bottom: 24rpx;
  overflow-x: auto;
}

.category-tab {
  background: #fff;
  padding: 16rpx 24rpx;
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  gap: 8rpx;
  flex-shrink: 0;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.category-tab.active {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
}

.tab-icon {
  font-size: 24rpx;
}

.tab-label {
  font-size: 24rpx;
  color: #1F2937;
}

.category-tab.active .tab-label {
  color: #fff;
}

.memories-list {
  margin-bottom: 32rpx;
}

.memory-item {
  background: #fff;
  border-radius: 20rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.memory-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.memory-key {
  font-size: 28rpx;
  font-weight: bold;
  color: #1F2937;
}

.memory-meta {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.star {
  font-size: 20rpx;
}

.memory-time {
  font-size: 22rpx;
  color: #9CA3AF;
}

.memory-value {
  font-size: 26rpx;
  color: #4B5563;
  line-height: 1.6;
  display: block;
  margin-bottom: 16rpx;
}

.memory-actions {
  display: flex;
  justify-content: flex-end;
}

.delete-btn {
  font-size: 24rpx;
  color: #EF4444;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  display: block;
  margin-bottom: 24rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #9CA3AF;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-content {
  background: #fff;
  border-radius: 24rpx;
  padding: 40rpx;
  width: 600rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.dialog-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 32rpx;
  text-align: center;
}

.form-item {
  margin-bottom: 24rpx;
}

.form-label {
  font-size: 26rpx;
  color: #4B5563;
  display: block;
  margin-bottom: 12rpx;
}

.form-input, .form-textarea, .picker {
  width: 100%;
  background: #F3F4F6;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.picker {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.arrow {
  color: #9CA3AF;
  font-size: 32rpx;
}

.form-textarea {
  min-height: 120rpx;
}

.importance-selector {
  display: flex;
  gap: 16rpx;
  margin-top: 12rpx;
}

.star-btn {
  font-size: 40rpx;
  padding: 8rpx;
}

.star-btn.active {
  transform: scale(1.2);
}

.dialog-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 32rpx;
}

.btn {
  flex: 1;
  padding: 24rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.btn-cancel {
  background: #F3F4F6;
  color: #4B5563;
}

.btn-primary {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
}

.bottom-safe {
  height: 120rpx;
}
&lt;/style&gt;
