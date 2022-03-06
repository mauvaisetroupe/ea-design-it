import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Application Import and Flows e2e test', () => {
  const applicationPageUrl = '/application';
  const applicationPageUrlPattern = new RegExp('/application(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const applicationSample = { name: 'port Architect override' };

  let application: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/landscape-views+(?*|)').as('landscapeEntitiesRequest');
    cy.intercept('GET', '/api/plantuml/landscape-view/get-svg/*').as('plantumlRequest');
    cy.intercept('DELETE', '/api/landscape-views/*').as('deleteEntityRequest');
  });

  afterEach(() => {});

  it('Import Applications', () => {
    cy.visit('/');
    cy.get('[data-cy="import-excel-applications"]').click();
    cy.fixture('01-import-applications.xlsx').then(fileContent => {
      cy.get('input[type="file"]').selectFile('src/test/javascript/cypress/fixtures/01-import-applications.xlsx');
    });
    cy.get('button[type="submit"]').click();
    // Assert
  });

  it('Applications should be in application list', () => {
    // Assert ?
  });

  it('Import Flows', () => {
    cy.visit('/');
    cy.get('[data-cy="import-excel-flows"]').click();
    cy.fixture('02-import-flows.xlsx').then(fileContent => {
      cy.get('input[type="file"]').selectFile(['src/test/javascript/cypress/fixtures/02-import-flows.xlsx'], { force: true });
    });
    cy.get('button[type="submit"]').click();
    // Assert ?
  });

  it('Landscape should be in correctly created', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('landscape-view');
    cy.wait('@landscapeEntitiesRequest').then(({ response }) => {
      cy.get(entityTableSelector).should('exist');
    });

    // click on landscape detail
    const description = cy.get('td').contains('02 import flows');
    description.should('be.visible');
    description.siblings().first().find('a').click();

    cy.wait('@plantumlRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.get('.table').find('tbody').find('tr').should('have.length', 4);
  });

  it('delete Landscape', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('landscape-view');
    cy.wait('@landscapeEntitiesRequest').then(({ response }) => {
      cy.get(entityTableSelector).should('exist');
    });

    // click on landscape detail
    const description = cy.get('td').contains('02 import flows');
    description.should('be.visible');
    description.siblings().find(entityDeleteButtonSelector).click();
    cy.getEntityDeleteDialogHeading('landscapeView').should('exist');
    cy.get(entityConfirmDeleteButtonSelector).click();
    cy.wait('@deleteEntityRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(204);
    });
    cy.wait('@landscapeEntitiesRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
  });
});
