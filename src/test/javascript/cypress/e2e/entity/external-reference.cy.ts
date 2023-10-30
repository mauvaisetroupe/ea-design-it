import { entityItemSelector } from '../../support/commands';
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

describe('ExternalReference e2e test', () => {
  const externalReferencePageUrl = '/external-reference';
  const externalReferencePageUrlPattern = new RegExp('/external-reference(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const externalReferenceSample = {};

  let externalReference: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/external-references+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/external-references').as('postEntityRequest');
    cy.intercept('DELETE', '/api/external-references/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (externalReference) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/external-references/${externalReference.id}`,
      }).then(() => {
        externalReference = undefined;
      });
    }
  });

  it('ExternalReferences menu should load ExternalReferences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('external-reference');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExternalReference').should('exist');
    cy.url().should('match', externalReferencePageUrlPattern);
  });

  describe('ExternalReference page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(externalReferencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExternalReference page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/external-reference/new$'));
        cy.getEntityCreateUpdateHeading('ExternalReference');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', externalReferencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/external-references',
          body: externalReferenceSample,
        }).then(({ body }) => {
          externalReference = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/external-references+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [externalReference],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(externalReferencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExternalReference page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('externalReference');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', externalReferencePageUrlPattern);
      });

      it('edit button click should load edit ExternalReference page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExternalReference');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', externalReferencePageUrlPattern);
      });

      it('last delete button click should delete instance of ExternalReference', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('externalReference').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', externalReferencePageUrlPattern);

        externalReference = undefined;
      });
    });
  });

  describe('new ExternalReference page', () => {
    beforeEach(() => {
      cy.visit(`${externalReferencePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExternalReference');
    });

    it('should create an instance of ExternalReference', () => {
      cy.get(`[data-cy="externalID"]`).type('green Licensed').should('have.value', 'green Licensed');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        externalReference = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', externalReferencePageUrlPattern);
    });
  });
});
