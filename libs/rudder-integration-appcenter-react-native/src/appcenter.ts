import bridge from './bridge';

// Setup the integration
async function setup() {
  await bridge.setup();
}

async function enableAnalytics() {
  await bridge.enableAnalytics();
}

async function disableAnalytics() {
  await bridge.disableAnalytics();
}

export { enableAnalytics, disableAnalytics };
export default setup;
