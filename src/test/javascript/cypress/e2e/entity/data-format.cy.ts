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

describe('DataFormat e2e test', () => {
  const dataFormatPageUrl = '/data-format';
  const dataFormatPageUrlPattern = new RegExp('/data-format(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const dataFormatSample = { name: 'mobile Executive distributed' };

  let dataFormat: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/data-formats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/data-formats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/data-formats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dataFormat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/data-formats/${dataFormat.id}`,
      }).then(() => {
        dataFormat = undefined;
      });
    }
  });

  it('DataFormats menu should load DataFormats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('data-format');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DataFormat').should('exist');
    cy.url().should('match', dataFormatPageUrlPattern);
  });

  describe('DataFormat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dataFormatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DataFormat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/data-format/new$'));
        cy.getEntityCreateUpdateHeading('DataFormat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFormatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/data-formats',
          body: dataFormatSample,
        }).then(({ body }) => {
          dataFormat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/data-formats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dataFormat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dataFormatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DataFormat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dataFormat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFormatPageUrlPattern);
      });

      it('edit button click should load edit DataFormat page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataFormat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFormatPageUrlPattern);
      });

      it('last delete button click should delete instance of DataFormat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dataFormat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dataFormatPageUrlPattern);

        dataFormat = undefined;
      });
    });
  });

  describe('new DataFormat page', () => {
    beforeEach(() => {
      cy.visit(`${dataFormatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DataFormat');
    });

    it('should create an instance of DataFormat', () => {
      cy.get(`[data-cy="name"]`).type('Integration Clothing').should('have.value', 'Integration Clothing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        dataFormat = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', dataFormatPageUrlPattern);
    });
  });
});
