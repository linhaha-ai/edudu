/**
 * Pinia Store - 孩子管理
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getChildrenList, addChild, updateChild, deleteChild } from '@/api/child'
import type { Child } from '@/api/child/types'

export const useChildStore = defineStore(
  'child',
  () => {
    // State
    const children = ref<Child[]>([])
    const isLoading = ref(false)

    // Getters
    const childCount = computed(() => children.value.length)

    // Actions
    /**
     * 获取孩子列表
     */
    const fetchChildren = async () => {
      isLoading.value = true
      try {
        const res = await getChildrenList()
        if (res.code === 200) {
          children.value = res.data || []
        }
      } finally {
        isLoading.value = false
      }
    }

    /**
     * 添加孩子
     */
    const addNewChild = async (params: Omit<Child, 'id'>) => {
      const res = await addChild(params)
      if (res.code === 200) {
        await fetchChildren()
        return res.data
      }
      return null
    }

    /**
     * 更新孩子信息
     */
    const updateChildInfo = async (id: number, params: Partial<Child>) => {
      const res = await updateChild(id, params)
      if (res.code === 200) {
        await fetchChildren()
        return true
      }
      return false
    }

    /**
     * 删除孩子
     */
    const deleteChildById = async (id: number) => {
      const res = await deleteChild(id)
      if (res.code === 200) {
        await fetchChildren()
        return true
      }
      return false
    }

    return {
      children,
      isLoading,
      childCount,
      fetchChildren,
      addNewChild,
      updateChildInfo,
      deleteChildById,
    }
  },
  {
    persist: {
      key: 'edudu-child',
      paths: ['children'],
    },
  }
)
