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

describe('FunctionalFlowStep e2e test', () => {
  const functionalFlowStepPageUrl = '/functional-flow-step';
  const functionalFlowStepPageUrlPattern = new RegExp('/functional-flow-step(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const functionalFlowStepSample = {};

  let functionalFlowStep: any;
  //let flowInterface: any;
  //let functionalFlow: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/flow-interfaces',
      body: {"alias":"collaborative","status":"index virtual","documentationURL":"Loan Cambridgeshire Fish","documentationURL2":"International","description":"didactic","startDate":"2023-09-09","endDate":"2023-09-10"},
    }).then(({ body }) => {
      flowInterface = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/functional-flows',
      body: {"alias":"markets systemic","description":"Plastic deposit","comment":"Alabama","status":"quantify Fall","documentationURL":"Operations Computer engineer","documentationURL2":"microchip content","startDate":"2023-09-10","endDate":"2023-09-10"},
    }).then(({ body }) => {
      functionalFlow = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/functional-flow-steps+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/functional-flow-steps').as('postEntityRequest');
    cy.intercept('DELETE', '/api/functional-flow-steps/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/flow-interfaces', {
      statusCode: 200,
      body: [flowInterface],
    });

    cy.intercept('GET', '/api/flow-groups', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/functional-flows', {
      statusCode: 200,
      body: [functionalFlow],
    });

  });
   */

  afterEach(() => {
    if (functionalFlowStep) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/functional-flow-steps/${functionalFlowStep.id}`,
      }).then(() => {
        functionalFlowStep = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (flowInterface) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/flow-interfaces/${flowInterface.id}`,
      }).then(() => {
        flowInterface = undefined;
      });
    }
    if (functionalFlow) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/functional-flows/${functionalFlow.id}`,
      }).then(() => {
        functionalFlow = undefined;
      });
    }
  });
   */

  it('FunctionalFlowSteps menu should load FunctionalFlowSteps page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('functional-flow-step');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FunctionalFlowStep').should('exist');
    cy.url().should('match', functionalFlowStepPageUrlPattern);
  });

  describe('FunctionalFlowStep page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(functionalFlowStepPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FunctionalFlowStep page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/functional-flow-step/new$'));
        cy.getEntityCreateUpdateHeading('FunctionalFlowStep');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowStepPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/functional-flow-steps',
          body: {
            ...functionalFlowStepSample,
            flowInterface: flowInterface,
            flow: functionalFlow,
          },
        }).then(({ body }) => {
          functionalFlowStep = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/functional-flow-steps+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [functionalFlowStep],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(functionalFlowStepPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(functionalFlowStepPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details FunctionalFlowStep page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('functionalFlowStep');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowStepPageUrlPattern);
      });

      it('edit button click should load edit FunctionalFlowStep page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FunctionalFlowStep');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowStepPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of FunctionalFlowStep', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('functionalFlowStep').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', functionalFlowStepPageUrlPattern);

        functionalFlowStep = undefined;
      });
    });
  });

  describe('new FunctionalFlowStep page', () => {
    beforeEach(() => {
      cy.visit(`${functionalFlowStepPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FunctionalFlowStep');
    });

    it.skip('should create an instance of FunctionalFlowStep', () => {
      cy.get(`[data-cy="description"]`).type('capacitor CSS').should('have.value', 'capacitor CSS');

      cy.get(`[data-cy="stepOrder"]`).type('22585').should('have.value', '22585');

      cy.get(`[data-cy="flowInterface"]`).select(1);
      cy.get(`[data-cy="flow"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        functionalFlowStep = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', functionalFlowStepPageUrlPattern);
    });
  });
});
