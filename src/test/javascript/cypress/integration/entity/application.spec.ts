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
  entityListRefreshButton,
} from '../../support/entity';

describe('Application e2e test', () => {
  const applicationPageUrl = '/application';
  const applicationPageUrlPattern = new RegExp('/application(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const applicationSample = { name: 'port Architect override' };

  let application: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/applications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/applications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/applications/*').as('deleteEntityRequest');
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

  it('Applications menu should load Applications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application');

    // wait for REST call, but application list cached, so need to click on refresh
    cy.wait(500).get(entityListRefreshButton).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Application').should('exist');
    cy.url().should('match', applicationPageUrlPattern);
  });

  describe('Application page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Application page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/application/new$'));
        cy.getEntityCreateUpdateHeading('Application');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        // wait for REST call, but application list cached, so need to click on refresh
        cy.wait(500).get(entityListRefreshButton).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/applications',
          body: applicationSample,
        }).then(({ body }) => {
          application = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/applications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [application],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
    });
  });

  describe('new Application page', () => {
    beforeEach(() => {
      cy.visit(`${applicationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Application');
    });

    it('should create an instance of Application', () => {
      const alias = 'TST.HPX.' + Date.now();

      cy.get(`[data-cy="alias"]`).type(alias).should('have.value', alias);

      cy.get(`[data-cy="name"]`).type('Guarani Games Oregon').should('have.value', 'Guarani Games Oregon');

      cy.get(`[data-cy="description"]`).type('panel').should('have.value', 'panel');

      cy.get(`[data-cy="comment"]`).type('Swaziland Comoro USB').should('have.value', 'Swaziland Comoro USB');

      cy.get(`[data-cy="documentationURL"]`).type('Direct turquoise blue').should('have.value', 'Direct turquoise blue');

      cy.get(`[data-cy="startDate"]`).type('2021-11-04').should('have.value', '2021-11-04');

      cy.get(`[data-cy="endDate"]`).type('2021-11-04').should('have.value', '2021-11-04');

      cy.get(`[data-cy="applicationType"]`).select('MIDDLEWARE');

      cy.get(`[data-cy="softwareType"]`).select('ON_PREMISE_COTS');

      cy.get(`[data-cy="nickname"]`).type('port Architect override').should('have.value', 'port Architect override');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        application = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationPageUrlPattern);
    });
  });
});
