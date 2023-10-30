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
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const flowInterfaceSample = { alias: 'beyond turnip' };

  let flowInterface;
  let application;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/applications',
      body: {
        alias: 'gene instrumentation fatal',
        name: 'dapper creditor outside',
        description: 'woot delirious yahoo',
        comment: 'handle over',
        documentationURL: 'apropos some botch',
        startDate: '2021-11-04',
        endDate: '2021-11-04',
        applicationType: 'MIDDLEWARE',
        softwareType: 'ON_PREMISE_EXTERNAL_LIBRARY',
        nickname: 'sprinkles',
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
      if (response.body.length === 0) {
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
          expect(response.statusCode).to.equal(200);
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
            },
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
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);
      });

      it('edit button click should load edit FlowInterface page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowInterface');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);
      });

      it('edit button click should load edit FlowInterface page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowInterface');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowInterfacePageUrlPattern);
      });

      it('last delete button click should delete instance of FlowInterface', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('flowInterface').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
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
      cy.get(`[data-cy="alias"]`).type('violate');
      cy.get(`[data-cy="alias"]`).should('have.value', 'violate');

      cy.get(`[data-cy="status"]`).type('limply whereas');
      cy.get(`[data-cy="status"]`).should('have.value', 'limply whereas');

      cy.get(`[data-cy="documentationURL"]`).type('via boo daintily');
      cy.get(`[data-cy="documentationURL"]`).should('have.value', 'via boo daintily');

      cy.get(`[data-cy="documentationURL2"]`).type('thin excluding lest');
      cy.get(`[data-cy="documentationURL2"]`).should('have.value', 'thin excluding lest');

      cy.get(`[data-cy="description"]`).type('beyond');
      cy.get(`[data-cy="description"]`).should('have.value', 'beyond');

      cy.get(`[data-cy="startDate"]`).type('2021-11-03');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2021-11-03');

      cy.get(`[data-cy="endDate"]`).type('2021-11-04');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2021-11-04');

      cy.get(`[data-cy="source"]`).select(1);
      cy.get(`[data-cy="target"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        flowInterface = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', flowInterfacePageUrlPattern);
    });
  });
});
