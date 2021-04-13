process.env.VUE_APP_VERSION = require('./package.json').version

module.exports = {
  transpileDependencies: ["vuetify"],
  publicPath: "./",
  devServer: {
    proxy: {
      '^/broker': {
        target: 'http://localhost:3000/broker',
      }
    }
  }
};