import React from 'react';
import { render } from '@testing-library/react-native';

import DefaultApp from './DefaultApp';

test('renders correctly', () => {
  const { getByTestId } = render(<DefaultApp />);
  expect(getByTestId('heading')).toHaveTextContent('Welcome');
});
