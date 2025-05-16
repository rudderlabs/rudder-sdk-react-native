import bridge from './bridge';

class DBEncryption {
  key: string;
  enable: boolean;
  constructor(key: string, enable: boolean) {
    this.key = key;
    this.enable = enable;
  }

  // Add the encryption plugin
  async addDBEncryptionPlugin(): Promise<void> {
    await bridge.setup(this.key, this.enable);
  }
}

export default DBEncryption;
