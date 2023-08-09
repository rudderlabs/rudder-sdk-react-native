import React from 'react';
import { Button } from 'react-native';
import rudderClient, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';

const RudderEvents = () => {
  const identify = async () => {
    const options = {
      externalIds: [
        {
          id: '2d31d085-4d93-4126-b2b3-94e651810673',
          type: 'brazeExternalId',
        },
      ],
    };

    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj',
    };

    await rudderClient.identify(
      'test_userIdiOS',
      {
        email: 'testuseriOS@example.com',
        location: 'UK',
      },
      options,
    );
  };

  const customTrack = () => {
    rudderClient.track('Custom Track Event', {
      property1: 'value1',
      property2: 'value2',
    });
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
    rudderClient.reset();
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
    </>
  );
};

export default RudderEvents;
