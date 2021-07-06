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
      dataPlaneUrl: 'https://164685213c7b.ngrok.io',
      trackAppLifecycleEvents: false,
      logLevel: RUDDER_LOG_LEVEL.VERBOSE
    };
    // const defaultOptions = {
    //   integrations: {
    //     "App Center": true
    //   }
    // }

    await rc.setup('1pAKRv50y15Ti6UWpYroGJaO0Dj', config);

    // await rc.registerCallback('App Center', () => {
    //   console.log("App Center is ready");
    // })

    // const child_props = {
    //   c1: 'v1',
    //   c2: 'v2',
    // };

    // const props = {
    //   k1: 'v1',
    //   k2: 'v3',
    //   k3: 'v3',
    //   name: 'Miraj',
    //   c: child_props,
    // };
    
    // const options = {
    //   externalIds: [
    //     {
    //       id: "some_external_id_1",
    //       type: "brazeExternalId"
    //     },
    //     {
    //       id: "some_braze_id_2",
    //       type: "braze_id"
    //     }
    //   ],
    //   integrations: {
    //     All: false,
    //     Amplitude: true,
    //     Mixpanel: false
    //   }
    // }

  await rc.identify({"first name":"test"});  
  await rc.identify("test_userId", {
      "email":"testuser@example.com",
      "location":"UK"
  });
  await rc.identify({"full name":"test_user_id"});
  await rc.identify("user2")
  await rc.identify({"first name": "user 2",
"last name": "test 2",
"full name": "user2 test 2"});
await rc.identify("user3",{"first name": "user 3",
"last name": "test 3",
"full name": "user3 test 3"})
await rc.reset();
await rc.identify({"first name": "user 4",
"last name": "test 4",
"full name": "user4 test 4"});
await rc.identify("user4");



  // await rc.track('React Native event', props, options);
  // await rc.screen('React Native screen', props);
  
  // const appsFlyerId = await AppsFlyerIntegrationFactory.getAppsFlyerId();
  // const appsFlyerOptions = {
  //       externalIds: [{
  //               id: appsFlyerId,
  //               type: "appsflyerExternalId"
  //           }
  //       ]
  //   }
  // await rc.identify("test_userId", {
  //     "email":"testuser@example.com",
  //     "location":"UK"
  // }, appsFlyerOptions);
  // await rc.track("case_request_created",null,appsFlyerOptions);
  
  // const rudderContext = await rc.getRudderContext();
  // const traits = rudderContext.traits;

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
