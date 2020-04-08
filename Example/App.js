import React, { Component } from "react";
import { StyleSheet, Text, View, Button } from "react-native";
import { RudderSDK } from 'react-native-rudder-sdk';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center"
  },
  buttonContainer: {
    margin: 20
  }
});

class App extends Component {
  render() {
    console.log(RudderSDK);
    return (
      <View style={styles.container}>
        <View style={styles.buttonContainer}>
          <Text>Example Application for ReactNative SDK</Text>
        </View>
        <View style={styles.buttonContainer}>
          <Button
            onPress={() => {
              alert("Sample Track has been called");
            }}
            title="Track Button"
          />
        </View>
        <View style={styles.buttonContainer}>
          <Button
            onPress={() => {
              alert("Sample Screen has been called");
            }}
            title="Screen Button"
          />
        </View>
        <View style={styles.buttonContainer}>
          <Button
            onPress={() => {
              alert("Sample Identify has been called");
            }}
            title="Identify Button"
          />
        </View>
      </View>
    );
  }
}

export default App;
