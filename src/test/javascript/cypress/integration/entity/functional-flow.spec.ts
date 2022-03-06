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

describe('FunctionalFlow e2e test', () => {
  const functionalFlowPageUrl = '/functional-flow';
  const functionalFlowPageUrlPattern = new RegExp('/functional-flow(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  let functionalFlow: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/functional-flows+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/functional-flows').as('postEntityRequest');
    cy.intercept('DELETE', '/api/functional-flows/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (functionalFlow) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/functional-flows/${functionalFlow.id}?deleteFlowInterfaces=true&deleteDatas=true`,
      }).then(() => {
        functionalFlow = undefined;
      });
    }
  });

  it('FunctionalFlows menu should load FunctionalFlows page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('functional-flow');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FunctionalFlow').should('exist');
    cy.url().should('match', functionalFlowPageUrlPattern);
  });

  describe('FunctionalFlow page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(functionalFlowPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FunctionalFlow page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/functional-flow/new$'));
        cy.getEntityCreateUpdateHeading('FunctionalFlow');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      const alias = 'TST.FLOW.' + Date.now();
      const functionalFlowSample = { alias: alias };

      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/functional-flows',
          body: functionalFlowSample,
        }).then(({ body }) => {
          functionalFlow = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/functional-flows+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [functionalFlow],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(functionalFlowPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FunctionalFlow page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('functionalFlow');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowPageUrlPattern);
      });

      it('edit button click should load edit FunctionalFlow page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FunctionalFlow');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowPageUrlPattern);
      });

      it('last delete button click should delete instance of FunctionalFlow', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('functionalFlow').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowPageUrlPattern);

        functionalFlow = undefined;
      });
    });
  });

  describe('new FunctionalFlow page', () => {
    beforeEach(() => {
      cy.visit(`${functionalFlowPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FunctionalFlow');
    });

    it('should create an instance of FunctionalFlow', () => {
      const alias = 'TST.FLOW.' + Date.now();

      cy.get(`[data-cy="alias"]`).type(alias).should('have.value', alias);

      cy.get(`[data-cy="description"]`).type('Peso Buckinghamshire').should('have.value', 'Peso Buckinghamshire');

      cy.get(`[data-cy="comment"]`).type('Customer Executive').should('have.value', 'Customer Executive');

      cy.get(`[data-cy="status"]`).type('system Rubber').should('have.value', 'system Rubber');

      cy.get(`[data-cy="documentationURL"]`).type('microchip overriding').should('have.value', 'microchip overriding');

      cy.get(`[data-cy="documentationURL2"]`).type('Steel').should('have.value', 'Steel');

      cy.get(`[data-cy="startDate"]`).type('2021-11-04').should('have.value', '2021-11-04');

      cy.get(`[data-cy="endDate"]`).type('2021-11-04').should('have.value', '2021-11-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        functionalFlow = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', functionalFlowPageUrlPattern);
    });
  });
});
