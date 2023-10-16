import { IDBEncryption } from '@rudderstack/rudder-sdk-react-native';
import bridge from './bridge';

class DBEncryption implements IDBEncryption {
  key: string;
  enable: boolean;
  constructor(key: string, enable: boolean) {
    this.key = key;
    this.enable = enable;
  }

  async addDBEncryptionPlugin(key: string, enable: boolean): Promise<void> {
    await bridge.setup(key, enable);
  }
}

export default DBEncryption;
