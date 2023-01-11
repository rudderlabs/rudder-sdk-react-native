import * as React from 'react';
import { useEffect } from 'react';
import { Button, Text } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import rc, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';
import amplitude from '@rudderstack/rudder-integration-amplitude-react-native';
import appcenter from '@rudderstack/rudder-integration-appcenter-react-native';
import braze from '@rudderstack/rudder-integration-braze-react-native';
import clevertap from '@rudderstack/rudder-integration-clevertap-react-native';
import firebase from '@rudderstack/rudder-integration-firebase-react-native';
import moengage from '@rudderstack/rudder-integration-moengage-react-native';
import singular from '@rudderstack/rudder-integration-singular-react-native';
import appsflyer, {
  setOneLinkCustomDomains,
  setOptions,
} from '@rudderstack/rudder-integration-appsflyer-react-native';
// @ts-ignore
import { TEST_DATAPLANE_URL, TEST_WRITE_KEY, APPSFLYER_DEV_KEY, APPSFLYER_APPLE_ID } from '@env';

const Stack = createNativeStackNavigator();

const initialization = async () => {
  // TODO: get all secret details in .env file
  setOptions({
    devKey: APPSFLYER_DEV_KEY,
    isDebug: true,
    onInstallConversionDataListener: true,
    appleAppId: APPSFLYER_APPLE_ID,
  });

  const config = {
    dataPlaneUrl: TEST_DATAPLANE_URL,
    trackAppLifecycleEvents: true,
    autoCollectAdvertId: true,
    recordScreenViews: true,
    logLevel: RUDDER_LOG_LEVEL.VERBOSE,
    withFactories: [
      appsflyer,
      amplitude,
      appcenter,
      braze,
      clevertap,
      firebase,
      moengage,
      singular,
    ],
  };

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

  await rc.setup(TEST_WRITE_KEY, config);

  await rc.identify(
    'test_userIdiOS',
    {
      email: 'testuseriOS@example.com',
      location: 'UK',
    },
    options,
  );
  await rc.track('React Native event', props);
  await rc.screen('React Native screen', props);

  setOneLinkCustomDomains(
    'desu.rudderstack.com',
    () => {
      console.log('Successfully set');
    },
    () => {
      console.log('Failed to set');
    },
  );
};

const App = () => {
  const routeNameRef = React.useRef();
  const navigationRef = React.useRef();

  useEffect(() => {
    const awaitInitialization = async () => {
      await initialization();
    };

    awaitInitialization().catch(console.error);
  }, []);

  return (
    <NavigationContainer
      ref={navigationRef}
      onReady={() => {
        routeNameRef.current = (navigationRef.current as any).getCurrentRoute().name;
      }}
      onStateChange={async () => {
        const previousRouteName = routeNameRef.current;
        const currentRouteName = (navigationRef.current as any).getCurrentRoute().name;

        if (previousRouteName !== currentRouteName) {
          await rc.screen(currentRouteName, {
            screen_name: currentRouteName,
            screen_class: currentRouteName,
          });
        }
        routeNameRef.current = currentRouteName;
      }}
    >
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} options={{ title: 'Welcome' }} />
        <Stack.Screen name="Profile" component={ProfileScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

const HomeScreen = ({ navigation }) => {
  useEffect(() => {
    const awaitTrack = async () => {
      const props = {
        k1: 'v1',
        k2: 'v3',
        k3: 'v3',
        name: 'Miraj',
      };

      await rc.track('React Native Home Screen Event', props);
      await rc.screen('React Native Home screen', props);
    };

    awaitTrack().catch(console.error);
  }, []);

  return (
    <>
      <Text>Dataplane: {TEST_DATAPLANE_URL}</Text>
      <Text>Write key: {TEST_WRITE_KEY}</Text>
      <Button
        title="Go to Jane's profile"
        onPress={() => navigation.navigate('Profile', { name: 'Jane' })}
      />
      <Button
        testID="init_btn"
        title="Initialization"
        onPress={async () => await initialization()}
      />
    </>
  );
};

const ProfileScreen = ({ navigation, route }) => {
  useEffect(() => {
    const awaitTrack = async () => {
      const props = {
        k1: 'v1',
        k2: 'v3',
        k3: 'v3',
        name: 'Miraj',
      };

      await rc.track('React Native Profile Screen Event', props);
      await rc.screen('React Native Profile screen', props);
    };

    awaitTrack().catch(console.error);
  }, []);

  return <Text>This is {route.params.name}'s profile</Text>;
};

export default App;
