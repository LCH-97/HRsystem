const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    allowedHosts: "all"  // 모든 호스트를 허용합니다.
  }
});


// preset: "@vue/cli-plugin-unit-jest",
// transformIgnorePatterns: ['/node_modules/']
// transformIgnorePatterns: ["node_modules/(?!axios)"],
