import { Configuration } from "./NativeBridge";

export const configure = async (
  writeKey: string,
  {
    endPointUrl = "https://api.rudderlabs.com",
    flushQueueSize = 30,
    dbCountThreshold = 10000,
    sleepTimeOut = 10,
    logLevel = 2,
    configRefreshInterval = 2,
    trackAppLifecycleEvents = true,
    recordScreenViews = false
  }: Configuration
): Promise<Configuration> => {
  const config = {
    endPointUrl,
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
