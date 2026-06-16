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

describe('ExpenseEntry e2e test', () => {
  const expenseEntryPageUrl = '/expense-entry';
  const expenseEntryPageUrlPattern = new RegExp('/expense-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const expenseEntrySample = {};

  let expenseEntry;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/expense-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/expense-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/expense-entries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (expenseEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/expense-entries/${expenseEntry.id}`,
      }).then(() => {
        expenseEntry = undefined;
      });
    }
  });

  it('ExpenseEntries menu should load ExpenseEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('expense-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExpenseEntry').should('exist');
    cy.url().should('match', expenseEntryPageUrlPattern);
  });

  describe('ExpenseEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(expenseEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExpenseEntry page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/expense-entry/new$'));
        cy.getEntityCreateUpdateHeading('ExpenseEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/expense-entries',
          body: expenseEntrySample,
        }).then(({ body }) => {
          expenseEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/expense-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/expense-entries?page=0&size=20>; rel="last",<http://localhost/api/expense-entries?page=0&size=20>; rel="first"',
              },
              body: [expenseEntry],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(expenseEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExpenseEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('expenseEntry');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseEntryPageUrlPattern);
      });

      it('edit button click should load edit ExpenseEntry page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExpenseEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseEntryPageUrlPattern);
      });

      it('edit button click should load edit ExpenseEntry page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExpenseEntry');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseEntryPageUrlPattern);
      });

      it('last delete button click should delete instance of ExpenseEntry', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('expenseEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseEntryPageUrlPattern);

        expenseEntry = undefined;
      });
    });
  });

  describe('new ExpenseEntry page', () => {
    beforeEach(() => {
      cy.visit(`${expenseEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExpenseEntry');
    });

    it('should create an instance of ExpenseEntry', () => {
      cy.get(`[data-cy="branchCode"]`).type('absentmindedly knowledgeably');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'absentmindedly knowledgeably');

      cy.get(`[data-cy="branchId"]`).type('summary fraudster');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'summary fraudster');

      cy.get(`[data-cy="accountCode"]`).type('yowza');
      cy.get(`[data-cy="accountCode"]`).should('have.value', 'yowza');

      cy.get(`[data-cy="expenseCode"]`).type('yuck however');
      cy.get(`[data-cy="expenseCode"]`).should('have.value', 'yuck however');

      cy.get(`[data-cy="expenseCategoryCode"]`).type('stoop inwardly');
      cy.get(`[data-cy="expenseCategoryCode"]`).should('have.value', 'stoop inwardly');

      cy.get(`[data-cy="expenseSubCategoryCode"]`).type('faraway cautiously formula');
      cy.get(`[data-cy="expenseSubCategoryCode"]`).should('have.value', 'faraway cautiously formula');

      cy.get(`[data-cy="createdByUsername"]`).type('who powerfully');
      cy.get(`[data-cy="createdByUsername"]`).should('have.value', 'who powerfully');

      cy.get(`[data-cy="date"]`).type('2026-06-11');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2026-06-11');

      cy.get(`[data-cy="voucherNo"]`).type('when');
      cy.get(`[data-cy="voucherNo"]`).should('have.value', 'when');

      cy.get(`[data-cy="description"]`).type('gasp amongst');
      cy.get(`[data-cy="description"]`).should('have.value', 'gasp amongst');

      cy.get(`[data-cy="amount"]`).type('20028.51');
      cy.get(`[data-cy="amount"]`).should('have.value', '20028.51');

      cy.get(`[data-cy="paymentMode"]`).select('CHEQUE');

      cy.get(`[data-cy="approvalStatus"]`).select('TO_REVIEW');

      cy.get(`[data-cy="approvedBy"]`).type('apud');
      cy.get(`[data-cy="approvedBy"]`).should('have.value', 'apud');

      cy.get(`[data-cy="vendor"]`).type('brr throughout redact');
      cy.get(`[data-cy="vendor"]`).should('have.value', 'brr throughout redact');

      cy.get(`[data-cy="syncStatus"]`).select('PENDING_OFFLINE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        expenseEntry = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', expenseEntryPageUrlPattern);
    });
  });
});
