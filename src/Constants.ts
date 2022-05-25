import { RUDDER_LOG_LEVEL } from "./Logger";

export const DATA_PLANE_URL = "https://hosted.rudderlabs.com";
export const CONTROL_PLANE_URL = "https://api.rudderlabs.com";
export const FLUSH_QUEUE_SIZE = 30;
export const DB_COUNT_THRESHOLD = 10000;
export const SLEEP_TIMEOUT = 10;
export const CONFIG_REFRESH_INTERVAL = 2;
export const AUTO_COLLECT_ADVERT_ID = false;
export const TRACK_LIFECYCLE_EVENTS = true;
export const RECORD_SCREEN_VIEWS = false;
export const LOG_LEVEL = RUDDER_LOG_LEVEL.ERROR;
export const SDK_VERSION = "1.4.1";