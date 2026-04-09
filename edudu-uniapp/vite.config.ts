import { defineConfig, loadEnv } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
import { resolve } from 'path'

export default defineConfig(({ command, mode }) => {
  const env = loadEnv(mode, process.cwd())

  return {
    plugins: [uni()],
    base: '/',
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src')
      }
    },
    server: {
      port: 8080,
      host: '0.0.0.0',
      open: false,
      proxy: {
        '/api': {
          target: 'http://localhost:8081',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      }
    },
    build: {
      sourcemap: mode !== 'production'
    }
  }
})
