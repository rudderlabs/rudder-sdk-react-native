import { configure } from "./RudderConfiguaration";
import bridge, { Configuration } from "./NativeBridge";

const log = (error) => {
  console.log(`RudderSDK : Error : ${error}`);
};

export class RudderClient {
  public readonly ready = false;

  public async setup(writeKey: string, configuration: Configuration = {}) {
    if (writeKey == undefined || typeof writeKey != "string") {
      // verify log
      log("setup: \'writeKey\' either missing or of a non string type");
      return;
    }
    if (!configuration.dataPlaneUrl || typeof configuration.dataPlaneUrl != "string") {
      log("setup: \'dataPlaneUrl\' either missing in configuration or of a non string type");
      return;
    }
    await bridge.setup(await configure(writeKey, configuration));
  }

  public async track(
    event: string,
    properties: Object = null,
    userProperties: Object = null,
    options: Object = null
  ) {
    if (event == undefined) {
      log("track: Mandatory field \'event\' missing");
      return;
    }
    if (typeof event != "string") {
      log("track: \'event\' must be a string");
      return;
    }
    bridge.track(event, properties, userProperties, options);
  }

  public async screen(
    name: string,
    properties: Object = null,
    userProperties: Object = null,
    options: Object = null
  ) {
    if (name == undefined) {
      log("screen: Mandatory field \'name\' missing");
      return;
    }
    if (typeof name != "string") {
      log("screen: \'name\' must be a string");
      return;
    }
    bridge.screen(name, properties, userProperties, options);
  }
  
  identify(userId: string, traits: Object, options: Object): Promise<void>;
  identify(traits: Object, options: Object): Promise<void>;
  public async identify(
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
    } else if (typeof userIdOrTraits == "object"){
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

  public async reset() {
    bridge.reset();
  }

  public async getAnonymousId(): Promise<string> {
    return bridge.getAnonymousId();
  }
}
