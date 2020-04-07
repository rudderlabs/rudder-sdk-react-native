import React, {Component} from 'react';
import { StyleSheet, View, Button, Alert } from 'react-native';

class App extends Component {
  render () {
    return (
      <View style={styles.container}>
        <Button 
          title="Track"
          onPress={() => Alert.alert('Track button')} 
        />
        <Button 
          title="Screen"
          onPress={() => Alert.alert('Screen button')}
        />
        <Button
          title="Identify"
          onPress={() => Alert.alert('Identify button')}
        />
      </View>
    );
  }
}

export default App;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
