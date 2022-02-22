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
import braze from 'rudder-integration-braze-react-native';



const App: () => React$Node = () => {
  (async function () {
    const config = {
      dataPlaneUrl: 'https://7e76-61-95-158-116.ngrok.io',
      trackAppLifecycleEvents: true,
      recordScreenViews: true,
      logLevel: RUDDER_LOG_LEVEL.VERBOSE,
      withFactories: [braze]
    };


    // await rc.track("Deprecated non-null")
    // await rc.track("Deprecated null");
    // await rc.track("Deprecated empty");
    
    // await rc.track("NonDep non-null")
    // await rc.track("NonDep null");
    // await rc.track("NonDep empty");
    

    await rc.putDeviceToken("devicetoken");
    await rc.putAdvertisingId("advertisingId");
    await rc.putAnonymousId("anonymousId");

    await rc.setup('1pAKRv50y15Ti6UWpYroGJaO0Dj', config);

    await rc.putDeviceToken("nondeptoken");
    await rc.track("NonDep Device token non-null")
    await rc.putDeviceToken(null);
    await rc.track("NonDep Device token null");
    await rc.putDeviceToken("");
    await rc.track("NonDep Device token empty");

    await rc.putDeviceToken("depandroid","depiOSToken");
    await rc.track("Deprecated Device token non-null")
    await rc.putDeviceToken(null, null);
    await rc.track("Deprecated Device token null");
    await rc.putDeviceToken("", "");
    await rc.track("Deprecated Device token empty");
    
    await rc.putAnonymousId("nondepanonId");
    await rc.track("NonDep AnonymousId non-null")
    await rc.putAnonymousId(null);
    await rc.track("NonDep AnonymousId null");
    await rc.putAnonymousId("");
    await rc.track("NonDep AnonymousId empty");

    await rc.setAnonymousId("depanonId");
    await rc.track("Deprecated AnonymousId non-null")
    await rc.setAnonymousId(null);
    await rc.track("Deprecated AnonymousId null");
    await rc.setAnonymousId("");
    await rc.track("Deprecated AnonymousId empty");


    await rc.setAdvertisingId("depaddvertId");
    await rc.track("Dep AdvertisingId non-null")
    await rc.setAdvertisingId(null);
    await rc.track("Dep AdvertisingId null");
    await rc.setAdvertisingId("");
    await rc.track("Dep AdvertisingId empty");


    await rc.putAdvertisingId("nondepadvertId");
    await rc.track("NonDeprecated AdvertisingId non-null")
    await rc.putAdvertisingId(null);
    await rc.track("NonDeprecated AdvertisingId null");
    await rc.putAdvertisingId("");
    await rc.track("NonDeprecated AdvertisingId empty");

    // const props = {
    //   k1: 'v1',
    //   k2: 'v3',
    //   k3: 'v3',
    //   name: 'Miraj'
    // };

    // await rc.identify("test_userIdiOS", {
    //   "email": "testuseriOS@example.com",
    //   "location": "UK"
    // });
    // await rc.track('React Native event', props);
    // await rc.screen('React Native screen', props);
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
