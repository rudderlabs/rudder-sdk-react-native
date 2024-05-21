import React from 'react';
import { Button, Platform } from 'react-native';
import rudderClient, { IRudderContext } from '@rudderstack/rudder-sdk-react-native';
import {
  enableAnalytics,
  disableAnalytics,
} from '@rudderstack/rudder-integration-appcenter-react-native';
import { getAppsFlyerId } from '@rudderstack/rudder-integration-appsflyer-react-native';

const getLocalOptions = () => {
  return {
    externalIds: [
      {
        id: '2d31d085-4d93-4126-b2b3-94e651810673',
        type: 'brazeExternalId',
      },
    ],
    integrations: {
      // specifying destination by its display name
      Amplitude: true,
      Mixpanel: false,
    },
    account: {
      level: 'standard',
      membership: 'silver',
    },
  };
};

const RudderEvents = () => {
  const identify = async () => {
    await rudderClient.identify(
      'test_userIdiOS',
      {
        email: 'testuseriOS@example.com',
        location: 'UK',
      },
      getLocalOptions(),
    );
  };

  const customTrack = () => {
    rudderClient.track(
      'Custom Track Event',
      {
        property1: 'value1',
        property2: 'value2',
      },
      getLocalOptions(),
    );
  };

  const orderCompleted = () => {
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
  };

  const screen = () => {
    rudderClient.screen('Home Screen', {
      property1: 'value1',
      property2: 'value2',
    });
  };

  const group = () => {
    rudderClient.group('Custom group Event - 1');
  };

  const alias = () => {
    rudderClient.alias('RN Alias ID', 'previous RN userId');
  };

  const startSession = () => {
    rudderClient.startSession();
  };

  const startSessionWithID = () => {
    rudderClient.startSession(1234567890123);
  };

  const endSession = () => {
    rudderClient.endSession();
  };

  const reset = () => {
    rudderClient.reset(false);
  };

  const getSessionId = async () => {
    const sessionId: number | null = await rudderClient.getSessionId();
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
    const context: IRudderContext | null = await rudderClient.getRudderContext();
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

  const enableAppCenterAnalytics = async () => {
    await enableAnalytics();
  };

  const disableAppCenterAnalytics = async () => {
    await disableAnalytics();
  };

  const appsFlyerId = async () => {
    const appsFlyerId = await getAppsFlyerId();
    console.log(`AppsFlyer ID: ${appsFlyerId}`);
  };

  return (
    <>
      <Button title="Identify" onPress={identify} />
      <Button title="Custom Track" onPress={customTrack} />
      <Button title="track Order Completed" onPress={orderCompleted} />
      <Button title="Screen" onPress={screen} />
      <Button title="Group" onPress={group} />
      <Button title="Alias" onPress={alias} />
      <Button title="startSession()" onPress={startSession} />
      <Button title="startSessionWithID()" onPress={startSessionWithID} />
      <Button title="endSession()" onPress={endSession} />
      <Button title="RESET" onPress={reset} />
      <Button title="getSessionId()" onPress={getSessionId} />
      <Button title="enableOptOut()" onPress={enableOptOut} />
      <Button title="disableOptOut()" onPress={disableOptOut} />
      <Button title="getRudderContext()" onPress={getRudderContext} />
      <Button title="putAdvertisingId()" onPress={putAdvertisingId} />
      <Button title="clearAdvertisingId()" onPress={clearAdvertisingId} />
      <Button title="enable AppCenter Analytics()" onPress={enableAppCenterAnalytics} />
      <Button title="disable AppCenter Analytics()" onPress={disableAppCenterAnalytics} />
      <Button title="getAppsFlyerId()" onPress={appsFlyerId} />
    </>
  );
};

export default RudderEvents;
