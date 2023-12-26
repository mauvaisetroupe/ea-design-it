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

describe('OrganizationalEntity e2e test', () => {
  const organizationalEntityPageUrl = '/organizational-entity';
  const organizationalEntityPageUrlPattern = new RegExp('/organizational-entity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const organizationalEntitySample = { name: 'ack' };

  let organizationalEntity;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/organizational-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/organizational-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/organizational-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (organizationalEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organizational-entities/${organizationalEntity.id}`,
      }).then(() => {
        organizationalEntity = undefined;
      });
    }
  });

  it('OrganizationalEntities menu should load OrganizationalEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('organizational-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrganizationalEntity').should('exist');
    cy.url().should('match', organizationalEntityPageUrlPattern);
  });

  describe('OrganizationalEntity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(organizationalEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrganizationalEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/organizational-entity/new$'));
        cy.getEntityCreateUpdateHeading('OrganizationalEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationalEntityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/organizational-entities',
          body: organizationalEntitySample,
        }).then(({ body }) => {
          organizationalEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/organizational-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [organizationalEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(organizationalEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrganizationalEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('organizationalEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationalEntityPageUrlPattern);
      });

      it('edit button click should load edit OrganizationalEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationalEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationalEntityPageUrlPattern);
      });

      it('edit button click should load edit OrganizationalEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationalEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationalEntityPageUrlPattern);
      });

      it('last delete button click should delete instance of OrganizationalEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('organizationalEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationalEntityPageUrlPattern);

        organizationalEntity = undefined;
      });
    });
  });

  describe('new OrganizationalEntity page', () => {
    beforeEach(() => {
      cy.visit(`${organizationalEntityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrganizationalEntity');
    });

    it('should create an instance of OrganizationalEntity', () => {
      cy.get(`[data-cy="name"]`).type('lest between along');
      cy.get(`[data-cy="name"]`).should('have.value', 'lest between along');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        organizationalEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', organizationalEntityPageUrlPattern);
    });
  });
});
