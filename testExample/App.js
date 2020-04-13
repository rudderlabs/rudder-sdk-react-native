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

import RNRudderSdk from 'react-native-rudder-sdk';

const App: () => React$Node = () => {
  let rc = new RNRudderSdk();
  (async function(){
    const config = {
      dataPlaneUrl: "https://hosted.rudderlabs.com",
      trackAppLifecycleEvents: false
    };
    await rc.setup("1XI9Niof0vuKt5XTLurbWh9KkXn", config);
    const child_props = {
      "c1":"v1",
      "c2":"v2"
    };
    const props = {
      "k1":"v1",
      "k2":"v3",
      "k3":"v3",
      "c":child_props
    };
    await rc.identify("new user11", props, null);
    await rc.track("new event", props, child_props); 
    await rc.screen("new screen", props);
  })();
  return (
    <>
      <View style={{
        flex : 1,
        justifyContent: "center",
        alignItems: "center",
        fontSize: 20
      }}>
        <Text>Hello world</Text>
      </View>
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
