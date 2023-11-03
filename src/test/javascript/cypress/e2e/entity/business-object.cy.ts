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

describe('BusinessObject e2e test', () => {
  const businessObjectPageUrl = '/business-object';
  const businessObjectPageUrlPattern = new RegExp('/business-object(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const businessObjectSample = { name: 'notwithstanding' };

  let businessObject;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/business-objects+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/business-objects').as('postEntityRequest');
    cy.intercept('DELETE', '/api/business-objects/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (businessObject) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-objects/${businessObject.id}`,
      }).then(() => {
        businessObject = undefined;
      });
    }
  });

  it('BusinessObjects menu should load BusinessObjects page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('business-object');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BusinessObject').should('exist');
    cy.url().should('match', businessObjectPageUrlPattern);
  });

  describe('BusinessObject page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(businessObjectPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BusinessObject page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/business-object/new$'));
        cy.getEntityCreateUpdateHeading('BusinessObject');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', businessObjectPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/business-objects',
          body: businessObjectSample,
        }).then(({ body }) => {
          businessObject = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/business-objects+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [businessObject],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(businessObjectPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BusinessObject page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('businessObject');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', businessObjectPageUrlPattern);
      });

      it('edit button click should load edit BusinessObject page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BusinessObject');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', businessObjectPageUrlPattern);
      });

      it('edit button click should load edit BusinessObject page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BusinessObject');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', businessObjectPageUrlPattern);
      });

      it('last delete button click should delete instance of BusinessObject', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('businessObject').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', businessObjectPageUrlPattern);

        businessObject = undefined;
      });
    });
  });

  describe('new BusinessObject page', () => {
    beforeEach(() => {
      cy.visit(`${businessObjectPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BusinessObject');
    });

    it('should create an instance of BusinessObject', () => {
      cy.get(`[data-cy="name"]`).type('yet sternly');
      cy.get(`[data-cy="name"]`).should('have.value', 'yet sternly');

      cy.get(`[data-cy="abstractBusinessObject"]`).should('not.be.checked');
      cy.get(`[data-cy="abstractBusinessObject"]`).click();
      cy.get(`[data-cy="abstractBusinessObject"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        businessObject = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', businessObjectPageUrlPattern);
    });
  });
});
