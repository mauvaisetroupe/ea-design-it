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

describe('FlowImport e2e test', () => {
  const flowImportPageUrl = '/flow-import';
  const flowImportPageUrlPattern = new RegExp('/flow-import(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const flowImportSample = {};

  let flowImport: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/flow-imports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/flow-imports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/flow-imports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (flowImport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/flow-imports/${flowImport.id}`,
      }).then(() => {
        flowImport = undefined;
      });
    }
  });

  it('FlowImports menu should load FlowImports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-import');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FlowImport').should('exist');
    cy.url().should('match', flowImportPageUrlPattern);
  });

  describe('FlowImport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(flowImportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FlowImport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/flow-import/new$'));
        cy.getEntityCreateUpdateHeading('FlowImport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowImportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/flow-imports',
          body: flowImportSample,
        }).then(({ body }) => {
          flowImport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/flow-imports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [flowImport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(flowImportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FlowImport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('flowImport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowImportPageUrlPattern);
      });

      it('edit button click should load edit FlowImport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowImport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowImportPageUrlPattern);
      });

      it('last delete button click should delete instance of FlowImport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('flowImport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowImportPageUrlPattern);

        flowImport = undefined;
      });
    });
  });

  describe('new FlowImport page', () => {
    beforeEach(() => {
      cy.visit(`${flowImportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FlowImport');
    });

    it('should create an instance of FlowImport', () => {
      cy.get(`[data-cy="idFlowFromExcel"]`).type('Shirt Sausages transmitting').should('have.value', 'Shirt Sausages transmitting');

      cy.get(`[data-cy="flowAlias"]`).type('Multi-layered').should('have.value', 'Multi-layered');

      cy.get(`[data-cy="sourceElement"]`).type('Tennessee Chips').should('have.value', 'Tennessee Chips');

      cy.get(`[data-cy="targetElement"]`).type('redundant channels overriding').should('have.value', 'redundant channels overriding');

      cy.get(`[data-cy="description"]`).type('Checking invoice').should('have.value', 'Checking invoice');

      cy.get(`[data-cy="stepDescription"]`).type('Front-line models Somalia').should('have.value', 'Front-line models Somalia');

      cy.get(`[data-cy="integrationPattern"]`).type('protocol Soft').should('have.value', 'protocol Soft');

      cy.get(`[data-cy="frequency"]`).type('Pennsylvania Fundamental Account').should('have.value', 'Pennsylvania Fundamental Account');

      cy.get(`[data-cy="format"]`).type('generate Consultant').should('have.value', 'generate Consultant');

      cy.get(`[data-cy="swagger"]`).type('Diverse access Programmable').should('have.value', 'Diverse access Programmable');

      cy.get(`[data-cy="sourceURLDocumentation"]`).type('Electronics').should('have.value', 'Electronics');

      cy.get(`[data-cy="targetURLDocumentation"]`).type('protocol leading-edge').should('have.value', 'protocol leading-edge');

      cy.get(`[data-cy="sourceDocumentationStatus"]`).type('Myanmar').should('have.value', 'Myanmar');

      cy.get(`[data-cy="targetDocumentationStatus"]`).type('incubate Avon').should('have.value', 'incubate Avon');

      cy.get(`[data-cy="flowStatus"]`).type('AGP one-to-one Division').should('have.value', 'AGP one-to-one Division');

      cy.get(`[data-cy="comment"]`).type('Avon reboot').should('have.value', 'Avon reboot');

      cy.get(`[data-cy="documentName"]`).type('Tasty').should('have.value', 'Tasty');

      cy.get(`[data-cy="importInterfaceStatus"]`).select('EXISTING');

      cy.get(`[data-cy="importFunctionalFlowStatus"]`).select('UPDATED');

      cy.get(`[data-cy="importDataFlowStatus"]`).select('NEW');

      cy.get(`[data-cy="importStatusMessage"]`)
        .type('Card Configuration needs-based')
        .should('have.value', 'Card Configuration needs-based');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        flowImport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', flowImportPageUrlPattern);
    });
  });
});
