import { Configuration } from "./NativeBridge";
import {
  DATA_PLANE_URL,
  CONTROL_PLANE_URL,
  FLUSH_QUEUE_SIZE,
  DB_COUNT_THRESHOLD,
  SLEEP_TIMEOUT,
  CONFIG_REFRESH_INTERVAL,
  TRACK_LIFECYCLE_EVENTS,
  RECORD_SCREEN_VIEWS,
  LOG_LEVEL
} from './Constants'
import { logInit } from "./Logger";

export const configure = async (
  writeKey: string,
  {
    dataPlaneUrl = DATA_PLANE_URL,
    controlPlaneUrl = CONTROL_PLANE_URL,
    flushQueueSize = FLUSH_QUEUE_SIZE,
    dbCountThreshold = DB_COUNT_THRESHOLD,
    sleepTimeOut = SLEEP_TIMEOUT,
    logLevel = LOG_LEVEL,
    configRefreshInterval = CONFIG_REFRESH_INTERVAL,
    trackAppLifecycleEvents = TRACK_LIFECYCLE_EVENTS,
    recordScreenViews = RECORD_SCREEN_VIEWS
  }: Configuration
): Promise<Configuration> => {
  // init log level
  logInit(logLevel);

  const config = {
    writeKey,
    dataPlaneUrl,
    controlPlaneUrl,
    flushQueueSize,
    dbCountThreshold,
    sleepTimeOut,
    logLevel,
    configRefreshInterval,
    trackAppLifecycleEvents,
    recordScreenViews
  };

  return { ...config };
};
