/**
 * 表单校验工具
 */

/**
 * 校验手机号
 */
export const validatePhone = (phone: string): boolean => {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 校验验证码
 */
export const validateCode = (code: string): boolean => {
  return /^\d{4,6}$/.test(code)
}

/**
 * 校验姓名
 */
export const validateName = (name: string): boolean => {
  return name.length >= 1 && name.length <= 20
}

/**
 * 校验日期格式
 */
export const validateDate = (date: string): boolean => {
  return /^\d{4}-\d{2}-\d{2}$/.test(date)
}

/**
 * 脱敏手机号
 */
export const maskPhone = (phone: string): string => {
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}
