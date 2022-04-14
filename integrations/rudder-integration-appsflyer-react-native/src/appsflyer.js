import bridge from './bridge';
import { NativeEventEmitter, NativeModules } from 'react-native';

const { RudderIntegrationAppsflyerReactNative } = NativeModules;
const appsFlyerEventEmitter = new NativeEventEmitter(RudderIntegrationAppsflyerReactNative);

var devKey = "";
var isDebug = false;
var onInstallConversionDataListener = true;
var onDeepLinkListener = false;

async function setup() {
    await bridge.setup(devkey, isDebug === true, onInstallConversionDataListener === true, onDeepLinkListener === true);
}

function setOptions(options) {
    if (options == null) {
        throw new Error('RudderSDK: Warn: Options is Invalid, Aborting Appsflyer Initialization');
    }
    if (options.devKey) {
        if (typeof options.devKey !== 'string' || typeof options.devKey === 'undefined') {
            throw new Error('RudderSDK: Warn: devKey should be a string!');
        }
        devKey = options.devKey;
    }
    if (options.isDebug) {
        if (typeof options.isDebug !== 'boolean') {
            console.log('RudderSDK: Warn: isDebug should be a boolean!');
            return;
        }
        isDebug = options.isDebug
    }
    if (options.onInstallConversionDataListener) {
        if (typeof options.onInstallConversionDataListener !== 'boolean') {
            console.log('RudderSDK: Warn: onInstallConversionDataListener should be a boolean!');
            return;
        }
        onInstallConversionDataListener = options.onInstallConversionDataListener
    }
    if (options.onDeepLinkListener) {
        if (typeof options.onDeepLinkListener !== 'boolean') {
            console.log('RudderSDK: Warn: onDeepLinkListener should be a boolean!');
            return;
        }
        onDeepLinkListener = options.onDeepLinkListener
    }
}

function onInstallConversionData(callback) {
    const listener = appsFlyerEventEmitter.addListener('onInstallConversionDataLoaded', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback('Invalid data structure');
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
}

function onInstallConversionFailure(callback) {
    const listener = appsFlyerEventEmitter.addListener('onInstallConversionFailure', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback('Invalid data structure');
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

function onAppOpenAttribution(callback) {
    const listener = appsFlyerEventEmitter.addListener('onAppOpenAttribution', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback('Invalid data structure');
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

function onAttributionFailure(callback) {
    const listener = appsFlyerEventEmitter.addListener('onAttributionFailure', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback(new AFParseJSONException('Invalid data structure', _data));
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

function onDeepLink(callback) {
    const listener = appsFlyerEventEmitter.addListener('onDeepLinking', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback(new AFParseJSONException('Invalid data structure', _data));
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

export { onDeepLink, onInstallConversionData, onInstallConversionFailure, onAppOpenAttribution, onAttributionFailure, setOptions };
export default setup;