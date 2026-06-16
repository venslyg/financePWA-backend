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

describe('ExpenseSubCategory e2e test', () => {
  const expenseSubCategoryPageUrl = '/expense-sub-category';
  const expenseSubCategoryPageUrlPattern = new RegExp('/expense-sub-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const expenseSubCategorySample = {};

  let expenseSubCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/expense-sub-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/expense-sub-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/expense-sub-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (expenseSubCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/expense-sub-categories/${expenseSubCategory.id}`,
      }).then(() => {
        expenseSubCategory = undefined;
      });
    }
  });

  it('ExpenseSubCategories menu should load ExpenseSubCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('expense-sub-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExpenseSubCategory').should('exist');
    cy.url().should('match', expenseSubCategoryPageUrlPattern);
  });

  describe('ExpenseSubCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(expenseSubCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExpenseSubCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/expense-sub-category/new$'));
        cy.getEntityCreateUpdateHeading('ExpenseSubCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseSubCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/expense-sub-categories',
          body: expenseSubCategorySample,
        }).then(({ body }) => {
          expenseSubCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/expense-sub-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/expense-sub-categories?page=0&size=20>; rel="last",<http://localhost/api/expense-sub-categories?page=0&size=20>; rel="first"',
              },
              body: [expenseSubCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(expenseSubCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExpenseSubCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('expenseSubCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseSubCategoryPageUrlPattern);
      });

      it('edit button click should load edit ExpenseSubCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExpenseSubCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseSubCategoryPageUrlPattern);
      });

      it('edit button click should load edit ExpenseSubCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExpenseSubCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseSubCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of ExpenseSubCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('expenseSubCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', expenseSubCategoryPageUrlPattern);

        expenseSubCategory = undefined;
      });
    });
  });

  describe('new ExpenseSubCategory page', () => {
    beforeEach(() => {
      cy.visit(`${expenseSubCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExpenseSubCategory');
    });

    it('should create an instance of ExpenseSubCategory', () => {
      cy.get(`[data-cy="branchCode"]`).type('calmly cap');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'calmly cap');

      cy.get(`[data-cy="branchId"]`).type('considering entice um');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'considering entice um');

      cy.get(`[data-cy="categoryCode"]`).type('mid');
      cy.get(`[data-cy="categoryCode"]`).should('have.value', 'mid');

      cy.get(`[data-cy="subCategoryCode"]`).type('hippodrome ack');
      cy.get(`[data-cy="subCategoryCode"]`).should('have.value', 'hippodrome ack');

      cy.get(`[data-cy="subCategoryName"]`).type('exaggerate tuba next');
      cy.get(`[data-cy="subCategoryName"]`).should('have.value', 'exaggerate tuba next');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        expenseSubCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', expenseSubCategoryPageUrlPattern);
    });
  });
});
