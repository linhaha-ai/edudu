/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

/**
 * 环境变量类型定义
 */
interface ImportMetaEnv {
  /** API 基础地址 */
  readonly VITE_API_BASE_URL: string
  /** 应用名称 */
  readonly VITE_APP_TITLE: string
  /** 微信 AppID */
  readonly VITE_WX_APPID: string
  /** 文件上传地址 */
  readonly VITE_UPLOAD_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
