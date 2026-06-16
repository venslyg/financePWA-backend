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

describe('BudgetPlan e2e test', () => {
  const budgetPlanPageUrl = '/budget-plan';
  const budgetPlanPageUrlPattern = new RegExp('/budget-plan(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const budgetPlanSample = {};

  let budgetPlan;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/budget-plans+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/budget-plans').as('postEntityRequest');
    cy.intercept('DELETE', '/api/budget-plans/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (budgetPlan) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/budget-plans/${budgetPlan.id}`,
      }).then(() => {
        budgetPlan = undefined;
      });
    }
  });

  it('BudgetPlans menu should load BudgetPlans page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('budget-plan');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BudgetPlan').should('exist');
    cy.url().should('match', budgetPlanPageUrlPattern);
  });

  describe('BudgetPlan page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(budgetPlanPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BudgetPlan page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/budget-plan/new$'));
        cy.getEntityCreateUpdateHeading('BudgetPlan');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPlanPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/budget-plans',
          body: budgetPlanSample,
        }).then(({ body }) => {
          budgetPlan = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/budget-plans+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/budget-plans?page=0&size=20>; rel="last",<http://localhost/api/budget-plans?page=0&size=20>; rel="first"',
              },
              body: [budgetPlan],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(budgetPlanPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BudgetPlan page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('budgetPlan');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPlanPageUrlPattern);
      });

      it('edit button click should load edit BudgetPlan page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BudgetPlan');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPlanPageUrlPattern);
      });

      it('edit button click should load edit BudgetPlan page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BudgetPlan');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPlanPageUrlPattern);
      });

      it('last delete button click should delete instance of BudgetPlan', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('budgetPlan').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPlanPageUrlPattern);

        budgetPlan = undefined;
      });
    });
  });

  describe('new BudgetPlan page', () => {
    beforeEach(() => {
      cy.visit(`${budgetPlanPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BudgetPlan');
    });

    it('should create an instance of BudgetPlan', () => {
      cy.get(`[data-cy="branchCode"]`).type('plus er');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'plus er');

      cy.get(`[data-cy="branchId"]`).type('parade');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'parade');

      cy.get(`[data-cy="accountCode"]`).type('tricky degenerate lovingly');
      cy.get(`[data-cy="accountCode"]`).should('have.value', 'tricky degenerate lovingly');

      cy.get(`[data-cy="budgetPlanCode"]`).type('times aha');
      cy.get(`[data-cy="budgetPlanCode"]`).should('have.value', 'times aha');

      cy.get(`[data-cy="departmentName"]`).type('gee normal');
      cy.get(`[data-cy="departmentName"]`).should('have.value', 'gee normal');

      cy.get(`[data-cy="year"]`).type('16606');
      cy.get(`[data-cy="year"]`).should('have.value', '16606');

      cy.get(`[data-cy="allocatedAmount"]`).type('32288.01');
      cy.get(`[data-cy="allocatedAmount"]`).should('have.value', '32288.01');

      cy.get(`[data-cy="spentAmount"]`).type('26718.5');
      cy.get(`[data-cy="spentAmount"]`).should('have.value', '26718.5');

      cy.get(`[data-cy="remainingAmount"]`).type('18650.54');
      cy.get(`[data-cy="remainingAmount"]`).should('have.value', '18650.54');

      cy.get(`[data-cy="usedPercentage"]`).type('22552.52');
      cy.get(`[data-cy="usedPercentage"]`).should('have.value', '22552.52');

      cy.get(`[data-cy="alertStatus"]`).select('NEED_APPROVAL');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        budgetPlan = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', budgetPlanPageUrlPattern);
    });
  });
});
