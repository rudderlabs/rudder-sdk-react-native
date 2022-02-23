// /**
//  * Sample React Native App
//  * https://github.com/facebook/react-native
//  *
//  * @format
//  * @flow strict-local
//  */

// import React from 'react';
// import {
//   SafeAreaView,
//   StyleSheet,
//   ScrollView,
//   View,
//   Text,
//   StatusBar,
// } from 'react-native';

// import {
//   Header,
//   LearnMoreLinks,
//   Colors,
//   DebugInstructions,
//   ReloadInstructions,
// } from 'react-native/Libraries/NewAppScreen';




// const App: () => React$Node = () => {
//   (async function () {
//     const config = {
//       dataPlaneUrl: 'https://d433-61-95-158-116.ngrok.io',
//       trackAppLifecycleEvents: true,
//       recordScreenViews: true,
//       logLevel: RUDDER_LOG_LEVEL.VERBOSE,
//       withFactories: [braze]
//     };

//     const props = {
//       k1: 'v1',
//       k2: 'v3',
//       k3: 'v3',
//       name: 'Miraj'
//     };

//     await rc.identify("test_userIdiOS", {
//       "email": "testuseriOS@example.com",
//       "location": "UK"
//     });
//     await rc.track('React Native event', props);
//     await rc.screen('React Native screen', props);
//   }

//   )();
//   return (
//     <>
//       <StatusBar barStyle="dark-content" />
//       <SafeAreaView>
//         <ScrollView
//           contentInsetAdjustmentBehavior="automatic"
//           style={styles.scrollView}>
//           <Header />
//           {global.HermesInternal == null ? null : (
//             <View style={styles.engine}>
//               <Text style={styles.footer}>Engine: Hermes</Text>
//             </View>
//           )}
//           <View style={styles.body}>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>Step One</Text>
//               <Text style={styles.sectionDescription}>
//                 Edit <Text style={styles.highlight}>App.js</Text> to change this
//                 screen and then come back to see your edits.
//               </Text>
//             </View>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>See Your Changes</Text>
//               <Text style={styles.sectionDescription}>
//                 <ReloadInstructions />
//               </Text>
//             </View>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>Debug</Text>
//               <Text style={styles.sectionDescription}>
//                 <DebugInstructions />
//               </Text>
//             </View>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>Learn More</Text>
//               <Text style={styles.sectionDescription}>
//                 Read the docs to discover what to do next:
//               </Text>
//             </View>
//             <LearnMoreLinks />
//           </View>
//         </ScrollView>
//       </SafeAreaView>
//     </>
//   );
// };

// const styles = StyleSheet.create({
//   scrollView: {
//     backgroundColor: Colors.lighter,
//   },
//   engine: {
//     position: 'absolute',
//     right: 0,
//   },
//   body: {
//     backgroundColor: Colors.white,
//   },
//   sectionContainer: {
//     marginTop: 32,
//     paddingHorizontal: 24,
//   },
//   sectionTitle: {
//     fontSize: 24,
//     fontWeight: '600',
//     color: Colors.black,
//   },
//   sectionDescription: {
//     marginTop: 8,
//     fontSize: 18,
//     fontWeight: '400',
//     color: Colors.dark,
//   },
//   highlight: {
//     fontWeight: '700',
//   },
//   footer: {
//     color: Colors.dark,
//     fontSize: 12,
//     fontWeight: '600',
//     padding: 4,
//     paddingRight: 12,
//     textAlign: 'right',
//   },
// });

// export default App;


import * as React from 'react';
import {
  Text,
Button,
} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import rc, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';
import braze from 'rudder-integration-braze-react-native';


const Stack = createNativeStackNavigator();

const App = () => {
  (async function () {
    const config = {
      dataPlaneUrl: 'https://d433-61-95-158-116.ngrok.io',
      trackAppLifecycleEvents: true,
      recordScreenViews: true,
      logLevel: RUDDER_LOG_LEVEL.VERBOSE,
      withFactories: [braze]
    };

    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj'
    };

    await rc.setup('1pAKRv50y15Ti6UWpYroGJaO0Dj', config);
    
    await rc.identify("test_userIdiOS", {
      "email": "testuseriOS@example.com",
      "location": "UK"
    });
    await rc.track('React Native event', props);
    await rc.screen('React Native screen', props);
  }

  )();
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          options={{ title: 'Welcome' }}
        />
        <Stack.Screen name="Profile" component={ProfileScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

const HomeScreen = ({ navigation }) => {
  (async function () {
    
    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj'
    };

    await rc.track('React Native Home Screen Event', props);
    await rc.screen('React Native  Home screen', props);
  }

  )();
  return (
    <Button
      title="Go to Jane's profile"
      onPress={() =>
        navigation.navigate('Profile', { name: 'Jane' })
      }
    />
  );
};
const ProfileScreen = ({ navigation, route }) => {
  (async function () {
    
    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj'
    };

    await rc.track('React Native Profile Screen Event', props);
    await rc.screen('React Native  Profile screen', props);
  }

  )();
  return <Text>This is {route.params.name}'s profile</Text>;
};

export default App;