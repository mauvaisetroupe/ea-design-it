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

describe('DataFlowImport e2e test', () => {
  const dataFlowImportPageUrl = '/data-flow-import';
  const dataFlowImportPageUrlPattern = new RegExp('/data-flow-import(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dataFlowImportSample = {};

  let dataFlowImport: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/data-flow-imports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/data-flow-imports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/data-flow-imports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dataFlowImport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/data-flow-imports/${dataFlowImport.id}`,
      }).then(() => {
        dataFlowImport = undefined;
      });
    }
  });

  it('DataFlowImports menu should load DataFlowImports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('data-flow-import');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DataFlowImport').should('exist');
    cy.url().should('match', dataFlowImportPageUrlPattern);
  });

  describe('DataFlowImport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dataFlowImportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DataFlowImport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/data-flow-import/new$'));
        cy.getEntityCreateUpdateHeading('DataFlowImport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowImportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/data-flow-imports',
          body: dataFlowImportSample,
        }).then(({ body }) => {
          dataFlowImport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/data-flow-imports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dataFlowImport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dataFlowImportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DataFlowImport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dataFlowImport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowImportPageUrlPattern);
      });

      it('edit button click should load edit DataFlowImport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataFlowImport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowImportPageUrlPattern);
      });

      it('last delete button click should delete instance of DataFlowImport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dataFlowImport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowImportPageUrlPattern);

        dataFlowImport = undefined;
      });
    });
  });

  describe('new DataFlowImport page', () => {
    beforeEach(() => {
      cy.visit(`${dataFlowImportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DataFlowImport');
    });

    it('should create an instance of DataFlowImport', () => {
      cy.get(`[data-cy="dataId"]`).type('holistic Dynamic bypass').should('have.value', 'holistic Dynamic bypass');

      cy.get(`[data-cy="dataParentId"]`).type('bottom-line').should('have.value', 'bottom-line');

      cy.get(`[data-cy="dataParentName"]`).type('Pike magenta explicit').should('have.value', 'Pike magenta explicit');

      cy.get(`[data-cy="functionalFlowId"]`).type('back Borders withdrawal').should('have.value', 'back Borders withdrawal');

      cy.get(`[data-cy="flowInterfaceId"]`).type('deposit Grocery synthesize').should('have.value', 'deposit Grocery synthesize');

      cy.get(`[data-cy="dataType"]`).type('Account Persistent').should('have.value', 'Account Persistent');

      cy.get(`[data-cy="dataResourceName"]`).type('lime').should('have.value', 'lime');

      cy.get(`[data-cy="dataResourceType"]`).type('program').should('have.value', 'program');

      cy.get(`[data-cy="dataDescription"]`).type('target web-readiness').should('have.value', 'target web-readiness');

      cy.get(`[data-cy="dataFrequency"]`).type('generate').should('have.value', 'generate');

      cy.get(`[data-cy="dataFormat"]`).type('Intelligent solution-oriented').should('have.value', 'Intelligent solution-oriented');

      cy.get(`[data-cy="dataContractURL"]`).type('Ghana Courts Metal').should('have.value', 'Ghana Courts Metal');

      cy.get(`[data-cy="dataDocumentationURL"]`).type('Plastic calculating CSS').should('have.value', 'Plastic calculating CSS');

      cy.get(`[data-cy="source"]`).type('Account navigating').should('have.value', 'Account navigating');

      cy.get(`[data-cy="target"]`).type('interface Small Indonesia').should('have.value', 'interface Small Indonesia');

      cy.get(`[data-cy="importDataStatus"]`).select('NEW');

      cy.get(`[data-cy="importDataItemStatus"]`).select('UPDATED');

      cy.get(`[data-cy="importStatusMessage"]`).type('Dam').should('have.value', 'Dam');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        dataFlowImport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', dataFlowImportPageUrlPattern);
    });
  });
});
