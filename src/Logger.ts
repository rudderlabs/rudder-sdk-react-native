export const RUDDER_LOG_LEVEL_VERBOSE = 5;
export const RUDDER_LOG_LEVEL_DEBUG = 4;
export const RUDDER_LOG_LEVEL_INFO = 3;
export const RUDDER_LOG_LEVEL_WARN = 2;
export const RUDDER_LOG_LEVEL_ERROR = 1;
export const RUDDER_LOG_LEVEL_NONE = 0;

let _logLevel = 2;
export const logInit = (logLevel: number) => {
  _logLevel = logLevel;
}

export const logVerbose = (message: String) => {
  if (_logLevel >= RUDDER_LOG_LEVEL_VERBOSE) {
    console.log(`RudderSDK: Verbose: ${message}`);
  }
}

export const logDebug = (message: String) => {
  if (_logLevel >= RUDDER_LOG_LEVEL_DEBUG) {
    console.log(`RudderSDK: Debug: ${message}`);
  }
}

export const logInfo = (message: String) => {
  if (_logLevel >= RUDDER_LOG_LEVEL_INFO) {
    console.log(`RudderSDK: Info: ${message}`);
  }
}

export const logWarn = (message: String) => {
    if (_logLevel >= RUDDER_LOG_LEVEL_WARN) {
        console.log(`RudderSDK: Warn: ${message}`)
    }
}

export const logError = (message: String) => {
    if (_logLevel >= RUDDER_LOG_LEVEL_ERROR) {
        console.log(`RudderSDK: Error: ${message}`)
    }
}