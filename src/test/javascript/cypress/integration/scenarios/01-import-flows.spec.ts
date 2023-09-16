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
    cy.intercept('DELETE', '/api/landscape-views/*').as('landscapeDeleteEntityRequest');
    cy.intercept('GET', '/api/applications+(?*|)').as('applicationEntitiesRequest');
    cy.intercept('DELETE', '/api/applications/*').as('applicationDeleteEntityRequest');
    cy.intercept('POST', '/api/import/flow/upload-multi-file').as('landscapeImportRequest');
    cy.intercept('POST', '/api/import/summary').as('excelSummaryRequest');
    cy.intercept('POST', '/api/import/application/upload-file').as('applicationsImportRequest');
    cy.intercept('POST', '/api/functional-flows').as('flowPostEntityRequest');
    cy.intercept('DELETE', '/api/functional-flows/*').as('flowDeleteEntityRequest');
    cy.intercept('GET', '/api/functional-flows+(?*|)').as('flowEntitiesRequest');
  });

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  afterEach(() => {});

  it('Import Applications', () => {
    cy.visit('/');
    cy.get('[data-cy="import-excel-applications"]').click();
    cy.fixture('01-import-applications.xlsx').then(fileContent => {
      cy.get('input[type="file"]').selectFile('src/test/javascript/cypress/fixtures/01-import-applications.xlsx');
    });
    cy.get('button[type="submit"]').click();
    cy.wait('@applicationsImportRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
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

    cy.wait('@excelSummaryRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.get('button[type="submit"]').click();

    cy.wait('@landscapeImportRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
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

  it('Import Flows second time - should be idempotent', () => {
    cy.visit('/');
    cy.get('[data-cy="import-excel-flows"]').click();
    cy.fixture('02-import-flows.xlsx').then(fileContent => {
      cy.get('input[type="file"]').selectFile(['src/test/javascript/cypress/fixtures/02-import-flows.xlsx'], { force: true });
    });
    cy.get('button[type="submit"]').click();

    cy.wait('@excelSummaryRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.get('button[type="submit"]').click();

    cy.wait('@landscapeImportRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
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

  // CREATE EXTERNAL FUNCTIONALFLOW MAUALLY
  it('create external flow manually', () => {
    const functionalFlowPageUrl = '/functional-flow';
    cy.visit(`${functionalFlowPageUrl}`);
    cy.get(entityCreateButtonSelector).click();
    cy.getEntityCreateUpdateHeading('FunctionalFlow');
    cy.get(`[data-cy="alias"]`).type('CYP.02').should('have.value', 'CYP.02');

    cy.get(entityCreateSaveButtonSelector).click();

    cy.wait('@flowPostEntityRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(201);
    });
  });

  // WE SHOULD HAVE NOW 5 FLOWS in landscape

  it('Import Flows second time - should be idempotent', () => {
    cy.visit('/');
    cy.get('[data-cy="import-excel-flows"]').click();
    cy.fixture('02-import-flows.xlsx').then(fileContent => {
      cy.get('input[type="file"]').selectFile(['src/test/javascript/cypress/fixtures/02-import-flows.xlsx'], { force: true });
    });
    cy.get('button[type="submit"]').click();
    cy.wait('@excelSummaryRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.get('button[type="submit"]').click();

    cy.wait('@landscapeImportRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
  });

  it('Landscape should be in correctly created with external', () => {
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

    cy.get('.table').find('tbody').find('tr').should('have.length', 5);
  });

  it('delete created Landscape', () => {
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
    cy.wait('@landscapeDeleteEntityRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(204);
    });
    cy.wait('@landscapeEntitiesRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
  });

  // it('last delete button click should delete instance of FunctionalFlow', () => {
  //   cy.visit('/');
  //   cy.clickOnEntityMenuItem('functional-flow');
  //   cy.wait('@flowEntitiesRequest').then(({ response }) => {
  //       cy.get(entityTableSelector).should('exist');
  //   });

  //   cy.get(entityDeleteButtonSelector).last().click();
  //   cy.getEntityDeleteDialogHeading('functionalFlow').should('exist');
  //   cy.get(entityConfirmDeleteButtonSelector).click();
  //   cy.wait('@flowDeleteEntityRequest').then(({ response }) => {
  //     expect(response!.statusCode).to.equal(204);
  //   });
  // });
});
