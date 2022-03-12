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

describe('Technology e2e test', () => {
  const technologyPageUrl = '/technology';
  const technologyPageUrlPattern = new RegExp('/technology(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const technologySample = { name: 'Avon Mouse Team-oriented' };

  let technology: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/technologies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/technologies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/technologies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (technology) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/technologies/${technology.id}`,
      }).then(() => {
        technology = undefined;
      });
    }
  });

  it('Technologies menu should load Technologies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('technology');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Technology').should('exist');
    cy.url().should('match', technologyPageUrlPattern);
  });

  describe('Technology page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(technologyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Technology page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/technology/new$'));
        cy.getEntityCreateUpdateHeading('Technology');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', technologyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/technologies',
          body: technologySample,
        }).then(({ body }) => {
          technology = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/technologies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [technology],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(technologyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Technology page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('technology');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', technologyPageUrlPattern);
      });

      it('edit button click should load edit Technology page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Technology');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', technologyPageUrlPattern);
      });

      it('last delete button click should delete instance of Technology', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('technology').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', technologyPageUrlPattern);

        technology = undefined;
      });
    });
  });

  describe('new Technology page', () => {
    beforeEach(() => {
      cy.visit(`${technologyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Technology');
    });

    it('should create an instance of Technology', () => {
      cy.get(`[data-cy="name"]`).type('ADP Cambridgeshire').should('have.value', 'ADP Cambridgeshire');

      cy.get(`[data-cy="type"]`).type('Internal Technician').should('have.value', 'Internal Technician');

      cy.get(`[data-cy="description"]`).type('Mouse Unbranded').should('have.value', 'Mouse Unbranded');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        technology = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', technologyPageUrlPattern);
    });
  });
});
