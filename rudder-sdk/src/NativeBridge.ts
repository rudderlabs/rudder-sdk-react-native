import { NativeModules } from 'react-native';

export interface NativeBridge {
    _initiateInstance(
        _writeKey: string,
        _endPointUrl: string,
        _flushQueueSize: number,
        _dbCountThreshold: number,
        _sleepTimeOut: number
    ): Promise<void>;

    _logEvent(
        _eventType: string,
        _eventName: String,
        _userId: string,
        _eventPropsJson: string,
        _userPropsJson: string,
        _integrationJson: string
    ): Promise<void>;
}

const nativeBridge: NativeBridge = NativeModules.RNRudderSdk;

if (!nativeBridge) {
	throw new Error('Failed to load Rudder.')
}

export default nativeBridge;