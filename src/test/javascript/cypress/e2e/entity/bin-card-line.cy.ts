import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('BinCardLine e2e test', () => {
  const binCardLinePageUrl = '/bin-card-line';
  const binCardLinePageUrlPattern = new RegExp('/bin-card-line(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const binCardLineSample = {};

  let binCardLine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bin-card-lines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bin-card-lines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bin-card-lines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (binCardLine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bin-card-lines/${binCardLine.id}`,
      }).then(() => {
        binCardLine = undefined;
      });
    }
  });

  it('BinCardLines menu should load BinCardLines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bin-card-line');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BinCardLine').should('exist');
    cy.url().should('match', binCardLinePageUrlPattern);
  });

  describe('BinCardLine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(binCardLinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BinCardLine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bin-card-line/new$'));
        cy.getEntityCreateUpdateHeading('BinCardLine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', binCardLinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bin-card-lines',
          body: binCardLineSample,
        }).then(({ body }) => {
          binCardLine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bin-card-lines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/bin-card-lines?page=0&size=20>; rel="last",<http://localhost/api/bin-card-lines?page=0&size=20>; rel="first"',
              },
              body: [binCardLine],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(binCardLinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BinCardLine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('binCardLine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', binCardLinePageUrlPattern);
      });

      it('edit button click should load edit BinCardLine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BinCardLine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', binCardLinePageUrlPattern);
      });

      it('edit button click should load edit BinCardLine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BinCardLine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', binCardLinePageUrlPattern);
      });

      it('last delete button click should delete instance of BinCardLine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('binCardLine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', binCardLinePageUrlPattern);

        binCardLine = undefined;
      });
    });
  });

  describe('new BinCardLine page', () => {
    beforeEach(() => {
      cy.visit(`${binCardLinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BinCardLine');
    });

    it('should create an instance of BinCardLine', () => {
      cy.get(`[data-cy="branchCode"]`).type('confused bony apud');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'confused bony apud');

      cy.get(`[data-cy="branchId"]`).type('boastfully rigid');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'boastfully rigid');

      cy.get(`[data-cy="inventoryItemCode"]`).type('regarding');
      cy.get(`[data-cy="inventoryItemCode"]`).should('have.value', 'regarding');

      cy.get(`[data-cy="date"]`).type('2026-06-11');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2026-06-11');

      cy.get(`[data-cy="referenceNo"]`).type('cautiously custody address');
      cy.get(`[data-cy="referenceNo"]`).should('have.value', 'cautiously custody address');

      cy.get(`[data-cy="description"]`).type('and resource');
      cy.get(`[data-cy="description"]`).should('have.value', 'and resource');

      cy.get(`[data-cy="quantityIn"]`).type('9720.84');
      cy.get(`[data-cy="quantityIn"]`).should('have.value', '9720.84');

      cy.get(`[data-cy="quantityOut"]`).type('12036.78');
      cy.get(`[data-cy="quantityOut"]`).should('have.value', '12036.78');

      cy.get(`[data-cy="runningBalance"]`).type('18007.23');
      cy.get(`[data-cy="runningBalance"]`).should('have.value', '18007.23');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        binCardLine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', binCardLinePageUrlPattern);
    });
  });
});
