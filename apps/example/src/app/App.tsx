/* eslint-disable jsx-a11y/accessible-emoji */
import React, { useRef, useState, useEffect } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  TouchableOpacity,
} from 'react-native';
import Svg, { Path } from 'react-native-svg';

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
import DBEncryption from '@rudderstack/rudder-plugin-db-encryption-react-native';
// @ts-ignore
import { TEST_DATAPLANE_URL, TEST_WRITE_KEY, APPSFLYER_DEV_KEY, APPSFLYER_APPLE_ID } from '@env';
import RudderEvents from './RudderEvents';

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

const getGlobalOptions = () => {
  return {
    integrations: {
      // specifying destination by its display name
      Mixpanel: false,
    },
    // custom contexts:
    tier: {
      category: 'premium',
      type: 'gold',
    },
    account: {
      level: 'standard',
      membership: 'silver',
    },
  };
};

const initRudderReactNativeSDK = async () => {
  const dbEncryption = new DBEncryption('versys', false);
  const options = getGlobalOptions();

  const config = {
    dataPlaneUrl: TEST_DATAPLANE_URL,
    autoCollectAdvertId: true,
    collectDeviceId: false,
    recordScreenViews: false,
    logLevel: RUDDER_LOG_LEVEL.VERBOSE,
    enableBackgroundMode: true,
    trackAppLifecycleEvents: true,
    autoSessionTracking: true,
    dbEncryption: dbEncryption,
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

  await rc.setup(TEST_WRITE_KEY, config, options);
};

const initialization = async () => {
  await initRNAppsFlyerSDK();
  await initRudderReactNativeSDK();
};

export const App = () => {
  const [whatsNextYCoord, setWhatsNextYCoord] = useState<number>(0);
  const scrollViewRef = useRef<null | ScrollView>(null);

  useEffect(() => {
    const awaitInitialization = async () => {
      await initialization();
    };

    awaitInitialization().catch(console.error);
  }, []);

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          ref={(ref) => {
            scrollViewRef.current = ref;
          }}
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}
        >
          <View style={styles.section}>
            <Text style={styles.textLg}>Hello there,</Text>
            <Text style={[styles.textXL, styles.appTitleText]} testID="heading">
              Welcome 👋
            </Text>
          </View>
          <RudderEvents />
          <View style={styles.section}>
            <View style={styles.hero}>
              <View style={styles.heroTitle}>
                <Svg
                  width={32}
                  height={32}
                  stroke="hsla(162, 47%, 50%, 1)"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <Path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"
                  />
                </Svg>
                <Text style={[styles.textLg, styles.heroTitleText]}>You're up and running</Text>
              </View>
              <TouchableOpacity
                style={styles.whatsNextButton}
                onPress={() => {
                  scrollViewRef.current?.scrollTo({
                    x: 0,
                    y: whatsNextYCoord,
                  });
                }}
              >
                <Text style={[styles.textMd, styles.textCenter]}>What's next?</Text>
              </TouchableOpacity>
            </View>
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};
const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: '#ffffff',
  },
  textCenter: {
    textAlign: 'center',
  },
  textMd: {
    fontSize: 18,
  },
  textLg: {
    fontSize: 24,
  },
  textXL: {
    fontSize: 48,
  },
  section: {
    marginVertical: 24,
    marginHorizontal: 12,
  },
  appTitleText: {
    paddingTop: 12,
    fontWeight: '500',
  },
  hero: {
    borderRadius: 12,
    backgroundColor: '#143055',
    padding: 36,
    marginBottom: 24,
  },
  heroTitle: {
    flex: 1,
    flexDirection: 'row',
  },
  heroTitleText: {
    color: '#ffffff',
    marginLeft: 12,
  },
  whatsNextButton: {
    backgroundColor: '#ffffff',
    paddingVertical: 16,
    borderRadius: 8,
    width: '50%',
    marginTop: 24,
  },
});

export default App;
