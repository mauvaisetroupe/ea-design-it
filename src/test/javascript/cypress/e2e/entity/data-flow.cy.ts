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

describe('DataFlow e2e test', () => {
  const dataFlowPageUrl = '/data-flow';
  const dataFlowPageUrlPattern = new RegExp('/data-flow(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dataFlowSample = { resourceName: 'usually long-term' };

  let dataFlow;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/data-flows+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/data-flows').as('postEntityRequest');
    cy.intercept('DELETE', '/api/data-flows/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dataFlow) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/data-flows/${dataFlow.id}`,
      }).then(() => {
        dataFlow = undefined;
      });
    }
  });

  it('DataFlows menu should load DataFlows page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('data-flow');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DataFlow').should('exist');
    cy.url().should('match', dataFlowPageUrlPattern);
  });

  describe('DataFlow page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dataFlowPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DataFlow page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/data-flow/new$'));
        cy.getEntityCreateUpdateHeading('DataFlow');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/data-flows',
          body: dataFlowSample,
        }).then(({ body }) => {
          dataFlow = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/data-flows+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dataFlow],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dataFlowPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DataFlow page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dataFlow');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowPageUrlPattern);
      });

      it('edit button click should load edit DataFlow page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataFlow');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowPageUrlPattern);
      });

      it('edit button click should load edit DataFlow page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataFlow');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowPageUrlPattern);
      });

      it('last delete button click should delete instance of DataFlow', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dataFlow').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowPageUrlPattern);

        dataFlow = undefined;
      });
    });
  });

  describe('new DataFlow page', () => {
    beforeEach(() => {
      cy.visit(`${dataFlowPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DataFlow');
    });

    it('should create an instance of DataFlow', () => {
      cy.get(`[data-cy="resourceName"]`).type('openly how contrail');
      cy.get(`[data-cy="resourceName"]`).should('have.value', 'openly how contrail');

      cy.get(`[data-cy="resourceType"]`).type('majestically quizzical');
      cy.get(`[data-cy="resourceType"]`).should('have.value', 'majestically quizzical');

      cy.get(`[data-cy="description"]`).type('freeze a jackal');
      cy.get(`[data-cy="description"]`).should('have.value', 'freeze a jackal');

      cy.get(`[data-cy="frequency"]`).select('HOURLY');

      cy.get(`[data-cy="contractURL"]`).type('phooey cure hm');
      cy.get(`[data-cy="contractURL"]`).should('have.value', 'phooey cure hm');

      cy.get(`[data-cy="documentationURL"]`).type('easy visible against');
      cy.get(`[data-cy="documentationURL"]`).should('have.value', 'easy visible against');

      cy.get(`[data-cy="startDate"]`).type('2021-11-04');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2021-11-04');

      cy.get(`[data-cy="endDate"]`).type('2021-11-04');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2021-11-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dataFlow = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', dataFlowPageUrlPattern);
    });
  });
});
