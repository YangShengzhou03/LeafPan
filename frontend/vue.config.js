const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  // 关键：设置为根路径，确保打包后引用路径以 / 开头
  publicPath: '/',
  // 生产构建优化配置
  productionSourceMap: false,  // 关闭 SourceMap，减少 80% 内存占用
  css: {
    extract: true,  // 提取 CSS 到单独文件
    sourceMap: false  // 关闭 CSS SourceMap
  },
  configureWebpack: {
    optimization: {
      minimize: true,  // 保持压缩，但优化配置
      splitChunks: {
        chunks: 'all',
        cacheGroups: {
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name: 'vendors',
            chunks: 'all'
          }
        }
      }
    }
  },
  // 并行处理优化
  parallel: require('os').cpus().length > 1,
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
