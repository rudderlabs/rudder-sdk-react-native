import rudderClient from '../src';
import bridge from '../src/NativeRudderBridge';
import { DATA_PLANE_URL } from '../src/Constants';

jest.mock('../src/NativeRudderBridge', () => ({
  __esModule: true,
  default: {
    setup: jest.fn().mockResolvedValue(undefined),
    track: jest.fn(),
    identify: jest.fn(),
    alias: jest.fn(),
    startSession: jest.fn(),
    registerCallback: jest.fn(),
  },
}));

const mockedBridge = jest.mocked(bridge);

describe('public SDK calls at the native bridge boundary', () => {
  beforeEach(() => jest.clearAllMocks());

  it('initializes integrations and encryption before native setup', async () => {
    const calls: string[] = [];
    const factory = async () => {
      await new Promise<void>((resolve) => setImmediate(resolve));
      calls.push('integration');
    };
    const addDBEncryptionPlugin = jest.fn(async () => {
      await new Promise<void>((resolve) => setImmediate(resolve));
      calls.push('encryption');
    });
    mockedBridge.setup.mockImplementationOnce(async () => {
      calls.push('setup');
    });

    await rudderClient.setup('key', {
      dataPlaneUrl: DATA_PLANE_URL,
      withFactories: [factory],
      dbEncryption: { key: 'test-key', enable: true, addDBEncryptionPlugin },
    });

    expect(calls).toEqual(['integration', 'encryption', 'setup']);
    expect(addDBEncryptionPlugin).toHaveBeenCalledWith('test-key', true);
  });

  it('waits for the first native setup to finish before starting another', async () => {
    let finishFirst!: () => void;
    const firstPending = new Promise<void>((resolve) => {
      finishFirst = resolve;
    });
    mockedBridge.setup.mockReturnValueOnce(firstPending);
    const config = { dataPlaneUrl: DATA_PLANE_URL };
    const first = rudderClient.setup('first', config);
    const second = rudderClient.setup('second', config);
    try {
      await new Promise<void>((resolve) => setImmediate(resolve));
      expect(mockedBridge.setup.mock.calls).toEqual([
        [expect.objectContaining({ writeKey: 'first' }), null],
      ]);
    } finally {
      finishFirst();
      await Promise.all([first, second]);
    }
    expect(mockedBridge.setup).toHaveBeenNthCalledWith(
      2,
      expect.objectContaining({ writeKey: 'second' }),
      null,
    );
  });

  it('removes nested NaN values without losing valid properties or changing the input', async () => {
    const properties = { items: [0, NaN, { price: NaN, active: false }], note: null };
    await rudderClient.track('Order', properties, { context: { invalid: NaN, count: 0 } });

    expect(mockedBridge.track).toHaveBeenCalledWith(
      'Order',
      { items: [0, { active: false }], note: null },
      { context: { count: 0 } },
    );
    expect(properties.items).toEqual([0, NaN, { price: NaN, active: false }]);
  });

  it('keeps traits and options in their respective positions for both identify forms', async () => {
    const traits = { plan: 'pro' };
    const options = { integrations: { All: false } };
    await rudderClient.identify(traits, options);
    await rudderClient.identify('user', traits, options);
    expect(mockedBridge.identify.mock.calls).toEqual([
      ['', traits, options],
      ['user', traits, options],
    ]);
  });

  it('distinguishes alias options from a previous user ID', async () => {
    const options = { integrations: { All: true } };
    await rudderClient.alias('new', options);
    await rudderClient.alias('new', 'old', options);
    expect(mockedBridge.alias.mock.calls).toEqual([
      ['new', null, options],
      ['new', 'old', options],
    ]);
  });

  it('passes a valid manual session ID as a string and ignores short IDs', async () => {
    await rudderClient.startSession(42);
    await rudderClient.startSession(1730000000);
    expect(mockedBridge.startSession.mock.calls).toEqual([['1730000000']]);
  });

  it('registers the integration callback under its requested name', async () => {
    const callback = jest.fn();
    await rudderClient.registerCallback('Braze', callback);
    expect(mockedBridge.registerCallback).toHaveBeenCalledWith('Braze', callback);
  });
});
