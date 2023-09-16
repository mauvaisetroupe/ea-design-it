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

describe('CapabilityApplicationMapping e2e test', () => {
  const capabilityApplicationMappingPageUrl = '/capability-application-mapping';
  const capabilityApplicationMappingPageUrlPattern = new RegExp('/capability-application-mapping(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const capabilityApplicationMappingSample = {};

  let capabilityApplicationMapping: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/capability-application-mappings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/capability-application-mappings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/capability-application-mappings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (capabilityApplicationMapping) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/capability-application-mappings/${capabilityApplicationMapping.id}`,
      }).then(() => {
        capabilityApplicationMapping = undefined;
      });
    }
  });

  it('CapabilityApplicationMappings menu should load CapabilityApplicationMappings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('capability-application-mapping');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CapabilityApplicationMapping').should('exist');
    cy.url().should('match', capabilityApplicationMappingPageUrlPattern);
  });

  describe('CapabilityApplicationMapping page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(capabilityApplicationMappingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CapabilityApplicationMapping page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/capability-application-mapping/new$'));
        cy.getEntityCreateUpdateHeading('CapabilityApplicationMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityApplicationMappingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/capability-application-mappings',
          body: capabilityApplicationMappingSample,
        }).then(({ body }) => {
          capabilityApplicationMapping = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/capability-application-mappings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [capabilityApplicationMapping],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(capabilityApplicationMappingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CapabilityApplicationMapping page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('capabilityApplicationMapping');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityApplicationMappingPageUrlPattern);
      });

      it('edit button click should load edit CapabilityApplicationMapping page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CapabilityApplicationMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityApplicationMappingPageUrlPattern);
      });

      it('last delete button click should delete instance of CapabilityApplicationMapping', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('capabilityApplicationMapping').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', capabilityApplicationMappingPageUrlPattern);

        capabilityApplicationMapping = undefined;
      });
    });
  });

  describe('new CapabilityApplicationMapping page', () => {
    beforeEach(() => {
      cy.visit(`${capabilityApplicationMappingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CapabilityApplicationMapping');
    });

    it('should create an instance of CapabilityApplicationMapping', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        capabilityApplicationMapping = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', capabilityApplicationMappingPageUrlPattern);
    });
  });
});
