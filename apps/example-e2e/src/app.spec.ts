import { device, element, by, expect } from 'detox';

describe('Example', () => {
  beforeEach(async () => {
    await device.reloadReactNative();
  });

  it('should display init button', async () => {
    await expect(element(by.id('init_btn'))).toHaveText('Initialize');
  });
});
