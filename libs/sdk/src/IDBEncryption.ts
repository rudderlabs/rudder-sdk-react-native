interface IDBEncryption {
  key: string;
  enable: boolean;
  addDBEncryptionPlugin(key: string, enable: boolean): Promise<void>;
}

export default IDBEncryption;
