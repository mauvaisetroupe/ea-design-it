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

describe('DataFlowItem e2e test', () => {
  const dataFlowItemPageUrl = '/data-flow-item';
  const dataFlowItemPageUrlPattern = new RegExp('/data-flow-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const dataFlowItemSample = { resourceName: 'Table' };

  let dataFlowItem: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/data-flow-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/data-flow-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/data-flow-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dataFlowItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/data-flow-items/${dataFlowItem.id}`,
      }).then(() => {
        dataFlowItem = undefined;
      });
    }
  });

  it('DataFlowItems menu should load DataFlowItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('data-flow-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DataFlowItem').should('exist');
    cy.url().should('match', dataFlowItemPageUrlPattern);
  });

  describe('DataFlowItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dataFlowItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DataFlowItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/data-flow-item/new$'));
        cy.getEntityCreateUpdateHeading('DataFlowItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/data-flow-items',
          body: dataFlowItemSample,
        }).then(({ body }) => {
          dataFlowItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/data-flow-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dataFlowItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dataFlowItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DataFlowItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dataFlowItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowItemPageUrlPattern);
      });

      it('edit button click should load edit DataFlowItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataFlowItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowItemPageUrlPattern);
      });

      it('last delete button click should delete instance of DataFlowItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dataFlowItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFlowItemPageUrlPattern);

        dataFlowItem = undefined;
      });
    });
  });

  describe('new DataFlowItem page', () => {
    beforeEach(() => {
      cy.visit(`${dataFlowItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DataFlowItem');
    });

    it('should create an instance of DataFlowItem', () => {
      cy.get(`[data-cy="resourceName"]`).type('Uganda Ergonomic Producer').should('have.value', 'Uganda Ergonomic Producer');

      cy.get(`[data-cy="resourceType"]`).type('circuit bypass').should('have.value', 'circuit bypass');

      cy.get(`[data-cy="description"]`).type('GB Administrator Cambridgeshire').should('have.value', 'GB Administrator Cambridgeshire');

      cy.get(`[data-cy="contractURL"]`).type('withdrawal Bahamas withdrawal').should('have.value', 'withdrawal Bahamas withdrawal');

      cy.get(`[data-cy="documentationURL"]`).type('pricing').should('have.value', 'pricing');

      cy.get(`[data-cy="startDate"]`).type('2021-11-20').should('have.value', '2021-11-20');

      cy.get(`[data-cy="endDate"]`).type('2021-11-20').should('have.value', '2021-11-20');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        dataFlowItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', dataFlowItemPageUrlPattern);
    });
  });
});
