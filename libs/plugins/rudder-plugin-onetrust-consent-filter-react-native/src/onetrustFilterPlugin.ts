import bridge from './bridge';

async function startConsentFilterPlugin(): Promise<boolean> {
  const consentGranted: boolean = await bridge.startConsentFilterPlugin();
  return consentGranted;
}

async function addConsentFilterPlugin(): Promise<void> {
  await bridge.setup();
}

export default addConsentFilterPlugin;
export { startConsentFilterPlugin };
