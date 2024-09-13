// const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const CompressionPlugin = require("compression-webpack-plugin")
module.exports = {
  // pluginOptions: {
  //   // 配置favicon.ico
  //   pwa: {
  //     iconPaths: {
  //       favicon32: './favicon.ico',
  //       favicon16: './favicon.ico',
  //       appleTouchIcon: './favicon.ico',
  //       maskIcon: './favicon.ico',
  //       msTileImage: './favicon.ico'
  //     }
  //   },
  // },
  configureWebpack: config => {
    if (process.env.NODE_ENV === 'production') { // 去掉console.log
      config.optimization.minimizer[0].options.terserOptions.compress.warnings = false
      config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
      config.optimization.minimizer[0].options.terserOptions.compress.drop_debugger = true
      config.optimization.minimizer[0].options.terserOptions.compress.pure_funcs = ['console.log']
    }

    if (process.env.NODE_ENV === 'production') {
      return {
        plugins: [
          new CompressionPlugin({
            test: /\.js$|\.html$|\.css/, //匹配文件名
            threshold: 10240,//对超过10k的数据压缩
            deleteOriginalAssets: false //不删除源文件
          })
        ]
      }
    }
  },
  chainWebpack: config => { // 打包分析

    if (process.env.use_analyzer) {
      config
        .plugin('webpack-bundle-analyzer')
        .use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin)
    }

    // 移除 prefetch 插件
    config.plugins.delete("prefetch");
    // 移除 preload 插件
    config.plugins.delete('preload');
    // 压缩代码
    config.optimization.minimize(true);
    // 分割代码
    config.optimization.splitChunks({
      chunks: 'all'
    })

    // 通过 chainWebpack 调整图片的大小限制，我们将图片大小限制设置为 6kb，低于6kb的图片全部被内联，高于6kb的图片会放在单独的img文件夹中。
    const imagesRule = config.module.rule("images")
    imagesRule
      .use('url-loader')
      .loader('url-loader')
      .tap(options => Object.assign(options, { limit: 6144 }))
  },

  // 更换覆盖主题
  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true,
      }
    }
  },

  // chainWebpack: config => {
  //   // ie报错无效字符 添加该配置项 解决该问题
  //   config.module
  //     .rule('iview')
  //     .test(/iview.src.*?js$/)
  //     .use('babel')
  //     .loader('babel-loader')
  //     .end()
  // },

  // 根域上下文目录
  publicPath: '/', // publicPath:'/rm', 这里可以设置二级文件夹作为主页面

  // build时构建文件的目录 构建时传入 --no-clean 可关闭该行为
  // outputDir: 'build',

  // build时放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录
  // assetsDir: 'assets',
  productionSourceMap: process.env.NODE_ENV === 'production' ? false : true,

  // 指定生成的 index.html 的输出路径 (相对于 outputDir)。也可以是一个绝对路径。
  // indexPath: 'index.html',

  devServer: {
    port: 8081,
    // sockHost: 'http://127.0.0.1',
    // disableHostCheck: true,
    // open: true,
    // host: "localhost",
    proxy: {
      '/piflow-web': {
        target: 'http://10.0.82.194:6001',
        // target: 'http://localhost:6002/piflow-web/admin',
        // target: 'http://127.0.0.1:6001',
        changeOrigin: true,
        ws: true, // 是否启用websockets
        secure: false, // 使用的是http协议则设置为false，https协议则设置为true
        // pathRewrite: {
        //   '^/piflow-web': '/'
        // }
      }
    },
  },
  lintOnSave: false,   // 取消 eslint 验证

}