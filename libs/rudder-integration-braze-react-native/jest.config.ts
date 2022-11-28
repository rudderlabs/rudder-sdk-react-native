const libName = 'rudder-integration-braze-react-native';

module.exports = {
  displayName: libName,
  preset: 'react-native',
  resolver: '@nrwl/jest/plugins/resolver',
  moduleFileExtensions: ['ts', 'js', 'html', 'tsx', 'jsx'],
  setupFilesAfterEnv: ['<rootDir>/test-setup.ts'],
  moduleNameMapper: {
    '.svg': '@nrwl/react-native/plugins/jest/svg-mock',
    '.png': '@nrwl/react-native/plugins/jest/svg-mock',
  },
  collectCoverageFrom: [
    'src/**/*.{ts,tsx}',
    '!**/*.test.{ts,tsx}',
    '!**/test/*.{ts,tsx}',
  ],
  coverageReporters: ['json', 'text', ['lcov', { projectRoot: '/' }], 'clover'],
  coverageThreshold: {
    global: {
      branches: 0,
      functions: 0,
      lines: 0,
      statements: 0,
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
};
