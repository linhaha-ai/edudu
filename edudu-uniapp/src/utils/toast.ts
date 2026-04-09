/**
 * Toast 提示封装
 */

/**
 * 显示成功提示
 */
export const showSuccess = (title: string = '操作成功') => {
  uni.showToast({
    title,
    icon: 'success',
    duration: 2000,
  })
}

/**
 * 显示失败提示
 */
export const showError = (title: string = '操作失败') => {
  uni.showToast({
    title,
    icon: 'none',
    duration: 2000,
  })
}

/**
 * 显示加载中
 */
export const showLoading = (title: string = '加载中...') => {
  uni.showLoading({
    title,
    mask: true,
  })
}

/**
 * 隐藏加载
 */
export const hideLoading = () => {
  uni.hideLoading()
}

/**
 * 显示确认对话框
 */
export const showConfirm = (title: string, content: string): Promise<boolean> => {
  return new Promise((resolve) => {
    uni.showModal({
      title,
      content,
      success: (res) => {
        resolve(res.confirm)
      },
    })
  })
}
