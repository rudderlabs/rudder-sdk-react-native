import { Platform } from 'react-native';
import AsyncLock from 'async-lock';

import { configure } from './RudderConfiguration';
import bridge, { Configuration } from './NativeBridge';
import { logInit, logDebug, logError, logWarn } from './Logger';
import { SDK_VERSION } from './Constants';
import IRudderContext from './IRudderContext';

const lock = new AsyncLock();

function validateConfiguration(configuration: Configuration) {
  if (configuration.controlPlaneUrl && typeof configuration.controlPlaneUrl != 'string') {
    logWarn("setup : 'controlPlaneUrl' must be a string. Falling back to the default value");
    delete configuration.controlPlaneUrl;
  }
  if (configuration.flushQueueSize && !Number.isInteger(configuration.flushQueueSize)) {
    logWarn("setup : 'flushQueueSize' must be an integer. Falling back to the default value");
    delete configuration.flushQueueSize;
  }
  if (configuration.dbCountThreshold && !Number.isInteger(configuration.dbCountThreshold)) {
    logWarn("setup : 'dbCountThreshold' must be an integer. Falling back to the default value");
    delete configuration.dbCountThreshold;
  }
  if (configuration.sleepTimeOut && !Number.isInteger(configuration.sleepTimeOut)) {
    logWarn("setup : 'sleepTimeOut' must be an integer. Falling back to the default value");
    delete configuration.sleepTimeOut;
  }
  if (configuration.logLevel && !Number.isInteger(configuration.logLevel)) {
    logWarn(
      "setup : 'logLevel' must be an integer. Use RUDDER_LOG_LEVEL to set this value.Falling back to the default value",
    );
    delete configuration.logLevel;
  }
  if (
    configuration.configRefreshInterval &&
    !Number.isInteger(configuration.configRefreshInterval)
  ) {
    logWarn(
      "setup : 'configRefreshInterval' must be an integer.  Falling back to the default value",
    );
    delete configuration.configRefreshInterval;
  }
  if (
    configuration.trackAppLifecycleEvents &&
    typeof configuration.trackAppLifecycleEvents != 'boolean'
  ) {
    logWarn(
      "setup : 'trackAppLifecycleEvents' must be a boolen. Falling back to the default value",
    );
    delete configuration.trackAppLifecycleEvents;
  }
  if (configuration.recordScreenViews && typeof configuration.recordScreenViews != 'boolean') {
    logWarn("setup : 'recordScreenViews' must be a boolen. Falling back to the default value");
    delete configuration.recordScreenViews;
  }
  if (configuration.autoCollectAdvertId && typeof configuration.autoCollectAdvertId != 'boolean') {
    logWarn("setup : 'autoCollectAdvertId' must be a boolen. Falling back to the default value");
    delete configuration.autoCollectAdvertId;
  }
  if (configuration.autoSessionTracking && typeof configuration.autoSessionTracking != 'boolean') {
    logWarn("setup : 'autoSessionTracking' must be a boolen. Falling back to the default value");
    delete configuration.autoSessionTracking;
  }
  if (configuration.sessionTimeout && !Number.isInteger(configuration.sessionTimeout)) {
    logWarn("setup : 'sessionTimeout' must be an integer. Falling back to the default value");
    delete configuration.sessionTimeout;
  }
  if (
    configuration.enableBackgroundMode &&
    typeof configuration.enableBackgroundMode != 'boolean'
  ) {
    logWarn("setup : 'enableBackgroundMode' must be a boolen. Falling back to the default value");
    delete configuration.enableBackgroundMode;
  }
  if (configuration.collectDeviceId && typeof configuration.collectDeviceId != 'boolean') {
    logWarn("setup : 'collectDeviceId' must be a boolean. Falling back to the default value");
    delete configuration.collectDeviceId;
  }
}

// setup the RudderSDK with writeKey and Config
async function setup(
  writeKey: string,
  configuration: Configuration = {},
  options: Record<string, unknown> | null = null,
) {
  if (writeKey == undefined || typeof writeKey != 'string' || writeKey == '') {
    logError('setup: writeKey is incorrect. Aborting');
    return;
  }
  if (
    !configuration.dataPlaneUrl ||
    typeof configuration.dataPlaneUrl != 'string' ||
    configuration.dataPlaneUrl! == ''
  ) {
    logError('setup: dataPlaneUrl is incorrect. Aborting');
    return;
  }
  // init log level
  if (configuration.logLevel && Number.isInteger(configuration.logLevel)) {
    logInit(configuration.logLevel);
  }

  logDebug(`Initializing Rudder RN SDK version: ${SDK_VERSION}`);
  validateConfiguration(configuration);

  // Acquire a lock before calling the setup of Native Modules
  await lock.acquire('lock', async function (done) {
    const config = await configure(writeKey, configuration);
    logDebug('setup: created config');
    await bridge.setup(config, options);
    logDebug('setup: setup completed');
    done();
  });
}

// wrapper for `track` method
async function track(
  event: string,
  properties: Record<string, unknown> | null = null,
  options: Record<string, unknown> | null = null,
) {
  if (event == undefined) {
    logWarn("track: Mandatory field 'event' missing");
    return;
  }
  if (typeof event != 'string') {
    logWarn("track: 'event' must be a string");
    return;
  }
  bridge.track(event, properties, options);
}

// wrapper for `screen` method
async function screen(
  name: string,
  properties: Record<string, unknown> | null = null,
  options: Record<string, unknown> | null = null,
) {
  if (name == undefined) {
    logWarn("screen: Mandatory field 'name' missing");
    return;
  }
  if (typeof name != 'string') {
    logWarn("screen: 'name' must be a string");
    return;
  }
  bridge.screen(name, properties, options);
}

