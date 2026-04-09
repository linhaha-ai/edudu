/**
 * 语音播放工具
 */

let innerAudioContext: UniApp.InnerAudioContext | null = null

/**
 * 初始化语音播放器
 */
export const initAudio = () => {
  if (!innerAudioContext) {
    innerAudioContext = uni.createInnerAudioContext()
    innerAudioContext.onError((err) => {
      console.error('语音播放失败:', err)
      uni.showToast({
        title: '语音播放失败',
        icon: 'none',
      })
    })
  }
  return innerAudioContext
}

/**
 * 文字转语音播放
 * 使用浏览器原生 TTS 或后端语音合成
 */
export const speakText = async (text: string, voiceUrl?: string): Promise<void> => {
  initAudio()

  if (voiceUrl) {
    // 使用后端返回的语音 URL
    innerAudioContext!.src = voiceUrl
    innerAudioContext!.play()
  } else {
    // 使用 Web Speech API
    if (typeof uni !== 'undefined' && 'createSpeechRecognition' in uni) {
      // 微信小程序环境 - 使用后端语音合成
      uni.showToast({
        title: '该环境不支持语音播放',
        icon: 'none',
      })
      return
    }

    // H5 环境使用 Web Speech API
    const utterance = new SpeechSynthesisUtterance(text)
    utterance.lang = 'zh-CN'
    utterance.rate = 0.9
    utterance.pitch = 1.0
    speechSynthesis.speak(utterance)
  }
}

/**
 * 停止播放
 */
export const stopSpeak = () => {
  if (innerAudioContext) {
    innerAudioContext.stop()
  }
  if (typeof speechSynthesis !== 'undefined') {
    speechSynthesis.cancel()
  }
}

/**
 * 暂停播放
 */
export const pauseSpeak = () => {
  if (innerAudioContext) {
    innerAudioContext.pause()
  }
  if (typeof speechSynthesis !== 'undefined') {
    speechSynthesis.pause()
  }
}

/**
 * 继续播放
 */
export const resumeSpeak = () => {
  if (innerAudioContext) {
    innerAudioContext.play()
  }
  if (typeof speechSynthesis !== 'undefined') {
    speechSynthesis.resume()
  }
}

/**
 * 销毁播放器
 */
export const destroyAudio = () => {
  if (innerAudioContext) {
    innerAudioContext.destroy()
    innerAudioContext = null
  }
}
