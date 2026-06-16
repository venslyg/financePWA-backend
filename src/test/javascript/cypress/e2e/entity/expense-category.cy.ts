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

describe('ExpenseCategory e2e test', () => {
  const expenseCategoryPageUrl = '/expense-category';
  const expenseCategoryPageUrlPattern = new RegExp('/expense-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const expenseCategorySample = {};

  let expenseCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/expense-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/expense-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/expense-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (expenseCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/expense-categories/${expenseCategory.id}`,
      }).then(() => {
        expenseCategory = undefined;
      });
    }
  });

  it('ExpenseCategories menu should load ExpenseCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('expense-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExpenseCategory').should('exist');
    cy.url().should('match', expenseCategoryPageUrlPattern);
  });

  describe('ExpenseCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(expenseCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExpenseCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/expense-category/new$'));
        cy.getEntityCreateUpdateHeading('ExpenseCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/expense-categories',
          body: expenseCategorySample,
        }).then(({ body }) => {
          expenseCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/expense-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/expense-categories?page=0&size=20>; rel="last",<http://localhost/api/expense-categories?page=0&size=20>; rel="first"',
              },
              body: [expenseCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(expenseCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExpenseCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('expenseCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseCategoryPageUrlPattern);
      });

      it('edit button click should load edit ExpenseCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExpenseCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseCategoryPageUrlPattern);
      });

      it('edit button click should load edit ExpenseCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExpenseCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of ExpenseCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('expenseCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseCategoryPageUrlPattern);

        expenseCategory = undefined;
      });
    });
  });

  describe('new ExpenseCategory page', () => {
    beforeEach(() => {
      cy.visit(`${expenseCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExpenseCategory');
    });

    it('should create an instance of ExpenseCategory', () => {
      cy.get(`[data-cy="branchCode"]`).type('before');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'before');

      cy.get(`[data-cy="branchId"]`).type('incidentally fortunately swath');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'incidentally fortunately swath');

      cy.get(`[data-cy="categoryCode"]`).type('less');
      cy.get(`[data-cy="categoryCode"]`).should('have.value', 'less');

      cy.get(`[data-cy="categoryName"]`).type('if excitedly vamoose');
      cy.get(`[data-cy="categoryName"]`).should('have.value', 'if excitedly vamoose');

      cy.get(`[data-cy="description"]`).type('recommendation');
      cy.get(`[data-cy="description"]`).should('have.value', 'recommendation');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        expenseCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', expenseCategoryPageUrlPattern);
    });
  });
});
