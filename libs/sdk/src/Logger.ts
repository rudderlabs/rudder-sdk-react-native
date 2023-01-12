const VERBOSE = 5;
const DEBUG = 4;
const INFO = 3;
const WARN = 2;
const ERROR = 1;
const NONE = 0;

let _logLevel = ERROR;
export const logInit = (logLevel: number) => {
  _logLevel = logLevel;
};

export const logVerbose = (message: string) => {
  if (_logLevel >= VERBOSE) {
    console.log(`RudderSDK: Verbose: ${message}`);
  }
};

export const logDebug = (message: string) => {
  if (_logLevel >= DEBUG) {
    console.log(`RudderSDK: Debug: ${message}`);
  }
};

export const logInfo = (message: string) => {
  if (_logLevel >= INFO) {
    console.log(`RudderSDK: Info: ${message}`);
  }
};

export const logWarn = (message: string) => {
  if (_logLevel >= WARN) {
    console.log(`RudderSDK: Warn: ${message}`);
  }
};

export const logError = (message: string) => {
  if (_logLevel >= ERROR) {
    console.log(`RudderSDK: Error: ${message}`);
  }
};
export const RUDDER_LOG_LEVEL = {
  NONE,
  ERROR,
  WARN,
  INFO,
  DEBUG,
  VERBOSE,
};
