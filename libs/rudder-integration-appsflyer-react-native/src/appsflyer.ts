import bridge from './bridge';
import { isString, isStringArray, getStringArray } from './util';
import { Platform, NativeEventEmitter, NativeModules } from 'react-native';

const { RudderIntegrationAppsflyerReactNative } = NativeModules;
const appsFlyerEventEmitter = new NativeEventEmitter(RudderIntegrationAppsflyerReactNative);

let devKey = '';
let appleAppId = '';
let isDebug = false;
let onInstallConversionDataListener = true;
let onDeepLinkListener = false;
let timeToWaitForATTUserAuthorization = 0;

// Setup the integration
async function setup() {
  if (Platform.OS === 'ios') {
    await bridge.setup(
      devKey,
      isDebug === true,
      onInstallConversionDataListener === true,
      onDeepLinkListener === true,
      appleAppId,
      timeToWaitForATTUserAuthorization,
    );
  } else if (Platform.OS === 'android') {
    await bridge.setup(
      devKey,
      isDebug === true,
      onInstallConversionDataListener === true,
      onDeepLinkListener === true,
    );
  }
}

function setOptions(options: any) {
  if (options == null) {
    throw new Error('RudderSDK: Warn: Options is Invalid, Aborting Appsflyer Initialization');
  }
  if (options.devKey) {
    if (typeof options.devKey !== 'string' || typeof options.devKey === 'undefined') {
      throw new Error('RudderSDK: Warn: devKey should be a string!');
    }
    devKey = options.devKey;
  }
  if (options.appleAppId) {
    if (typeof options.appleAppId !== 'string' || typeof options.appleAppId === 'undefined') {
      throw new Error('RudderSDK: Warn: appleAppId should be a string!');
    }
    appleAppId = options.appleAppId;
  }
  if (options.isDebug) {
    if (typeof options.isDebug !== 'boolean') {
      console.log('RudderSDK: Warn: isDebug should be a boolean!');
      return;
    }
    isDebug = options.isDebug;
  }
  if (options.onInstallConversionDataListener) {
    if (typeof options.onInstallConversionDataListener !== 'boolean') {
      console.log('RudderSDK: Warn: onInstallConversionDataListener should be a boolean!');
      return;
    }
    onInstallConversionDataListener = options.onInstallConversionDataListener;
  }
  if (options.onDeepLinkListener) {
    if (typeof options.onDeepLinkListener !== 'boolean') {
      console.log('RudderSDK: Warn: onDeepLinkListener should be a boolean!');
      return;
    }
    onDeepLinkListener = options.onDeepLinkListener;
  }
  if (options.timeToWaitForATTUserAuthorization) {
    if (!Number.isInteger(options.timeToWaitForATTUserAuthorization)) {
      console.log('RudderSDK: Warn: timeToWaitForATTUserAuthorization should be an integer!');
      return;
    }
    timeToWaitForATTUserAuthorization = options.timeToWaitForATTUserAuthorization;
  }
}

function onInstallConversionData(callback: (data: any) => void) {
  const listener = appsFlyerEventEmitter.addListener('onInstallConversionDataLoaded', (_data) => {
    if (callback && typeof callback === typeof Function) {
      try {
        const data = JSON.parse(_data);
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

function onInstallConversionFailure(callback: (data: any) => void) {
  const listener = appsFlyerEventEmitter.addListener('onInstallConversionFailure', (_data) => {
    if (callback && typeof callback === typeof Function) {
      try {
        const data = JSON.parse(_data);
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

function onAppOpenAttribution(callback: (data: any) => void) {
  const listener = appsFlyerEventEmitter.addListener('onAppOpenAttribution', (_data) => {
    if (callback && typeof callback === typeof Function) {
      try {
        const data = JSON.parse(_data);
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

function onAttributionFailure(callback: (data: any) => void) {
  const listener = appsFlyerEventEmitter.addListener('onAttributionFailure', (_data) => {
    if (callback && typeof callback === typeof Function) {
      try {
        const data = JSON.parse(_data);
        callback(data);
      } catch (_error) {
        callback(new Error('Invalid data structure: ' + _data));
      }
    }
  });

  // unregister listener (suppose should be called from componentWillUnmount() )
  return function remove() {
    listener.remove();
  };
}

function onDeepLink(callback: (data: any) => void) {
  const listener = appsFlyerEventEmitter.addListener('onDeepLinking', (_data) => {
    if (callback && typeof callback === typeof Function) {
      try {
        const data = JSON.parse(_data);
        callback(data);
      } catch (_error) {
        callback(new Error('Invalid data structure: ' + _data));
      }
    }
  });

  // unregister listener (suppose should be called from componentWillUnmount() )
  return function remove() {
    listener.remove();
  };
}

/**
 * Manually pass the Firebase / GCM Device Token for Uninstall measurement.
 *
 * @param token Firebase Device Token.
 * @param successC success callback function.
 */
function updateServerUninstallToken(token: any, successC: any) {
  if (token == null) {
    token = '';
  }
  if (typeof token != 'string') {
    token = token.toString();
  }
  if (successC) {
    return bridge.updateServerUninstallToken(token, successC);
  } else {
    return bridge.updateServerUninstallToken(token, (result: any) => console.log(result));
  }
}

/**
 * Setting your own customer ID enables you to cross-reference your own unique ID with AppsFlyer’s unique ID and the other devices’ IDs.
 * This ID is available in AppsFlyer CSV reports along with Postback APIs for cross-referencing with your internal IDs.
 *
 * @param {string} userId Customer ID for client.
 * @param successC callback function.
 */
function setCustomerUserId(userId: any, successC: any) {
  if (userId == null) {
    userId = '';
  }
  if (typeof userId != 'string') {
    userId = userId.toString();
  }
  if (successC) {
    return bridge.setCustomerUserId(userId, successC);
  } else {
    return bridge.setCustomerUserId(userId, (result: any) => console.log(result));
  }
}

/**
 * Set Onelink custom/branded domains
 * Use this API during the SDK Initialization to indicate branded domains.
 * For more information please refer to https://support.appsflyer.com/hc/en-us/articles/360002329137-Implementing-Branded-Links
 * @param domains array of strings
 * @param successC success callback function.
 * @param errorC error callback function.
 */
function setOneLinkCustomDomains(domains: any, successC: any, errorC: any) {
  if (!(isString(domains) || isStringArray(domains))) {
    throw new Error(
      'RudderSDK: Appsflyer: setOneLinkCustomDomains: domains should be a string or an array of strings!',
    );
  }
  const domainsArray = getStringArray(domains);
  if (domainsArray === null) {
    throw new Error(
      'RudderSDK: Appsflyer: setOneLinkCustomDomains: failed to process domains array!',
    );
  }
  return bridge.setOneLinkCustomDomains(getStringArray(domains), successC, errorC);
}

async function getAppsFlyerId(): Promise<string | null> {
  try {
    const appsflyerId: string = await bridge.getAppsFlyerId();
    return appsflyerId;
  } catch (error) {
    console.log('getAppsFlyerId: Failed to getAppsFlyerId: ', error);
    return null;
  }
}

export {
  onDeepLink,
  onInstallConversionData,
  onInstallConversionFailure,
  onAppOpenAttribution,
  onAttributionFailure,
  setOptions,
  updateServerUninstallToken,
  setCustomerUserId,
  setOneLinkCustomDomains,
  getAppsFlyerId,
};
export default setup;
