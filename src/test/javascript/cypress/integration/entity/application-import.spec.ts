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

describe('ApplicationImport e2e test', () => {
  const applicationImportPageUrl = '/application-import';
  const applicationImportPageUrlPattern = new RegExp('/application-import(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const applicationImportSample = {};

  let applicationImport: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/application-imports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-imports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-imports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (applicationImport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-imports/${applicationImport.id}`,
      }).then(() => {
        applicationImport = undefined;
      });
    }
  });

  it('ApplicationImports menu should load ApplicationImports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-import');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationImport').should('exist');
    cy.url().should('match', applicationImportPageUrlPattern);
  });

  describe('ApplicationImport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationImportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApplicationImport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/application-import/new$'));
        cy.getEntityCreateUpdateHeading('ApplicationImport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationImportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/application-imports',
          body: applicationImportSample,
        }).then(({ body }) => {
          applicationImport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/application-imports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [applicationImport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationImportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ApplicationImport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicationImport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationImportPageUrlPattern);
      });

      it('edit button click should load edit ApplicationImport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationImport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationImportPageUrlPattern);
      });

      it('last delete button click should delete instance of ApplicationImport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('applicationImport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationImportPageUrlPattern);

        applicationImport = undefined;
      });
    });
  });

  describe('new ApplicationImport page', () => {
    beforeEach(() => {
      cy.visit(`${applicationImportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ApplicationImport');
    });

    it('should create an instance of ApplicationImport', () => {
      cy.get(`[data-cy="importId"]`).type('RAM Team-oriented Refined').should('have.value', 'RAM Team-oriented Refined');

      cy.get(`[data-cy="excelFileName"]`).type('Communications Extended deposit').should('have.value', 'Communications Extended deposit');

      cy.get(`[data-cy="idFromExcel"]`).type('Lari').should('have.value', 'Lari');

      cy.get(`[data-cy="name"]`).type('lavender Account Cotton').should('have.value', 'lavender Account Cotton');

      cy.get(`[data-cy="description"]`).type('connecting mesh Bedfordshire').should('have.value', 'connecting mesh Bedfordshire');

      cy.get(`[data-cy="type"]`).type('model mobile').should('have.value', 'model mobile');

      cy.get(`[data-cy="softwareType"]`).type('Functionality robust Intelligent').should('have.value', 'Functionality robust Intelligent');

      cy.get(`[data-cy="category1"]`).type('e-markets Synergized').should('have.value', 'e-markets Synergized');

      cy.get(`[data-cy="category2"]`).type('Response').should('have.value', 'Response');

      cy.get(`[data-cy="category3"]`).type('eyeballs').should('have.value', 'eyeballs');

      cy.get(`[data-cy="technology"]`).type('parse').should('have.value', 'parse');

      cy.get(`[data-cy="documentation"]`).type('Direct').should('have.value', 'Direct');

      cy.get(`[data-cy="comment"]`).type('Soap Table multi-byte').should('have.value', 'Soap Table multi-byte');

      cy.get(`[data-cy="owner"]`).type('Consultant').should('have.value', 'Consultant');

      cy.get(`[data-cy="importStatus"]`).select('ERROR');

      cy.get(`[data-cy="importStatusMessage"]`).type('microchip fuchsia up').should('have.value', 'microchip fuchsia up');

      cy.get(`[data-cy="existingApplicationID"]`)
        .type('programming expedite optimizing')
        .should('have.value', 'programming expedite optimizing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        applicationImport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationImportPageUrlPattern);
    });
  });
});
