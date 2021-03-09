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
  Button,
  Alert,
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
import AppcenterIntegrationFactory from 'rudder-integration-appcenter-react-native/src/bridge';

const App: () => React$Node = () => {
  (async function () {
    const config = {
      dataPlaneUrl: 'https://c09b14e8d9d8.ngrok.io',
      trackAppLifecycleEvents: true,
      logLevel: RUDDER_LOG_LEVEL.DEBUG,
      withFactories: [appcenter],
    };
    await rc.setup('1pTxG1Tqxr7FCrqIy7j0p28AENV', config);
    const integrationReady = await rc.checkIntegrationReady('App Center');
    if(integrationReady)
    {
      // if the required integration is ready we are grabbing the user consent
    Alert.alert(
      "User Consent",
      "Would you like to share your data with Appcenter",
      [
        {
          text: "Cancel",
          onPress: () => AppcenterIntegrationFactory.disableAnalytics(),
          style: "cancel"
        },
        { text: "OK", onPress: () => AppcenterIntegrationFactory.enableAnalytics() }
      ],
      { cancelable: false });
    }
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
    //await rc.identify('new user', props, null);
    await rc.track('React Native event', props, child_props);
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
