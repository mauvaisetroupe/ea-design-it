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

describe('ExternalSystem e2e test', () => {
  const externalSystemPageUrl = '/external-system';
  const externalSystemPageUrlPattern = new RegExp('/external-system(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const externalSystemSample = {};

  let externalSystem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/external-systems+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/external-systems').as('postEntityRequest');
    cy.intercept('DELETE', '/api/external-systems/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (externalSystem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/external-systems/${externalSystem.id}`,
      }).then(() => {
        externalSystem = undefined;
      });
    }
  });

  it('ExternalSystems menu should load ExternalSystems page', () => {
    cy.visit('/');
    cy.get('[data-cy="home-welcome"]');

    cy.clickOnEntityMenuItem('external-system');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExternalSystem').should('exist');
    cy.url().should('match', externalSystemPageUrlPattern);
  });

  describe('ExternalSystem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(externalSystemPageUrl);
        cy.get('[data-cy="ExternalSystemHeading"]');
        cy.wait('@entitiesRequest');
      });

      it('should load create ExternalSystem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/external-system/new$'));
        cy.getEntityCreateUpdateHeading('ExternalSystem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', externalSystemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/external-systems',
          body: externalSystemSample,
        }).then(({ body }) => {
          externalSystem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/external-systems+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [externalSystem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(externalSystemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExternalSystem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('externalSystem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', externalSystemPageUrlPattern);
      });

      it('edit button click should load edit ExternalSystem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExternalSystem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', externalSystemPageUrlPattern);
      });

      it('edit button click should load edit ExternalSystem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExternalSystem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', externalSystemPageUrlPattern);
      });

      it('last delete button click should delete instance of ExternalSystem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('externalSystem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', externalSystemPageUrlPattern);

        externalSystem = undefined;
      });
    });
  });

  describe('new ExternalSystem page', () => {
    beforeEach(() => {
      cy.visit(`${externalSystemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExternalSystem');
    });

    it('should create an instance of ExternalSystem', () => {
      cy.get(`[data-cy="externalSystemID"]`).type('nice which boring');
      cy.get(`[data-cy="externalSystemID"]`).should('have.value', 'nice which boring');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        externalSystem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', externalSystemPageUrlPattern);
    });
  });
});
