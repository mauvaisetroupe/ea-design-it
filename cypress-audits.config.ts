import { defineConfig } from 'cypress';
import defaultConfig from './cypress.config';

export default defineConfig({
  ...defaultConfig,
  e2e: {
    ...defaultConfig.e2e,
    specPattern: 'src/test/javascript/cypress/integration/**/*.audits.ts',
  },
});
