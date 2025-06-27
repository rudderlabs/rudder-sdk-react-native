import { StatusBar } from 'expo-status-bar';
import React, { useEffect } from 'react';
import { StyleSheet, Text, View, Button, Platform } from 'react-native';
import { WRITE_KEY, DATA_PLANE_URL } from '@env';

import rudderClient, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';

import DBEncryption from '@rudderstack/rudder-plugin-db-encryption-react-native';

import amplitude from '@rudderstack/rudder-integration-amplitude-react-native';
import braze from '@rudderstack/rudder-integration-braze-react-native';

import appcenter, {
  enableAnalytics,
  disableAnalytics,
} from '@rudderstack/rudder-integration-appcenter-react-native';
import appsflyer, {
  setOptions,
  getAppsFlyerId,
} from '@rudderstack/rudder-integration-appsflyer-react-native';
import clevertap from '@rudderstack/rudder-integration-clevertap-react-native';
import moengage from '@rudderstack/rudder-integration-moengage-react-native';
import facebook from '@rudderstack/rudder-integration-facebook-react-native';
import firebase from '@rudderstack/rudder-integration-firebase-react-native';
import singular from '@rudderstack/rudder-integration-singular-react-native';

const identifyWithExternalId = async () => {
  const options = {
    externalIds: [
      {
        id: '2d31d085-4d93-4126-b2b3-94e651810673',
        type: 'brazeExternalId',
      },
    ],
  };

  rudderClient.identify(
    'test_userId',
    {
      email: 'testuser@example.com',
      location: 'UK',
    },
    options,
  );
};

const identify = async () => {
  const traits = {
    'key-1': 'value-1',
    'key-2': 34,
  };
  rudderClient.identify('userId', traits);
  console.log('Identify Event is called.');
};

const getLocalOptions = () => {
  return {
    integrations: {
      // specifying destination by its display name
      Amplitude: true,
      Mixpanel: false,
    },
    // custom contexts
    account: {
      level: 'standard',
      membership: 'silver',
    },
  };
};

async function track() {
  rudderClient.track('Custom track Event - 1', null, getLocalOptions());
  // Below method is not tested yet
  // orderCompleted();
  console.log('Track Event is called.');
}

async function orderCompleted() {
  rudderClient.track('Order Completed', {
    checkout_id: '12345',
    order_id: '1234',
    affiliation: 'Apple Store',
    total: 20,
    revenue: 15.0,
    shipping: 4,
    tax: 1,
    discount: 1.5,
    coupon: 'ImagePro',
    currency: 'USD',
    products: [
      {
        product_id: '123',
        sku: 'G-32',
        name: 'Monopoly',
        price: 14,
        quantity: 1,
        category: 'Games',
        url: 'https://www.website.com/product/path',
        image_url: 'https://www.website.com/product/path.jpg',
      },
      {
        product_id: '345',
        sku: 'F-32',
        name: 'UNO',
        price: 3.45,
        quantity: 2,
        category: 'Games',
      },
    ],
  });
}

async function screen() {
  rudderClient.screen('Custom screen Event - 1');
  console.log('Screen Event is called.');
}

async function group() {
  rudderClient.group('Custom group Event - 1');
  console.log('Group Event is called.');
}

async function alias() {
  const options = {
    integrations: {
      // specifying destination by its display name
      Amplitude: false,
    },
    // custom contexts
    tierLocal: {
      category: 'premium',
      type: 'gold',
    },
  };

  rudderClient.alias('Custom alias UserId');
  // rudderClient.alias("New User ID", options);
  // rudderClient.alias('New User ID', 'Old User ID');
  // rudderClient.alias("New User ID", "Explicit Previous UserId", options);

  // Some other combinations:
  // rudderClient.alias("New User ID", null);
  // rudderClient.alias("New User ID", undefined);
  // rudderClient.alias("New User ID", null, null);
  // rudderClient.alias("New User ID", null, undefined);
  // rudderClient.alias("New User ID", null, options);
  // rudderClient.alias("New User ID", undefined, options);
  // rudderClient.alias("New User ID", "Expliicit User ID", null);

  console.log('Alias Event is called.');
}

async function reset() {
  rudderClient.reset();
  console.log('RESET Event is called.');
}

async function flush() {
  rudderClient.flush();
  console.log('FLUSH Event is called.');
}

async function allCalls() {
  identify();
  track();
  screen();
  group();
  alias();
}

async function manualSession() {
  rudderClient.startSession();
  console.log('Manual session is called.');
}

async function manualSessionWithId() {
  rudderClient.startSession(1234567890);
  console.log('Manual session with ID is called.');
}

async function endSession() {
  rudderClient.endSession();
  console.log('End session.');
}

