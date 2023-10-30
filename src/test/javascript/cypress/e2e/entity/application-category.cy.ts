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

describe('ApplicationCategory e2e test', () => {
  const applicationCategoryPageUrl = '/application-category';
  const applicationCategoryPageUrlPattern = new RegExp('/application-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const applicationCategorySample = { name: 'unlike' };

  let applicationCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/application-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (applicationCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-categories/${applicationCategory.id}`,
      }).then(() => {
        applicationCategory = undefined;
      });
    }
  });

  it('ApplicationCategories menu should load ApplicationCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationCategory').should('exist');
    cy.url().should('match', applicationCategoryPageUrlPattern);
  });

  describe('ApplicationCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApplicationCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/application-category/new$'));
        cy.getEntityCreateUpdateHeading('ApplicationCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/application-categories',
          body: applicationCategorySample,
        }).then(({ body }) => {
          applicationCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/application-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [applicationCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ApplicationCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicationCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationCategoryPageUrlPattern);
      });

      it('edit button click should load edit ApplicationCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationCategoryPageUrlPattern);
      });

      it('edit button click should load edit ApplicationCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of ApplicationCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('applicationCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationCategoryPageUrlPattern);

        applicationCategory = undefined;
      });
    });
  });

  describe('new ApplicationCategory page', () => {
    beforeEach(() => {
      cy.visit(`${applicationCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ApplicationCategory');
    });

    it('should create an instance of ApplicationCategory', () => {
      cy.get(`[data-cy="name"]`).type('sign meanwhile ick');
      cy.get(`[data-cy="name"]`).should('have.value', 'sign meanwhile ick');

      cy.get(`[data-cy="type"]`).type('entry');
      cy.get(`[data-cy="type"]`).should('have.value', 'entry');

      cy.get(`[data-cy="description"]`).type('annihilate under anxiously');
      cy.get(`[data-cy="description"]`).should('have.value', 'annihilate under anxiously');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        applicationCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationCategoryPageUrlPattern);
    });
  });
});
