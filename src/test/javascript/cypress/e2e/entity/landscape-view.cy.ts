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

describe('LandscapeView e2e test', () => {
  const landscapeViewPageUrl = '/landscape-view';
  const landscapeViewPageUrlPattern = new RegExp('/landscape-view(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const landscapeViewSample = {};

  let landscapeView;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/landscape-views+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/landscape-views').as('postEntityRequest');
    cy.intercept('DELETE', '/api/landscape-views/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (landscapeView) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/landscape-views/${landscapeView.id}`,
      }).then(() => {
        landscapeView = undefined;
      });
    }
  });

  it('LandscapeViews menu should load LandscapeViews page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('landscape-view');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LandscapeView').should('exist');
    cy.url().should('match', landscapeViewPageUrlPattern);
  });

  describe('LandscapeView page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(landscapeViewPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LandscapeView page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/landscape-view/new$'));
        cy.getEntityCreateUpdateHeading('LandscapeView');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', landscapeViewPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/landscape-views',
          body: landscapeViewSample,
        }).then(({ body }) => {
          landscapeView = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/landscape-views+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [landscapeView],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(landscapeViewPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LandscapeView page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('landscapeView');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', landscapeViewPageUrlPattern);
      });

      it('edit button click should load edit LandscapeView page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LandscapeView');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', landscapeViewPageUrlPattern);
      });

      it('edit button click should load edit LandscapeView page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LandscapeView');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', landscapeViewPageUrlPattern);
      });

      it('last delete button click should delete instance of LandscapeView', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('landscapeView').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', landscapeViewPageUrlPattern);

        landscapeView = undefined;
      });
    });
  });

  describe('new LandscapeView page', () => {
    beforeEach(() => {
      cy.visit(`${landscapeViewPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LandscapeView');
    });

    it('should create an instance of LandscapeView', () => {
      cy.get(`[data-cy="viewpoint"]`).select('APPLICATION_LANDSCAPE');

      cy.get(`[data-cy="diagramName"]`).type('benchmark').should('have.value', 'benchmark');

      cy.get(`[data-cy="compressedDrawXML"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="compressedDrawSVG"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        landscapeView = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', landscapeViewPageUrlPattern);
    });
  });
});
