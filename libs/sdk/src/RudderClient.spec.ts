import { Platform } from 'react-native';
import rudderClient from './RudderClient';
import bridge from './NativeRudderBridge';
import { DATA_PLANE_URL } from './Constants';
import { RUDDER_LOG_LEVEL } from './Logger';

jest.mock('./NativeRudderBridge', () => ({
  __esModule: true,
  default: {
    setup: jest.fn().mockResolvedValue(undefined),
    track: jest.fn(),
    screen: jest.fn(),
    identify: jest.fn(),
    alias: jest.fn(),
    group: jest.fn(),
    reset: jest.fn(),
    flush: jest.fn(),
    optOut: jest.fn(),
    putDeviceToken: jest.fn(),
    putAdvertisingId: jest.fn(),
    clearAdvertisingId: jest.fn(),
    putAnonymousId: jest.fn(),
    registerCallback: jest.fn(),
    getRudderContext: jest.fn(),
    startSession: jest.fn(),
    endSession: jest.fn(),
    getSessionId: jest.fn(),
  },
}));

const mockedBridge = bridge as jest.Mocked<typeof bridge>;

describe('rudderClient setup configuration validation', () => {
  let consoleLogSpy: jest.SpyInstance;

  beforeEach(() => {
    jest.clearAllMocks();
    consoleLogSpy = jest.spyOn(console, 'log').mockImplementation(() => undefined);
  });

  afterEach(() => {
    consoleLogSpy.mockRestore();
  });

  it('preserves explicit false for trackDeepLinks', async () => {
    await rudderClient.setup('write-key', {
      dataPlaneUrl: DATA_PLANE_URL,
      trackDeepLinks: false,
    });

    expect(mockedBridge.setup).toHaveBeenCalledWith(
      expect.objectContaining({ trackDeepLinks: false }),
      null,
    );
  });

  it('removes invalid trackDeepLinks values before configuration defaults are applied', async () => {
    await rudderClient.setup('write-key', {
      dataPlaneUrl: DATA_PLANE_URL,
      logLevel: RUDDER_LOG_LEVEL.WARN,
      trackDeepLinks: 'false' as unknown as boolean,
    });

    expect(consoleLogSpy).toHaveBeenCalledWith(
      "RudderSDK: Warn: setup : 'trackDeepLinks' must be a boolean. Falling back to the default value true",
    );
    expect(mockedBridge.setup).toHaveBeenCalledWith(
      expect.objectContaining({ trackDeepLinks: true }),
      null,
    );
  });
});

describe('rudderClient device token routing', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('uses the iOS token on iOS', async () => {
    jest.replaceProperty(Platform, 'OS', 'ios');

    await rudderClient.putDeviceToken('android-token', 'ios-token');

    expect(mockedBridge.putDeviceToken).toHaveBeenCalledWith('ios-token');
  });

  it('uses the Android token on Android', async () => {
    jest.replaceProperty(Platform, 'OS', 'android');

    await rudderClient.putDeviceToken('android-token', 'ios-token');

    expect(mockedBridge.putDeviceToken).toHaveBeenCalledWith('android-token');
  });
});
