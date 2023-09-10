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

describe('FlowInterface e2e test', () => {
  const flowInterfacePageUrl = '/flow-interface';
  const flowInterfacePageUrlPattern = new RegExp('/flow-interface(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const flowInterfaceSample = { alias: 'Portugal Pound bandwidth' };

  let flowInterface: any;
  let application: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/applications',
      body: {
        alias: 'Card innovative Indian',
        name: 'Account',
        description: 'cross-platform',
        comment: 'generating override override',
        documentationURL: 'orange Program',
        startDate: '2023-09-10',
        endDate: '2023-09-09',
        applicationType: 'HARDWARE',
        softwareType: 'CLOUD_CUSTOM',
        nickname: 'Antillian',
      },
    }).then(({ body }) => {
      application = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/flow-interfaces+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/flow-interfaces').as('postEntityRequest');
    cy.intercept('DELETE', '/api/flow-interfaces/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/data-flows', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/applications', {
      statusCode: 200,
      body: [application],
    });

    cy.intercept('GET', '/api/application-components', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/protocols', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/owners', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/functional-flow-steps', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (flowInterface) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/flow-interfaces/${flowInterface.id}`,
      }).then(() => {
        flowInterface = undefined;
      });
    }
  });

  afterEach(() => {
    if (application) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/applications/${application.id}`,
      }).then(() => {
        application = undefined;
      });
    }
  });

  it('FlowInterfaces menu should load FlowInterfaces page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-interface');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FlowInterface').should('exist');
    cy.url().should('match', flowInterfacePageUrlPattern);
  });

  describe('FlowInterface page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(flowInterfacePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FlowInterface page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/flow-interface/new$'));
        cy.getEntityCreateUpdateHeading('FlowInterface');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/flow-interfaces',
          body: {
            ...flowInterfaceSample,
            source: application,
            target: application,
          },
        }).then(({ body }) => {
          flowInterface = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/flow-interfaces+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [flowInterface],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(flowInterfacePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FlowInterface page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('flowInterface');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);
      });

      it('edit button click should load edit FlowInterface page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowInterface');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);
      });

      it('last delete button click should delete instance of FlowInterface', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('flowInterface').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);

        flowInterface = undefined;
      });
    });
  });

  describe('new FlowInterface page', () => {
    beforeEach(() => {
      cy.visit(`${flowInterfacePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FlowInterface');
    });

    it('should create an instance of FlowInterface', () => {
      cy.get(`[data-cy="alias"]`).type('synthesizing cross-media Rustic').should('have.value', 'synthesizing cross-media Rustic');

      cy.get(`[data-cy="status"]`).type('Operations Stand-alone parse').should('have.value', 'Operations Stand-alone parse');

      cy.get(`[data-cy="documentationURL"]`).type('Account Cotton').should('have.value', 'Account Cotton');

      cy.get(`[data-cy="documentationURL2"]`).type('bluetooth lime').should('have.value', 'bluetooth lime');

      cy.get(`[data-cy="description"]`).type('cohesive open-source models').should('have.value', 'cohesive open-source models');

      cy.get(`[data-cy="startDate"]`).type('2023-09-10').should('have.value', '2023-09-10');

      cy.get(`[data-cy="endDate"]`).type('2023-09-10').should('have.value', '2023-09-10');

      cy.get(`[data-cy="source"]`).select(1);
      cy.get(`[data-cy="target"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        flowInterface = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', flowInterfacePageUrlPattern);
    });
  });
});
