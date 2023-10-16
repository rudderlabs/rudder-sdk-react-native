import bridge from './bridge';

async function setup(key: string, enable: boolean) {
  await bridge.setup(key, enable);
}

export default setup;
