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

describe('Capability e2e test', () => {
  const capabilityPageUrl = '/capability';
  const capabilityPageUrlPattern = new RegExp('/capability(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const capabilitySample = { name: 'utilize' };

  let capability;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/capabilities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/capabilities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/capabilities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (capability) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/capabilities/${capability.id}`,
      }).then(() => {
        capability = undefined;
      });
    }
  });

  it('Capabilities menu should load Capabilities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('capability');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Capability').should('exist');
    cy.url().should('match', capabilityPageUrlPattern);
  });

  describe('Capability page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(capabilityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Capability page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/capability/new$'));
        cy.getEntityCreateUpdateHeading('Capability');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/capabilities',
          body: capabilitySample,
        }).then(({ body }) => {
          capability = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/capabilities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [capability],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(capabilityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Capability page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('capability');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityPageUrlPattern);
      });

      it('edit button click should load edit Capability page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Capability');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityPageUrlPattern);
      });

      it('edit button click should load edit Capability page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Capability');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityPageUrlPattern);
      });

      it('last delete button click should delete instance of Capability', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('capability').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityPageUrlPattern);

        capability = undefined;
      });
    });
  });

  describe('new Capability page', () => {
    beforeEach(() => {
      cy.visit(`${capabilityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Capability');
    });

    it('should create an instance of Capability', () => {
      cy.get(`[data-cy="name"]`).type('Consultant capability Infrastructure').should('have.value', 'Consultant capability Infrastructure');

      cy.get(`[data-cy="description"]`).type('Station microchip').should('have.value', 'Station microchip');

      cy.get(`[data-cy="comment"]`).type('B2B bluetooth').should('have.value', 'B2B bluetooth');

      cy.get(`[data-cy="level"]`).type('76749').should('have.value', '76749');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        capability = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', capabilityPageUrlPattern);
    });
  });
});
