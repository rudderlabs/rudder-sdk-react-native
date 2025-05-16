import rudderClient from './RudderClient';
import { RUDDER_LOG_LEVEL } from './Logger';
import IDBEncryption from './IDBEncryption';
import IRudderContext from './IRudderContext';
import RudderSdkReactNative from './NativeRudderSdkReactNative';

export function multiply(a: number, b: number): number {
  return RudderSdkReactNative.multiply(a, b);
}

export { RUDDER_LOG_LEVEL, IDBEncryption, IRudderContext };
export default rudderClient;
