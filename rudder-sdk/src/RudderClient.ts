import { configure } from "./RudderConfiguaration";
import bridge from "./NativeBridge";

export interface Configuration {
  endPointUrl?: string;
  flushQueueSize?: number;
  dbCountThreshold?: number;
  sleepTimeOut?: number;
  logLevel?: number;
  configRefreshInterval?: number;
  trackAppLifecycleEvents?: boolean;
  recordScreenViews?: boolean;
}

export class RudderClient {
  public readonly ready = false;

  public async setup(writeKey: string, configuration: Configuration = {}) {
    await bridge.setup(await configure(writeKey, configuration));
  }

  public async track(
    event: string,
    properties: Object = {},
    userProperties: Object = {},
    options: Object = {}
  ) {
    bridge.track(event, properties, userProperties, options);
  }

  public async screen(
    name: string,
    properties: Object = {},
    userProperties: Object = {},
    options: Object = {}
  ) {
    bridge.screen(name, properties, userProperties, options);
  }

  public async identify(
    userId: string,
    traits: Object = {},
    options: Object = {}
  ) {
    bridge.identify(userId, traits, options);
  }

  public async reset() {
    bridge.reset();
  }

  public async getAnonymousId(): Promise<string> {
    return bridge.getAnonymousId();
  }
}
