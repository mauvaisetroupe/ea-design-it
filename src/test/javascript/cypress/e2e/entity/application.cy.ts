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

describe('Application e2e test', () => {
  const applicationPageUrl = '/application';
  const applicationPageUrlPattern = new RegExp('/application(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const applicationSample = { name: 'publication under reheat' };

  let application;

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
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
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
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
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
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Application page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('application');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationPageUrlPattern);
      });

      it('edit button click should load edit Application page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Application');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationPageUrlPattern);
      });

      it('edit button click should load edit Application page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Application');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationPageUrlPattern);
      });

      it('last delete button click should delete instance of Application', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('application').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationPageUrlPattern);

        application = undefined;
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
      cy.get(`[data-cy="alias"]`).type('anenst duh');
      cy.get(`[data-cy="alias"]`).should('have.value', 'anenst duh');

      cy.get(`[data-cy="name"]`).type('angrily without');
      cy.get(`[data-cy="name"]`).should('have.value', 'angrily without');

      cy.get(`[data-cy="description"]`).type('yet pinion');
      cy.get(`[data-cy="description"]`).should('have.value', 'yet pinion');

      cy.get(`[data-cy="comment"]`).type('enter hmph');
      cy.get(`[data-cy="comment"]`).should('have.value', 'enter hmph');

      cy.get(`[data-cy="documentationURL"]`).type('considering root');
      cy.get(`[data-cy="documentationURL"]`).should('have.value', 'considering root');

      cy.get(`[data-cy="startDate"]`).type('2021-11-04');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2021-11-04');

      cy.get(`[data-cy="endDate"]`).type('2021-11-04');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2021-11-04');

      cy.get(`[data-cy="applicationType"]`).select('PARTNER');

      cy.get(`[data-cy="softwareType"]`).select('ON_PREMISE_EXTERNAL_LIBRARY');

      cy.get(`[data-cy="nickname"]`).type('while');
      cy.get(`[data-cy="nickname"]`).should('have.value', 'while');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        application = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationPageUrlPattern);
    });
  });
});
