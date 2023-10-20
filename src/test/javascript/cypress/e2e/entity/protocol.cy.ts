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

describe('Protocol e2e test', () => {
  const protocolPageUrl = '/protocol';
  const protocolPageUrlPattern = new RegExp('/protocol(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const protocolSample = { name: 'oh', type: 'ETL' };

  let protocol;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/protocols+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/protocols').as('postEntityRequest');
    cy.intercept('DELETE', '/api/protocols/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (protocol) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/protocols/${protocol.id}`,
      }).then(() => {
        protocol = undefined;
      });
    }
  });

  it('Protocols menu should load Protocols page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('protocol');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Protocol').should('exist');
    cy.url().should('match', protocolPageUrlPattern);
  });

  describe('Protocol page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(protocolPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Protocol page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/protocol/new$'));
        cy.getEntityCreateUpdateHeading('Protocol');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', protocolPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/protocols',
          body: protocolSample,
        }).then(({ body }) => {
          protocol = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/protocols+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [protocol],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(protocolPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Protocol page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('protocol');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', protocolPageUrlPattern);
      });

      it('edit button click should load edit Protocol page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Protocol');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', protocolPageUrlPattern);
      });

      it('edit button click should load edit Protocol page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Protocol');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', protocolPageUrlPattern);
      });

      it('last delete button click should delete instance of Protocol', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('protocol').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', protocolPageUrlPattern);

        protocol = undefined;
      });
    });
  });

  describe('new Protocol page', () => {
    beforeEach(() => {
      cy.visit(`${protocolPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Protocol');
    });

    it('should create an instance of Protocol', () => {
      cy.get(`[data-cy="name"]`).type('sushi round');
      cy.get(`[data-cy="name"]`).should('have.value', 'sushi round');

      cy.get(`[data-cy="type"]`).select('FRONT');

      cy.get(`[data-cy="description"]`).type('forked');
      cy.get(`[data-cy="description"]`).should('have.value', 'forked');

      cy.get(`[data-cy="scope"]`).type('to acoustics');
      cy.get(`[data-cy="scope"]`).should('have.value', 'to acoustics');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        protocol = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', protocolPageUrlPattern);
    });
  });
});
