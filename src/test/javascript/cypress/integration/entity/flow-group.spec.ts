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

describe('FlowGroup e2e test', () => {
  const flowGroupPageUrl = '/flow-group';
  const flowGroupPageUrlPattern = new RegExp('/flow-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const flowGroupSample = {};

  let flowGroup: any;
  //let functionalFlow: any;
  //let functionalFlowStep: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/functional-flows',
      body: {"alias":"Throughway","description":"JSON Intelligent","comment":"Sharable AI","status":"hard Buckinghamshire","documentationURL":"software Human","documentationURL2":"Cotton pink","startDate":"2021-11-03","endDate":"2021-11-04"},
    }).then(({ body }) => {
      functionalFlow = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/functional-flow-steps',
      body: {"description":"Devolved","stepOrder":20724},
    }).then(({ body }) => {
      functionalFlowStep = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/flow-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/flow-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/flow-groups/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/functional-flows', {
      statusCode: 200,
      body: [functionalFlow],
    });

    cy.intercept('GET', '/api/functional-flow-steps', {
      statusCode: 200,
      body: [functionalFlowStep],
    });

  });
   */

  afterEach(() => {
    if (flowGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/flow-groups/${flowGroup.id}`,
      }).then(() => {
        flowGroup = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (functionalFlow) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/functional-flows/${functionalFlow.id}`,
      }).then(() => {
        functionalFlow = undefined;
      });
    }
    if (functionalFlowStep) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/functional-flow-steps/${functionalFlowStep.id}`,
      }).then(() => {
        functionalFlowStep = undefined;
      });
    }
  });
   */

  it('FlowGroups menu should load FlowGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FlowGroup').should('exist');
    cy.url().should('match', flowGroupPageUrlPattern);
  });

  describe('FlowGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(flowGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FlowGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/flow-group/new$'));
        cy.getEntityCreateUpdateHeading('FlowGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/flow-groups',
          body: {
            ...flowGroupSample,
            flow: functionalFlow,
            steps: functionalFlowStep,
          },
        }).then(({ body }) => {
          flowGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/flow-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [flowGroup],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(flowGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(flowGroupPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details FlowGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('flowGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });

      it('edit button click should load edit FlowGroup page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of FlowGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('flowGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);

        flowGroup = undefined;
      });
    });
  });

  describe('new FlowGroup page', () => {
    beforeEach(() => {
      cy.visit(`${flowGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FlowGroup');
    });

    it.skip('should create an instance of FlowGroup', () => {
      cy.get(`[data-cy="title"]`).type('Handmade Paradigm').should('have.value', 'Handmade Paradigm');

      cy.get(`[data-cy="url"]`).type('http://constance.com').should('have.value', 'http://constance.com');

      cy.get(`[data-cy="flow"]`).select(1);
      cy.get(`[data-cy="steps"]`).select([0]);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        flowGroup = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', flowGroupPageUrlPattern);
    });
  });
});
