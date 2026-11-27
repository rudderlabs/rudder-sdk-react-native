import React, { useEffect, useState } from 'react';
import { Text, View } from 'react-native';
import rudderClient from '@rudderstack/rudder-sdk-react-native';
import amplitude from '@rudderstack/rudder-integration-amplitude-react-native';
import appcenter from '@rudderstack/rudder-integration-appcenter-react-native';
import appsflyer from '@rudderstack/rudder-integration-appsflyer-react-native';
import braze from '@rudderstack/rudder-integration-braze-react-native';
import clevertap from '@rudderstack/rudder-integration-clevertap-react-native';
import facebook from '@rudderstack/rudder-integration-facebook-react-native';
import firebase from '@rudderstack/rudder-integration-firebase-react-native';
import moengage from '@rudderstack/rudder-integration-moengage-react-native';
import singular from '@rudderstack/rudder-integration-singular-react-native';
import sprig from '@rudderstack/rudder-integration-sprig-react-native';

export default function App() {
  const [status, setStatus] = useState('initializing...');
  useEffect(() => {
    rudderClient
      .setup('TEST_WRITE_KEY', {
        dataPlaneUrl: 'https://hosted.rudderlabs.com',
        withFactories: [
          amplitude,
          appcenter,
          appsflyer,
          braze,
          clevertap,
          facebook,
          firebase,
          moengage,
          singular,
          sprig,
        ],
      })
      .then(() => setStatus('Rudder init OK'))
      .catch((e: Error) => setStatus('Rudder init FAILED: ' + e.message));
  }, []);
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text style={{ fontSize: 28 }}>{status}</Text>
    </View>
  );
}
