const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        secure: false,
        changeOrigin: true,
        logLevel: 'debug'
      }
    }
  }
});

// preset: "@vue/cli-plugin-unit-jest",
// transformIgnorePatterns: ['/node_modules/']
// transformIgnorePatterns: ["node_modules/(?!axios)"],
