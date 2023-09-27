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

describe('ApplicationComponent e2e test', () => {
  const applicationComponentPageUrl = '/application-component';
  const applicationComponentPageUrlPattern = new RegExp('/application-component(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const applicationComponentSample = { name: 'withdrawal Savings' };

  let applicationComponent;
  let application;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/applications',
      body: {
        alias: 'stable',
        name: 'Configuration Industrial navigate',
        description: 'upward-trending open-source standardization',
        comment: 'enable Kansas analyzing',
        documentationURL: 'Mexico TCP open-source',
        startDate: '2021-11-04',
        endDate: '2021-11-04',
        applicationType: 'OTHER',
        softwareType: 'ON_PREMISE_CUSTOM',
        nickname: 'parallelism driver',
      },
    }).then(({ body }) => {
      application = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/application-components+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-components').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-components/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/applications', {
      statusCode: 200,
      body: [application],
    });

    cy.intercept('GET', '/api/application-categories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/technologies', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/external-references', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (applicationComponent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-components/${applicationComponent.id}`,
      }).then(() => {
        applicationComponent = undefined;
      });
    }
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

  it('ApplicationComponents menu should load ApplicationComponents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-component');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationComponent').should('exist');
    cy.url().should('match', applicationComponentPageUrlPattern);
  });

  describe('ApplicationComponent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationComponentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApplicationComponent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/application-component/new$'));
        cy.getEntityCreateUpdateHeading('ApplicationComponent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationComponentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/application-components',
          body: {
            ...applicationComponentSample,
            application: application,
          },
        }).then(({ body }) => {
          applicationComponent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/application-components+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [applicationComponent],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationComponentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ApplicationComponent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicationComponent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationComponentPageUrlPattern);
      });

      it('edit button click should load edit ApplicationComponent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationComponent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationComponentPageUrlPattern);
      });

      it('edit button click should load edit ApplicationComponent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationComponent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationComponentPageUrlPattern);
      });

      it('last delete button click should delete instance of ApplicationComponent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('applicationComponent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationComponentPageUrlPattern);

        applicationComponent = undefined;
      });
    });
  });

  describe('new ApplicationComponent page', () => {
    beforeEach(() => {
      cy.visit(`${applicationComponentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ApplicationComponent');
    });

    it('should create an instance of ApplicationComponent', () => {
      cy.get(`[data-cy="alias"]`).type('Licensed Bedfordshire').should('have.value', 'Licensed Bedfordshire');

      cy.get(`[data-cy="name"]`).type('hacking Fort homogeneous').should('have.value', 'hacking Fort homogeneous');

      cy.get(`[data-cy="description"]`).type('SMS optical').should('have.value', 'SMS optical');

      cy.get(`[data-cy="comment"]`).type('Liaison').should('have.value', 'Liaison');

      cy.get(`[data-cy="documentationURL"]`).type('1080p Chief').should('have.value', '1080p Chief');

      cy.get(`[data-cy="startDate"]`).type('2021-11-04').blur().should('have.value', '2021-11-04');

      cy.get(`[data-cy="endDate"]`).type('2021-11-04').blur().should('have.value', '2021-11-04');

      cy.get(`[data-cy="applicationType"]`).select('HARDWARE');

      cy.get(`[data-cy="softwareType"]`).select('CLOUD_SAAS');

      cy.get(`[data-cy="displayInLandscape"]`).should('not.be.checked');
      cy.get(`[data-cy="displayInLandscape"]`).click().should('be.checked');

      cy.get(`[data-cy="application"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        applicationComponent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationComponentPageUrlPattern);
    });
  });
});
