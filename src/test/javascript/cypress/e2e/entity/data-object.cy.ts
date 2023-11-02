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

describe('DataObject e2e test', () => {
  const dataObjectPageUrl = '/data-object';
  const dataObjectPageUrlPattern = new RegExp('/data-object(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dataObjectSample = { name: 'young instead' };

  let dataObject;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/data-objects+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/data-objects').as('postEntityRequest');
    cy.intercept('DELETE', '/api/data-objects/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dataObject) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/data-objects/${dataObject.id}`,
      }).then(() => {
        dataObject = undefined;
      });
    }
  });

  it('DataObjects menu should load DataObjects page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('data-object');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DataObject').should('exist');
    cy.url().should('match', dataObjectPageUrlPattern);
  });

  describe('DataObject page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dataObjectPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DataObject page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/data-object/new$'));
        cy.getEntityCreateUpdateHeading('DataObject');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataObjectPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/data-objects',
          body: dataObjectSample,
        }).then(({ body }) => {
          dataObject = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/data-objects+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dataObject],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dataObjectPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DataObject page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dataObject');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataObjectPageUrlPattern);
      });

      it('edit button click should load edit DataObject page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataObject');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataObjectPageUrlPattern);
      });

      it('edit button click should load edit DataObject page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DataObject');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataObjectPageUrlPattern);
      });

      it('last delete button click should delete instance of DataObject', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dataObject').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dataObjectPageUrlPattern);

        dataObject = undefined;
      });
    });
  });

  describe('new DataObject page', () => {
    beforeEach(() => {
      cy.visit(`${dataObjectPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DataObject');
    });

    it('should create an instance of DataObject', () => {
      cy.get(`[data-cy="name"]`).type('lovable faithfully');
      cy.get(`[data-cy="name"]`).should('have.value', 'lovable faithfully');

      cy.get(`[data-cy="type"]`).select('READ_WRITE_REPLICA');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dataObject = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', dataObjectPageUrlPattern);
    });
  });
});
