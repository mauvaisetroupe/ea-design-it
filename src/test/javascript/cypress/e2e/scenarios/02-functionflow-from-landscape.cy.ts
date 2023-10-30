import { entityEditButtonSelector, entityDetailsButtonSelector } from '../../support/entity';

const uniqueID = Date.now();
const landscapeAlias = 'LANDSCAPE-' + uniqueID;
const appli1 = 'MY-APPLI1-' + uniqueID;
const appli2 = 'MY-APPLI2-' + uniqueID;
const functionalFlowAlias = 'FUNC.001-' + uniqueID;
const inter1 = 'INTER-1-' + uniqueID;
const inter2 = 'INTER-2-' + uniqueID;
const inter3 = 'INTER-3-' + uniqueID;
const username = Cypress.env('E2E_USERNAME') ?? 'admin';
const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

describe('Create a new Landscape, a new Functional flow from landscape with 2 specs', () => {
  beforeEach(() => {
    cy.login(username, password);
    cy.intercept('GET', '/api/plantuml/landscape-view/get-svg/*').as('plantumlRequest');
    cy.intercept('POST', '/api/import/flow/sequence-diagram/save*').as('plantumlSave');
    cy.intercept('GET', '/api/landscape-views/*').as('getLandscape');
  });

  it('Create new landscape', function () {
    cy.visit('/landscape-view');
    cy.get('[data-cy="entityCreateButton"] > span').click();
    cy.get('[data-cy="viewpoint"]').select('APPLICATION_LANDSCAPE');
    cy.get('[data-cy="diagramName"]').clear();
    cy.get('[data-cy="diagramName"]').type(landscapeAlias);
    cy.get('[data-cy="entityCreateSaveButton"] > span').click();
  });

  it('create 2 applications', function () {
    cy.visit('/application');
    cy.get('[data-cy="entityCreateButton"] > span').click();
    cy.get('[data-cy="alias"]').clear();
    cy.get('[data-cy="alias"]').type(appli1);
    cy.get('[data-cy="name"]').clear();
    cy.get('[data-cy="name"]').type(appli1);
    cy.get('[data-cy="entityCreateSaveButton"] > span').click();
    cy.visit('localhost:8080/application');
    cy.get('[data-cy="entityCreateButton"] > span').click();
    cy.get('[data-cy="alias"]').clear();
    cy.get('[data-cy="alias"]').type(appli2);
    cy.get('[data-cy="name"]').clear();
    cy.get('[data-cy="name"]').type(appli2);
    cy.get('[data-cy="entityCreateSaveButton"] > span').click();
  });

  it('Create new function flow from landscape page', function () {
    cy.visit('/landscape-view');
    cy.get('[data-cy="entityDetailsButton-' + landscapeAlias + '"]').click();

    cy.get('#tab-flows___BV_tab_button__').click();

    cy.get('[data-cy="entityCreateButton"] > span').click();
    cy.get('[data-cy="alias"]').clear();
    cy.get('[data-cy="alias"]').type(functionalFlowAlias);
    cy.get('[data-cy="description"]').clear();
    cy.get('[data-cy="description"]').type('My-Functional-Flow');
    cy.get('[data-cy="comment"]').clear();
    cy.get('[data-cy="comment"]').type('My-Comment');

    // TAB2
    cy.get('a[role="tab"]').eq(1).click();
    const txtarea = cy.get('textarea');
    txtarea.click();
    txtarea.type('"' + appli1 + '" --> "' + appli2 + '" : step1 // API\n');
    txtarea.type('"' + appli2 + '" --> "' + appli1 + '" : step2 // API');
    cy.get('[data-cy="submit"]').click();

    cy.wait('@plantumlRequest').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    // TAB3
    cy.get('a[role="tab"]').eq(2).click();

    cy.get('tbody > :nth-child(1) > :nth-child(7) > input').clear();
    cy.get('tbody > :nth-child(1) > :nth-child(7) > input').type(inter1);
    cy.get(':nth-child(2) > :nth-child(7) > input').clear();
    cy.get(':nth-child(2) > :nth-child(7) > input').type(inter2);

    cy.get('[data-cy="entityCreateSaveButton"] > span').click();

    cy.wait('@plantumlSave').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.wait('@getLandscape').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.get('#tab-flows___BV_tab_button__').click();
    cy.get('[data-cy="entityDetailsButton"] > .svg-inline--fa').eq(0).click();
    cy.get('#tab-steps___BV_tab_button__').click();

    cy.contains('step1');
    cy.contains('step2');
  });
});

describe('Edit created Functional Flow and add a 3nd step', () => {
  beforeEach(() => {
    cy.login(username, password);
    cy.intercept('GET', '/api/plantuml/landscape-view/get-svg/*').as('plantumlRequest');
    cy.intercept('POST', '/api/import/flow/sequence-diagram/save*').as('plantumlSave');
    cy.intercept('GET', '/api/landscape-views/*').as('getLandscape');
    cy.intercept('GET', '/api/functional-flows').as('getFlows');
  });

  it('Find and edit created functional flow', function () {
    cy.visit('/functional-flow');

    cy.wait('@getFlows').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.contains('td', functionalFlowAlias)
      .parents('tr')
      .within(() => {
        // Click the 'View' button within this specific row
        cy.get(entityEditButtonSelector).click();
      });

    // TAB2
    cy.get('a[role="tab"]').eq(1).click();
    const txtarea = cy.get('textarea');
    txtarea.click();
    txtarea.type('"' + appli1 + '" --> "' + appli1 + '" : step3 // API');
    cy.get('[data-cy="submit"]').click();

    // TAB3
    cy.get('a[role="tab"]').eq(2).click();
    cy.get(':nth-child(3) > :nth-child(7) > input').clear();
    cy.get(':nth-child(3) > :nth-child(7) > input').type(inter3);

    cy.get('[data-cy="entityCreateSaveButton"] > span').click();

    cy.wait('@plantumlSave').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
  });

  it('Find and created functional flow and check 3 steps', function () {
    cy.visit('/functional-flow');

    cy.wait('@getFlows').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.contains('td', functionalFlowAlias)
      .parents('tr')
      .within(() => {
        // Click the 'View' button within this specific row
        cy.get(entityDetailsButtonSelector).click();
      });

    // should contain step 3
    cy.contains('step1');
    cy.contains('step2');
    cy.contains('step3');
  });
});
