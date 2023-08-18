class RNDBEncryption {
  key: string;
  enable: boolean;
  constructor(key: string, enable: boolean) {
    this.key = key;
    this.enable = enable;
  }
}

export default RNDBEncryption;
