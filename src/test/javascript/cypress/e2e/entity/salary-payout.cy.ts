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

describe('SalaryPayout e2e test', () => {
  const salaryPayoutPageUrl = '/salary-payout';
  const salaryPayoutPageUrlPattern = new RegExp('/salary-payout(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const salaryPayoutSample = {};

  let salaryPayout;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/salary-payouts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/salary-payouts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/salary-payouts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (salaryPayout) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/salary-payouts/${salaryPayout.id}`,
      }).then(() => {
        salaryPayout = undefined;
      });
    }
  });

  it('SalaryPayouts menu should load SalaryPayouts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('salary-payout');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SalaryPayout').should('exist');
    cy.url().should('match', salaryPayoutPageUrlPattern);
  });

  describe('SalaryPayout page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(salaryPayoutPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SalaryPayout page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/salary-payout/new$'));
        cy.getEntityCreateUpdateHeading('SalaryPayout');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salaryPayoutPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/salary-payouts',
          body: salaryPayoutSample,
        }).then(({ body }) => {
          salaryPayout = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/salary-payouts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/salary-payouts?page=0&size=20>; rel="last",<http://localhost/api/salary-payouts?page=0&size=20>; rel="first"',
              },
              body: [salaryPayout],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(salaryPayoutPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SalaryPayout page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('salaryPayout');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salaryPayoutPageUrlPattern);
      });

      it('edit button click should load edit SalaryPayout page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SalaryPayout');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salaryPayoutPageUrlPattern);
      });

      it('edit button click should load edit SalaryPayout page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SalaryPayout');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salaryPayoutPageUrlPattern);
      });

      it('last delete button click should delete instance of SalaryPayout', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('salaryPayout').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salaryPayoutPageUrlPattern);

        salaryPayout = undefined;
      });
    });
  });

  describe('new SalaryPayout page', () => {
    beforeEach(() => {
      cy.visit(`${salaryPayoutPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SalaryPayout');
    });

    it('should create an instance of SalaryPayout', () => {
      cy.get(`[data-cy="branchCode"]`).type('truthfully smoothly reclassify');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'truthfully smoothly reclassify');

      cy.get(`[data-cy="branchId"]`).type('searchingly weakly');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'searchingly weakly');

      cy.get(`[data-cy="salaryPayoutCode"]`).type('minty down psst');
      cy.get(`[data-cy="salaryPayoutCode"]`).should('have.value', 'minty down psst');

      cy.get(`[data-cy="staffCode"]`).type('formamide forenenst');
      cy.get(`[data-cy="staffCode"]`).should('have.value', 'formamide forenenst');

      cy.get(`[data-cy="payPeriod"]`).type('insignificant');
      cy.get(`[data-cy="payPeriod"]`).should('have.value', 'insignificant');

      cy.get(`[data-cy="baseSalary"]`).type('21745.43');
      cy.get(`[data-cy="baseSalary"]`).should('have.value', '21745.43');

      cy.get(`[data-cy="allowances"]`).type('29205.81');
      cy.get(`[data-cy="allowances"]`).should('have.value', '29205.81');

      cy.get(`[data-cy="deductions"]`).type('32057.52');
      cy.get(`[data-cy="deductions"]`).should('have.value', '32057.52');

      cy.get(`[data-cy="netPay"]`).type('11926.99');
      cy.get(`[data-cy="netPay"]`).should('have.value', '11926.99');

      cy.get(`[data-cy="payoutDate"]`).type('2026-06-11');
      cy.get(`[data-cy="payoutDate"]`).blur();
      cy.get(`[data-cy="payoutDate"]`).should('have.value', '2026-06-11');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        salaryPayout = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', salaryPayoutPageUrlPattern);
    });
  });
});
