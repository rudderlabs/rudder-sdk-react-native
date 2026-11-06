const libName = 'sdk';

module.exports = {
  displayName: libName,
  preset: 'react-native',
  resolver: '@nx/jest/plugins/resolver',
  moduleFileExtensions: ['ts', 'js', 'html', 'tsx', 'jsx'],
  setupFilesAfterEnv: ['<rootDir>/test-setup.ts'],
  moduleNameMapper: {
    '.svg': '@nx/react-native/plugins/jest/svg-mock',
    '.png': '@nx/react-native/plugins/jest/svg-mock',
  },
  collectCoverageFrom: ['src/**/*.{ts,tsx}', '!**/*.test.{ts,tsx}', '!**/test/*.{ts,tsx}'],
  coverageReporters: ['json', 'text', ['lcov', { projectRoot: '/' }], 'clover'],
  coverageThreshold: {
    global: {
      branches: 53,
      functions: 55,
      lines: 55,
      statements: 55,
    },
  },
  reporters: [
    'default',
    [
      'jest-sonar',
      {
        outputDirectory: 'reports/sonar',
        outputName: 'results-report-' + libName + '.xml',
        reportedFilePath: 'relative',
        relativeRootDir: './',
      },
    ],
  ],
  cacheDirectory: '<rootDir>/node_modules/.cache/unit-tests',
};
