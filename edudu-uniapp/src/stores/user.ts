/**
 * 用户 Store
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getUserInfo, loginBySms, sendSmsCode, loginByWechat, logout, register as registerApi } from '@/api/user'
import type { UserInfo, LoginParams, SmsParams, RegisterParams } from '@/api/user/types'

export const useUserStore = defineStore(
  'user',
  () => {
    // State
    const token = ref<string>('')
    const userInfo = ref<UserInfo | null>(null)
    const currentChildId = ref<number | null>(null)
    const isLoading = ref(false)

    // Getters
    const isLoggedIn = computed(() => !!token.value)
    const nickname = computed(() => userInfo.value?.nickname || '用户')
    const avatar = computed(() => userInfo.value?.avatar || '')

    // Actions
    const setToken = (newToken: string) => {
      token.value = newToken
    }

    const setUserInfo = (info: UserInfo) => {
      userInfo.value = info
    }

    const setCurrentChildId = (id: number | null) => {
      currentChildId.value = id
    }

    /**
     * 检查登录状态
     */
    const checkLogin = async () => {
      if (token.value) {
        try {
          await fetchUserInfo()
        } catch {
          // token 失效，清除登录状态
          clearLogin()
        }
      }
    }

    /**
     * 获取用户信息
     */
    const fetchUserInfo = async () => {
      if (!token.value) return
      isLoading.value = true
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          userInfo.value = res.data
          // 设置当前孩子
          if (res.data.children?.length && !currentChildId.value) {
            currentChildId.value = res.data.children[0].id
          }
        }
      } finally {
        isLoading.value = false
      }
    }

    /**
     * 短信登录
     */
    const loginWithSms = async (params: LoginParams) => {
      isLoading.value = true
      try {
        const res = await loginBySms(params)
        if (res.code === 200) {
          setToken(res.data.token)
          await fetchUserInfo()
          return true
        }
        return false
      } finally {
        isLoading.value = false
      }
    }

    /**
     * 发送验证码
     */
    const sendSms = async (params: SmsParams) => {
      const res = await sendSmsCode(params)
      return res.code === 200
    }

    /**
     * 微信登录
     */
    const loginWithWechat = async (code: string) => {
      isLoading.value = true
      try {
        const res = await loginByWechat({ code })
        if (res.code === 200) {
          setToken(res.data.token)
          await fetchUserInfo()
          return true
        }
        return false
      } finally {
        isLoading.value = false
      }
    }

    /**
     * 用户注册
     */
    const register = async (params: RegisterParams) => {
      isLoading.value = true
      try {
        const res = await registerApi(params)
        if (res.code === 200) {
          setToken(res.data.token)
          await fetchUserInfo()
          return true
        }
        return false
      } finally {
        isLoading.value = false
      }
    }

    /**
     * 退出登录
     */
    const logoutAction = async () => {
      try {
        await logout()
      } finally {
        clearLogin()
      }
    }

    /**
     * 清除登录状态
     */
    const clearLogin = () => {
      token.value = ''
      userInfo.value = null
      currentChildId.value = null
    }

    return {
      // State
      token,
      userInfo,
      currentChildId,
      isLoading,
      // Getters
      isLoggedIn,
      nickname,
      avatar,
      // Actions
      setToken,
      setUserInfo,
      setCurrentChildId,
      checkLogin,
      fetchUserInfo,
      loginWithSms,
      sendSms,
      loginWithWechat,
      register,
      logoutAction,
      clearLogin,
    }
  },
  {
    persist: {
      key: 'edudu-user',
      paths: ['token', 'userInfo', 'currentChildId'],
    },
  }
)
