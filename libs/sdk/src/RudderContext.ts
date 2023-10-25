interface RudderContext {
  userAgent: string;
  screen: {
    width: number;
    height: number;
    density: number;
  };
  timezone: string;
  os: {
    version: string;
    name: string;
  };
  traits: Record<string, unknown>;
  network: {
    wifi: boolean;
    cellular: boolean;
    carrier: string;
    bluetooth: boolean;
  };
  locale: string;
  device: {
    id: string;
    type: string;
    name: string;
    manufacturer: string;
    model: string;
    advertisingId: string;
    adTrackingEnabled: boolean;
    token: string;
  };
  library: {
    version: string;
    name: string;
  };
  app: {
    namespace: string;
    version: string;
    name: string;
    build: string;
  };
  externalId: Array<Record<string, unknown>>;
  customContextMap: Record<string, unknown>;
}

export default RudderContext;
