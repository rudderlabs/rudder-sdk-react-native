import bridge from './bridge';
import { NativeEventEmitter, NativeModules } from 'react-native';

const { RudderIntegrationAppsflyerReactNative } = NativeModules;
const appsFlyerEventEmitter = new NativeEventEmitter(RudderIntegrationAppsflyerReactNative);

var registerConvListener = false;
var registerDeepLinkListener = false;

async function setup() {
    await bridge.setup(registerConvListener === true, registerDeepLinkListener === true);
}

function registerForConversionListeners() {
    registerConvListener = true;
}

function registerForDeepLinkListeners() {
    registerDeepLinkListener = true;
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

export { onDeepLink, onInstallConversionData, onInstallConversionFailure, onAppOpenAttribution, onAttributionFailure, registerForConversionListeners, registerForDeepLinkListeners };
export default setup;