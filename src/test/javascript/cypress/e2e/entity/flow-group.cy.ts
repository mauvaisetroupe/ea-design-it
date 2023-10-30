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

describe('FlowGroup e2e test', () => {
  const flowGroupPageUrl = '/flow-group';
  const flowGroupPageUrlPattern = new RegExp('/flow-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  // const flowGroupSample = {};

  let flowGroup;
  // let functionalFlowStep;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/functional-flow-steps',
      body: {"description":"peak","stepOrder":19159},
    }).then(({ body }) => {
      functionalFlowStep = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/flow-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/flow-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/flow-groups/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/functional-flows', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/functional-flow-steps', {
      statusCode: 200,
      body: [functionalFlowStep],
    });

  });
   */

  afterEach(() => {
    if (flowGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/flow-groups/${flowGroup.id}`,
      }).then(() => {
        flowGroup = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (functionalFlowStep) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/functional-flow-steps/${functionalFlowStep.id}`,
      }).then(() => {
        functionalFlowStep = undefined;
      });
    }
  });
   */

  it('FlowGroups menu should load FlowGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FlowGroup').should('exist');
    cy.url().should('match', flowGroupPageUrlPattern);
  });

  describe('FlowGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(flowGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FlowGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/flow-group/new$'));
        cy.getEntityCreateUpdateHeading('FlowGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/flow-groups',
          body: {
            ...flowGroupSample,
            steps: functionalFlowStep,
          },
        }).then(({ body }) => {
          flowGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/flow-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [flowGroup],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(flowGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(flowGroupPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details FlowGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('flowGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });

      it('edit button click should load edit FlowGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });

      it('edit button click should load edit FlowGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FlowGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of FlowGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('flowGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', flowGroupPageUrlPattern);

        flowGroup = undefined;
      });
    });
  });

  describe('new FlowGroup page', () => {
    beforeEach(() => {
      cy.visit(`${flowGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FlowGroup');
    });

    it.skip('should create an instance of FlowGroup', () => {
      cy.get(`[data-cy="title"]`).type('bashfully deposit pace');
      cy.get(`[data-cy="title"]`).should('have.value', 'bashfully deposit pace');

      cy.get(`[data-cy="url"]`).type('https://same-indigence.net/');
      cy.get(`[data-cy="url"]`).should('have.value', 'https://same-indigence.net/');

      cy.get(`[data-cy="description"]`).type('incidentally permeate');
      cy.get(`[data-cy="description"]`).should('have.value', 'incidentally permeate');

      cy.get(`[data-cy="steps"]`).select([0]);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        flowGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', flowGroupPageUrlPattern);
    });
  });
});
