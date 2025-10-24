const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8080,  // 开发环境前端端口
    proxy: {
      '/api': {
        target: 'http://localhost:8081',  // 开发环境后端端口
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  }
})
