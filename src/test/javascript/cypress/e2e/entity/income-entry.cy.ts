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

describe('IncomeEntry e2e test', () => {
  const incomeEntryPageUrl = '/income-entry';
  const incomeEntryPageUrlPattern = new RegExp('/income-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const incomeEntrySample = {};

  let incomeEntry;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/income-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/income-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/income-entries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (incomeEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/income-entries/${incomeEntry.id}`,
      }).then(() => {
        incomeEntry = undefined;
      });
    }
  });

  it('IncomeEntries menu should load IncomeEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('income-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IncomeEntry').should('exist');
    cy.url().should('match', incomeEntryPageUrlPattern);
  });

  describe('IncomeEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(incomeEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IncomeEntry page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/income-entry/new$'));
        cy.getEntityCreateUpdateHeading('IncomeEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', incomeEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/income-entries',
          body: incomeEntrySample,
        }).then(({ body }) => {
          incomeEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/income-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/income-entries?page=0&size=20>; rel="last",<http://localhost/api/income-entries?page=0&size=20>; rel="first"',
              },
              body: [incomeEntry],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(incomeEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IncomeEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('incomeEntry');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', incomeEntryPageUrlPattern);
      });

      it('edit button click should load edit IncomeEntry page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IncomeEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', incomeEntryPageUrlPattern);
      });

      it('edit button click should load edit IncomeEntry page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IncomeEntry');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', incomeEntryPageUrlPattern);
      });

      it('last delete button click should delete instance of IncomeEntry', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('incomeEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', incomeEntryPageUrlPattern);

        incomeEntry = undefined;
      });
    });
  });

  describe('new IncomeEntry page', () => {
    beforeEach(() => {
      cy.visit(`${incomeEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IncomeEntry');
    });

    it('should create an instance of IncomeEntry', () => {
      cy.get(`[data-cy="branchCode"]`).type('dream');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'dream');

      cy.get(`[data-cy="branchId"]`).type('after blah although');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'after blah although');

      cy.get(`[data-cy="accountCode"]`).type('account');
      cy.get(`[data-cy="accountCode"]`).should('have.value', 'account');

      cy.get(`[data-cy="incomeCode"]`).type('than forceful where');
      cy.get(`[data-cy="incomeCode"]`).should('have.value', 'than forceful where');

      cy.get(`[data-cy="createdByUsername"]`).type('intrigue');
      cy.get(`[data-cy="createdByUsername"]`).should('have.value', 'intrigue');

      cy.get(`[data-cy="date"]`).type('2026-06-12');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2026-06-12');

      cy.get(`[data-cy="receiptNo"]`).type('membership');
      cy.get(`[data-cy="receiptNo"]`).should('have.value', 'membership');

      cy.get(`[data-cy="description"]`).type('solidly whose');
      cy.get(`[data-cy="description"]`).should('have.value', 'solidly whose');

      cy.get(`[data-cy="incomeType"]`).select('GIFT');

      cy.get(`[data-cy="amount"]`).type('31497.82');
      cy.get(`[data-cy="amount"]`).should('have.value', '31497.82');

      cy.get(`[data-cy="paymentMethod"]`).select('BANK');

      cy.get(`[data-cy="receivablePerson"]`).type('boohoo versus desecrate');
      cy.get(`[data-cy="receivablePerson"]`).should('have.value', 'boohoo versus desecrate');

      cy.get(`[data-cy="receivedBy"]`).type('spook metabolise eventually');
      cy.get(`[data-cy="receivedBy"]`).should('have.value', 'spook metabolise eventually');

      cy.get(`[data-cy="syncStatus"]`).select('SYNCED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        incomeEntry = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', incomeEntryPageUrlPattern);
    });
  });
});
