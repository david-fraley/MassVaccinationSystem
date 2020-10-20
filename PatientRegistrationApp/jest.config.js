module.exports = {
  preset: '@vue/cli-plugin-unit-jest',
  transformIgnorePatterns: ['/node_modules/(?!lib-to-transform|other-lib)'],
  testMatch: ["**/__tests__/*.{j,t}s?(x)",
    "**/tests/unit/**/*.test.{j,t}s?(x)"]
}