// wrapper for `identify` method
async function identify(
  userId: string,
  traits: Record<string, unknown>,
  options: Record<string, unknown>,
): Promise<void>;
async function identify(
  traits: Record<string, unknown>,
  options: Record<string, unknown>,
): Promise<void>;
async function identify(
  userIdOrTraits: string | Record<string, unknown>,
  traitsOrOptions: Record<string, unknown> | null = null,
  options: Record<string, unknown> | null = null,
) {
  if (userIdOrTraits == undefined) {
    logWarn('identify: atleast one of userId or traits is required');
    return;
  }

  let _userId;
  let _traits;
  let _options;
  if (typeof userIdOrTraits == 'string') {
    // userIdOrTraits contains userId
    _userId = userIdOrTraits;
    _traits = traitsOrOptions;
    _options = options;
  } else if (typeof userIdOrTraits == 'object') {
    // userIdOrTraits contains traits
    _userId = '';
    _traits = userIdOrTraits;
    _options = traitsOrOptions;
  } else {
    logWarn('identify : Unsupported argument type passed to identify');
    return;
  }

  bridge.identify(_userId, _traits, _options);
}

// wrapper for `group` method
async function group(
  groupId: string,
  traits: Record<string, unknown> | null = null,
  options: Record<string, unknown> | null = null,
) {
  if (groupId == undefined) {
    logWarn("group: Mandatory field 'groupId' missing");
    return;
  }
  if (typeof groupId != 'string') {
    logWarn("group: 'groupId' must be a string");
    return;
  }
  bridge.group(groupId, traits, options);
}

// wrapper for `alias` method
async function alias(previousId: string, userId: string | Record<string, unknown>): Promise<void>;
async function alias(newId: string, options: Record<string, unknown> | null | string = null) {
  if (newId == undefined) {
    logWarn("alias: Mandatory field 'newId' missing");
    return;
  }
  if (typeof newId != 'string') {
    logWarn("alias: 'newId' must be a string");
    return;
  }
  if (typeof options == 'string') {
    bridge.alias(options, null);
  } else if (typeof options == 'object') {
    bridge.alias(newId, options);
  } else {
    bridge.alias(newId, null);
  }
}

async function putDeviceToken(token: string): Promise<void>;
/**
 * @deprecated use putDeviceToken{@link putDeviceToken(token: string)} instead
 */
async function putDeviceToken(androidToken: string, iOSToken: string): Promise<void>;
async function putDeviceToken(token: string, iOSToken: string | null = null): Promise<void> {
  if (Platform.OS == 'ios' && iOSToken) {
    bridge.putDeviceToken(iOSToken);
  } else if (token) {
    bridge.putDeviceToken(token);
  }
}

/**
 * @deprecated use putAdvertisingId{@link putAdvertisingId(advertisingId: string)} instead
 */
async function setAdvertisingId(androidId: string, iOSId: string) {
  switch (Platform.OS) {
    case 'ios':
      if (iOSId) {
        putAdvertisingId(iOSId);
      }
      break;
    case 'android':
      if (androidId) {
        putAdvertisingId(androidId);
      }
      break;
  }
}

async function putAdvertisingId(advertisingId: string) {
  if (advertisingId) {
    bridge.putAdvertisingId(advertisingId);
  }
}

/**
 * @deprecated use putAnonymousId{@link putAnonymousId(anonymousId: string)} instead
 */
async function setAnonymousId(anonymousId: string) {
  if (anonymousId) {
    putAnonymousId(anonymousId);
  }
}

async function putAnonymousId(anonymousId: string) {
  if (anonymousId) {
    bridge.putAnonymousId(anonymousId);
  }
}

async function reset(clearAnonymousId = false) {
  bridge.reset(clearAnonymousId);
}

async function flush() {
  bridge.flush();
}

async function optOut(optOut: boolean) {
  bridge.optOut(optOut);
}

// eslint-disable-next-line @typescript-eslint/ban-types
async function registerCallback(name: string, callback: Function) {
  if (name) {
    bridge.registerCallback(name, callback);
  }
}

async function getRudderContext(): Promise<IRudderContext | null> {
  const context: IRudderContext | null = await bridge.getRudderContext();
  return context ?? null;
}

async function startSession(sessionId?: number): Promise<void> {
  if (sessionId === undefined) {
    bridge.startSession('');
  } else if (!Number.isInteger(sessionId)) {
    logWarn("startSession: 'sessionId' must be an integer");
  } else {
    if (sessionId.toString().length < 10) {
      logWarn("startSession: 'sessionId' length should be at least 10, hence ignoring it");
      return;
    }
    bridge.startSession(sessionId.toString());
  }
}

async function endSession() {
  bridge.endSession();
}

async function getSessionId(): Promise<number | null> {
  try {
    const sessionId: number | null = await bridge.getSessionId();
    if (sessionId === null || sessionId === undefined) {
      return null;
    }
    return Number(sessionId);
  } catch (e) {
    logError('getSessionId: Failed to get sessionId: ' + e);
    return null;
  }
}

const rudderClient = {
  setup,
  track,
  screen,
  identify,
  group,
  alias,
  reset,
  flush,
  optOut,
  putDeviceToken,
  putAdvertisingId,
  setAdvertisingId,
  putAnonymousId,
  setAnonymousId,
  registerCallback,
  getRudderContext,
  startSession,
  endSession,
  getSessionId,
};
export default rudderClient;
