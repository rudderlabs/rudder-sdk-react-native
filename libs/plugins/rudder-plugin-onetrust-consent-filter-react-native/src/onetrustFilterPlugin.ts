import bridge from './bridge';

async function startConsentFilterPlugin(
  cdn: string,
  domainIdentifier: string,
  languageCode: string,
): Promise<boolean> {
  const consentGranted: boolean = await bridge.startConsentFilterPlugin(
    cdn,
    domainIdentifier,
    languageCode,
  );
  return consentGranted;
}

async function addConsentFilterPlugin(): Promise<void> {
  await bridge.setup();
}

export default addConsentFilterPlugin;
export { startConsentFilterPlugin };
