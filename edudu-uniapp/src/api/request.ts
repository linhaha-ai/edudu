/**
 * 全局请求封装
 */
import { useUserStore } from '@/stores/user'

// API 基础地址
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api'

// 请求队列
let requestQueue: any[] = []
let isRefreshing = false

/**
 * 请求拦截器
 */
const requestInterceptor = (options: UniApp.RequestOptions): UniApp.RequestOptions => {
  const userStore = useUserStore()

  // 添加 token
  if (userStore.token) {
    options.header = {
      ...options.header,
      Authorization: `Bearer ${userStore.token}`,
    }
  }

  // 添加时间戳防止缓存
  if (options.method === 'GET') {
    options.url += (options.url.includes('?') ? '&' : '?') + `_t=${Date.now()}`
  }

  return options
}

/**
 * 响应拦截器
 */
const responseInterceptor = (
  response: UniApp.RequestSuccessCallbackResult
): UniApp.RequestSuccessCallbackResult | Promise<any> => {
  const data = response.data as any

  // 处理业务错误
  if (data.code !== 200) {
    // Token 过期
    if (data.code === 401) {
      return handleUnauthorized()
    }

    // 显示错误提示
    uni.showToast({
      title: data.message || '请求失败',
      icon: 'none',
      duration: 2000,
    })

    return Promise.reject(data)
  }

  return response
}

/**
 * 处理未授权
 */
const handleUnauthorized = async () => {
  if (isRefreshing) return

  isRefreshing = true
  const userStore = useUserStore()

  try {
    // 清除登录状态并跳转登录页
    userStore.clearLogin()
    uni.reLaunch({
      url: '/pages/user/login',
    })
  } finally {
    isRefreshing = false
  }
}

/**
 * 请求封装
 */
interface RequestOptions extends UniApp.RequestOptions {
  loading?: boolean
  loadingText?: string
}

export const request = <T = any>(options: RequestOptions): Promise<T> => {
  return new Promise((resolve, reject) => {
    // 显示 loading
    if (options.loading !== false) {
      uni.showLoading({
        title: options.loadingText || '加载中...',
        mask: true,
      })
    }

    // 应用请求拦截器
    const finalOptions = requestInterceptor(options)

    uni.request({
      url: BASE_URL + options.url,
      ...finalOptions,
      success: async (res) => {
        uni.hideLoading()

        try {
          const result = await responseInterceptor(res)
          resolve(result.data)
        } catch (err) {
          reject(err)
        }
      },
      fail: (err) => {
        uni.hideLoading()
        uni.showToast({
          title: '网络请求失败',
          icon: 'none',
        })
        reject(err)
      },
    })
  })
}

/**
 * GET 请求
 */
export const get = <T = any>(
  url: string,
  params?: Record<string, any>,
  options?: Partial<RequestOptions>
): Promise<T> => {
  let queryString = ''
  if (params) {
    queryString =
      '?' +
      Object.entries(params)
        .map(([key, value]) => `${key}=${encodeURIComponent(value as any)}`)
        .join('&')
  }
  return request<T>({
    url: url + queryString,
    method: 'GET',
    ...options,
  })
}

/**
 * POST 请求
 */
export const post = <T = any>(
  url: string,
  data?: any,
  options?: Partial<RequestOptions>
): Promise<T> => {
  return request<T>({
    url,
    method: 'POST',
    data,
    ...options,
  })
}

/**
 * PUT 请求
 */
export const put = <T = any>(
  url: string,
  data?: any,
  options?: Partial<RequestOptions>
): Promise<T> => {
  return request<T>({
    url,
    method: 'PUT',
    data,
    ...options,
  })
}

/**
 * DELETE 请求
 */
export const del = <T = any>(
  url: string,
  data?: any,
  options?: Partial<RequestOptions>
): Promise<T> => {
  return request<T>({
    url,
    method: 'DELETE',
    data,
    ...options,
  })
}

export default {
  request,
  get,
  post,
  put,
  del,
}
