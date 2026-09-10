import { defineConfig, loadEnv } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
import { resolve } from 'path'

export default defineConfig(({ command, mode }) => {
  const env = loadEnv(mode, process.cwd())

  return {
    plugins: [uni()],
    base: '/',
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler',
          silenceDeprecations: ['import', 'legacy-js-api']
        }
      }
    },
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src')
      }
    },
    server: {
      port: 8888,
      host: '0.0.0.0',
      open: false,
      proxy: {
        '/auth': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        },
        '/ai': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        },
        '/story': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        },
        '/task': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        },
        '/child': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        },
        '/growth': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        },
        '/helper': {
          target: 'http://localhost:8081/api',
          changeOrigin: true
        }
      }
    },
    build: {
      sourcemap: mode !== 'production'
    }
  }
})