const getSessionId = async () => {
  const sessionId = await rudderClient.getSessionId();
  console.log(`Session id: ${sessionId}`);
  console.log(`SessionId type: ${typeof sessionId}`);
};

const enableOptOut = () => {
  rudderClient.optOut(true);
};

const disableOptOut = () => {
  rudderClient.optOut(false);
};

const getRudderContext = async () => {
  const context = await rudderClient.getRudderContext();
  console.log(`${JSON.stringify(context)}`);
};

const putAdvertisingId = async () => {
  switch (Platform.OS) {
    case 'ios':
      await rudderClient.putAdvertisingId('iOS-ADVERTISING-ID');
      console.log('Setting iOS Advertising ID');
      break;
    case 'android':
      await rudderClient.putAdvertisingId('ANDROID-ADVERTISING-ID');
      console.log('Setting Android Advertising ID');
      break;
  }
};

const clearAdvertisingId = async () => {
  await rudderClient.clearAdvertisingId();
  console.log('Cleared Advertising ID');
};

const fetchAppsFlyerId = async () => {
  const appsFlyerId = await getAppsFlyerId();
  console.log(`Fetched AppsFlyer ID: ${appsFlyerId}`);
};

const enableAppCenterAnalytics = async () => {
  await enableAnalytics();
};

const disableAppCenterAnalytics = async () => {
  await disableAnalytics();
};

const appsFlyerInitialization = async () => {
  // TODO: get all secret details in .env file
  setOptions({
    devKey: 'APPSFLYER_DEV_KEY',
    isDebug: true,
  });
};

const getGlobalOptions = () => {
  return {
    integrations: {
      // specifying destination by its display name
      Mixpanel: false,
    },
    // custom contexts
    tier: {
      category: 'premium',
      type: 'gold',
    },
  };
};

function RudderStackAPIs() {
  console.log(WRITE_KEY);
  console.log(DATA_PLANE_URL);

  useEffect(() => {
    // appsFlyerInitialization();

    const dbEncryption = new DBEncryption('versys', false);

    const rudderInitialise = async () => {
      await rudderClient.setup(
        WRITE_KEY,
        {
          dataPlaneUrl: DATA_PLANE_URL,
          logLevel: RUDDER_LOG_LEVEL.VERBOSE,
          recordScreenViews: false,
          enableBackgroundMode: true,
          trackAppLifecycleEvents: true,
          autoSessionTracking: true,
          dbEncryption: dbEncryption,
          enableGzip: true,
          withFactories: [
            amplitude,
            appcenter,
            appsflyer,
            braze,
            clevertap,
            facebook,
            // This requires additional setup in iOS and Android i.e., adding GoogleService-Info.plist and google-services.json files respectively
            // firebase,
            moengage,
            singular,
          ],
        },
        // getGlobalOptions()
      );
      console.log('SDK is initalised');
    };
    rudderInitialise().catch(console.error);
  }, []);

  return (
    <View style={styles.container}>
      <StatusBar style="auto" />

      <Button title="Identify" color="#841584" onPress={() => identify()} />
      <Button title="Track" color="#841584" onPress={() => track()} />
      <Button title="Screen" color="#841584" onPress={() => screen()} />
      <Button title="Group" color="#841584" onPress={() => group()} />
      <Button title="Alias" color="#841584" onPress={() => alias()} />
      <Button title="All calls" color="#841584" onPress={() => allCalls()} />
      <Button title="Manual Session" color="#841584" onPress={() => manualSession()} />
      <Button
        title="Manual Session with ID"
        color="#841584"
        onPress={() => manualSessionWithId()}
      />
      <Button title="End session" color="#841584" onPress={() => endSession()} />

      <Button
        title="Identify With ExternalID"
        color="#841584"
        onPress={() => identifyWithExternalId()}
      />
      <Button title="RESET" color="#841584" onPress={() => reset()} />
      <Button title="FLUSH" color="#841584" onPress={() => flush()} />
      <Button title="getSessionId()" onPress={getSessionId} />
      <Button title="Enable OptOut" color="#841584" onPress={enableOptOut} />
      <Button title="Disable OptOut" color="#841584" onPress={disableOptOut} />
      <Button title="getRudderContext" color="#841584" onPress={getRudderContext} />
      <Button title="putAdvertisingId()" onPress={putAdvertisingId} />
      <Button title="clearAdvertisingId()" onPress={clearAdvertisingId} />
      <Button title="fetch AppsFlyerId()" onPress={fetchAppsFlyerId} />
      <Button title="enable AppCenter Analytics()" onPress={enableAppCenterAnalytics} />
      <Button title="disable AppCenter Analytics()" onPress={disableAppCenterAnalytics} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default RudderStackAPIs;
