import { configure } from "./RudderConfiguaration";
import bridge, { Configuration } from "./NativeBridge";
import { logDebug, logError } from "./Logger";

// setup the RudderSDK with writeKey and Config
async function setup(writeKey: string, configuration: Configuration = {}) {
  if (writeKey == undefined || typeof writeKey != "string" || writeKey == '') {
    logError("setup: writeKey is incorrect. Aborting");
    return;
  }
  if (
    !configuration.dataPlaneUrl ||
    typeof configuration.dataPlaneUrl != "string" ||
    configuration.dataPlaneUrl! == ''
  ) {
    logError("setup: dataPlaneUrl is incorrect. Aborting");
    return;
  }
  const config = await configure(writeKey, configuration);
  logDebug("setup: created config")
  await bridge.setup(config);
  logDebug("setup: setup completed")
}

// wrapper for `track` method
async function track(
  event: string,
  properties: Object = null,
  options: Object = null
) {
  if (event == undefined) {
    log("track: Mandatory field 'event' missing");
    return;
  }
  if (typeof event != "string") {
    log("track: 'event' must be a string");
    return;
  }
  bridge.track(event, properties, options);
}

// wrapper for `screen` method
async function screen(
  name: string,
  properties: Object = null,
  options: Object = null
) {
  if (name == undefined) {
    log("screen: Mandatory field 'name' missing");
    return;
  }
  if (typeof name != "string") {
    log("screen: 'name' must be a string");
    return;
  }
  bridge.screen(name, properties, options);
}

// wrapper for `identify` method 
async function identify(userId: string, traits: Object, options: Object): Promise<void>;
async function identify(traits: Object, options: Object): Promise<void>;
async function identify(
  userIdOrTraits: any,
  traitsOrOptions: Object = null,
  options: Object = null
) {
  if (userIdOrTraits == undefined) {
    log("identify: atleast one of userId or traits is required");
    return;
  }

  let _userId;
  let _traits;
  let _options;
  if (typeof userIdOrTraits == "string") {
    // userIdOrTraits contains userId
    _userId = userIdOrTraits;
    _traits = traitsOrOptions;
    _options = options;
  } else if (typeof userIdOrTraits == "object") {
    // userIdOrTraits contains traits
    _userId = "";
    _traits = userIdOrTraits;
    _options = traitsOrOptions;
  } else {
    log("identify : Unsupported argument type passed to identify");
    return;
  }

  bridge.identify(_userId, _traits, options);
}

async function group(groupId: string, traits: Object = null) {
  if (groupId == undefined) {
    log("group: Mandatory field \'groupId\' missing");
    return;
  }
  if (typeof groupId != "string") {
    log("group: \'groupId\' must be a string");
    return;
  }
  bridge.group(groupId, traits);
}

async function alias(previousId: string, userId: string) {
  if (previousId == undefined) {
    log("alias: Mandatory field \'previousId\' missing");
    return;
  }
  if (typeof previousId != "string") {
    log("alias: \'previousID\' must be a string");
    return;
  }
  bridge.alias(previousId, userId);
}
  
async function reset() {
  bridge.reset();
}

const rudderClient = { setup , track, screen, identify, group, alias, reset };
export default rudderClient;