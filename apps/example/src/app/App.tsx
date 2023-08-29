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
  onAppOpenAttribution,
  onAttributionFailure,
  onDeepLink,
  onInstallConversionData,
  onInstallConversionFailure,
  setOneLinkCustomDomains,
  setOptions,
} from '@rudderstack/rudder-integration-appsflyer-react-native';
// @ts-ignore
import { TEST_DATAPLANE_URL, TEST_WRITE_KEY, APPSFLYER_DEV_KEY, APPSFLYER_APPLE_ID } from '@env';
import RudderEvents from './RudderEvents';

const Stack = createNativeStackNavigator();

const initRNAppsFlyerSDK = async () => {
  if (APPSFLYER_APPLE_ID) {
    setOptions({
      devKey: APPSFLYER_DEV_KEY,
      isDebug: true,
      onInstallConversionDataListener: true,
      appleAppId: APPSFLYER_APPLE_ID,
      timeToWaitForATTUserAuthorization: 60,
    });

    onAppOpenAttribution((data) => {
      console.log('On App Open Attribution Success and the data is ', data);
    });

    onAttributionFailure((data) => {
      console.log('On App Attribution Failure and the data is ', data);
    });

    onInstallConversionData((data) => {
      console.log('On Install conversion Success data is ', data);
    });

    onInstallConversionFailure((data) => {
      console.log('On Install conversion Failure data is ', data);
    });

    onDeepLink((data) => {
      console.log('On Deeplink data is ', data);
    });

    setOneLinkCustomDomains(
      'desu.rudderstack.com',
      () => {
        console.log('Successfully set');
      },
      () => {
        console.log('Failed to set');
      },
    );
  }
};

const initRudderReactNativeSDK = async () => {
  const config = {
    dataPlaneUrl: TEST_DATAPLANE_URL,
    autoCollectAdvertId: true,
    collectDeviceId: false,
    recordScreenViews: false,
    logLevel: RUDDER_LOG_LEVEL.VERBOSE,
    sessionTimeout: 0,
    enableBackgroundMode: true,
    trackAppLifecycleEvents: true,
    autoSessionTracking: true,
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

  await rc.setup(TEST_WRITE_KEY, config);
};

const initialization = async () => {
  await initRNAppsFlyerSDK();
  await initRudderReactNativeSDK();
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
      <Text>
        Write key: {TEST_WRITE_KEY} {'\n'}
      </Text>
      <Button
        testID="init_btn"
        title="Initialization"
        onPress={async () => await initialization()}
      />
      <RudderEvents />
      <Button
        title="Go to Jane's profile"
        onPress={() => navigation.navigate('Profile', { name: 'Jane' })}
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
