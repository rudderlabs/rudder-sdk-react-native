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

import rc, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';
import appsflyer from 'rudder-integration-appsflyer-react-native';
import amplitude from 'rudder-integration-amplitude-react-native';
import braze from 'rudder-integration-braze-react-native';
import firebase from 'rudder-integration-firebase-react-native';
import appcenter from 'rudder-integration-appcenter-react-native';
import clevertap from 'rudder-integration-clevertap-react-native';

import AppsFlyerIntegrationFactory from 'rudder-integration-appsflyer-react-native/src/bridge';

const App: () => React$Node = () => {
  (async function () {
    const config = {
      dataPlaneUrl: 'https://eb78-61-95-158-116.ngrok.io',
      trackAppLifecycleEvents: true,
      logLevel: RUDDER_LOG_LEVEL.VERBOSE,
      withFactories: [braze]
    };

    await rc.setup('20JW1xIqZrpr0swwPsOqXLPiDj1', config);

    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj'
    };

    await rc.identify("test_userIdiOS", {
      "email": "testuseriOS@example.com",
      "location": "UK"
    });
    await rc.track('React Native event', props);
    await rc.screen('React Native screen', props);
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
