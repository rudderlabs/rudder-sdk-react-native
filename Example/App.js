/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import rc, {RUDDER_LOG_LEVEL} from '@rudderstack/rudder-sdk-react-native';
import appsflyer from 'rudder-integration-appsflyer-react-native';
import firebase from 'rudder-integration-firebase-react-native';
import appcenter from 'rudder-integration-appcenter-react-native';
import clevertap from 'rudder-integration-clevertap-react-native';

import AppsFlyerIntegrationFactory from 'rudder-integration-appsflyer-react-native/src/bridge';

const App: () => React$Node = () => {
  (async function () {
    const config = {
      dataPlaneUrl: 'https://45c73d68b08e.ngrok.io',
      trackAppLifecycleEvents: true,
      logLevel: RUDDER_LOG_LEVEL.VERBOSE,
      withFactories: [appsflyer]
    };
    const defaultOptions = {
      integrations: {
        "App Center": true
      }
    }
    await rc.registerCallback('App Center', () => {
      console.log("App Center is ready");
    })
    
    await rc.setup('1pAKRv50y15Ti6UWpYroGJaO0Dj', config, defaultOptions);

    const child_props = {
      c1: 'v1',
      c2: 'v2',
    };
    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj',
      c: child_props,
    };
    const options = {
      externalIds: [
        {
          id: "some_external_id_1",
          type: "brazeExternalId"
        },
        {
          id: "some_braze_id_2",
          type: "braze_id"
        }
      ],
      integrations: {
        All: false,
        Amplitude: true,
        Mixpanel: false
      }
    }
    const idoptions = {
      externalIds: [{
              id: "some_external_id_1",
              type: "brazeExternalId"
          }
      ]
  }
  await rc.identify("test_userId", {
      "email":"testuser@example.com",
      "location":"UK"
  }, idoptions);
  await rc.track('React Native event', props, options);
  await rc.screen('React Native screen', props);
  
  const appsFlyerId = await AppsFlyerIntegrationFactory.getAppsFlyerId();
  const rudderContext = await rc.getRudderContext();
  const traits = rudderContext.traits;
  }

  )();
  return (
    <>
    <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          {global.HermesInternal == null ? null : (
            <View style={styles.engine}>
              <Text style={styles.footer}>Engine: Hermes</Text>
            </View>
          )}
          <View style={styles.body}>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Step One</Text>
              <Text style={styles.sectionDescription}>
                Edit <Text style={styles.highlight}>App.js</Text> to change this
                screen and then come back to see your edits.
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>See Your Changes</Text>
              <Text style={styles.sectionDescription}>
                <ReloadInstructions />
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Debug</Text>
              <Text style={styles.sectionDescription}>
                <DebugInstructions />
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Learn More</Text>
              <Text style={styles.sectionDescription}>
                Read the docs to discover what to do next:
              </Text>
            </View>
            <LearnMoreLinks />
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
