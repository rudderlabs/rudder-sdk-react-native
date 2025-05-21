import { Configuration } from './NativeBridge';
import {
  DATA_PLANE_URL,
  CONTROL_PLANE_URL,
  FLUSH_QUEUE_SIZE,
  DB_COUNT_THRESHOLD,
  SLEEP_TIMEOUT,
  CONFIG_REFRESH_INTERVAL,
  AUTO_COLLECT_ADVERT_ID,
  TRACK_LIFECYCLE_EVENTS,
  RECORD_SCREEN_VIEWS,
  LOG_LEVEL,
  AUTO_SESSION_TRACKING,
  SESSION_TIMEOUT,
  ENABLE_BACKGROUND_MODE,
  COLLECT_DEVICE_ID,
  ENABLE_GZIP,
} from './Constants';
import DBEncryptionPlugin from '@rudderstack/rudder-plugin-db-encryption-react-native';

export const configure = async (
  writeKey: string,
  {
    dataPlaneUrl = DATA_PLANE_URL,
    controlPlaneUrl = CONTROL_PLANE_URL,
    flushQueueSize = FLUSH_QUEUE_SIZE,
    dbCountThreshold = DB_COUNT_THRESHOLD,
    sleepTimeOut = SLEEP_TIMEOUT,
    logLevel = LOG_LEVEL,
    autoSessionTracking = AUTO_SESSION_TRACKING,
    sessionTimeout = SESSION_TIMEOUT,
    enableBackgroundMode = ENABLE_BACKGROUND_MODE,
    configRefreshInterval = CONFIG_REFRESH_INTERVAL,
    autoCollectAdvertId = AUTO_COLLECT_ADVERT_ID,
    trackAppLifecycleEvents = TRACK_LIFECYCLE_EVENTS,
    recordScreenViews = RECORD_SCREEN_VIEWS,
    collectDeviceId = COLLECT_DEVICE_ID,
    enableGzip = ENABLE_GZIP,
    dbEncryption,
    withFactories = [],
  }: Configuration,
): Promise<Configuration> => {
  // setup device mode integrations
  const integrations = withFactories;
  if (integrations && integrations.length > 0) {
    // ask about await
    await Promise.all(
      integrations.map(async (integration) =>
        typeof integration === 'function' ? integration() : null,
      ),
    );
  }

  if (dbEncryption !== undefined) {
    const dbEncryptionPluginInstance = new DBEncryptionPlugin(
      dbEncryption.key,
      dbEncryption.enable,
    );
    dbEncryptionPluginInstance.addDBEncryptionPlugin();
  }

  const config = {
    writeKey,
    dataPlaneUrl,
    controlPlaneUrl,
    flushQueueSize,
    dbCountThreshold,
    sleepTimeOut,
    logLevel,
    autoSessionTracking,
    sessionTimeout,
    configRefreshInterval,
    autoCollectAdvertId,
    trackAppLifecycleEvents,
    enableBackgroundMode,
    recordScreenViews,
    collectDeviceId,
    enableGzip,
  };

  return config;
};
