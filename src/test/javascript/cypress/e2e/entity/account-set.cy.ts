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

describe('AccountSet e2e test', () => {
  const accountSetPageUrl = '/account-set';
  const accountSetPageUrlPattern = new RegExp('/account-set(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accountSetSample = {};

  let accountSet;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-sets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-sets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-sets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountSet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-sets/${accountSet.id}`,
      }).then(() => {
        accountSet = undefined;
      });
    }
  });

  it('AccountSets menu should load AccountSets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-set');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountSet').should('exist');
    cy.url().should('match', accountSetPageUrlPattern);
  });

  describe('AccountSet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountSetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountSet page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/account-set/new$'));
        cy.getEntityCreateUpdateHeading('AccountSet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountSetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/account-sets',
          body: accountSetSample,
        }).then(({ body }) => {
          accountSet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/account-sets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/account-sets?page=0&size=20>; rel="last",<http://localhost/api/account-sets?page=0&size=20>; rel="first"',
              },
              body: [accountSet],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountSetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountSet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountSet');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountSetPageUrlPattern);
      });

      it('edit button click should load edit AccountSet page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountSet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountSetPageUrlPattern);
      });

      it('edit button click should load edit AccountSet page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountSet');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountSetPageUrlPattern);
      });

      it('last delete button click should delete instance of AccountSet', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountSet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountSetPageUrlPattern);

        accountSet = undefined;
      });
    });
  });

  describe('new AccountSet page', () => {
    beforeEach(() => {
      cy.visit(`${accountSetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccountSet');
    });

    it('should create an instance of AccountSet', () => {
      cy.get(`[data-cy="branchCode"]`).type('on');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'on');

      cy.get(`[data-cy="branchId"]`).type('hm emotional during');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'hm emotional during');

      cy.get(`[data-cy="accountCode"]`).type('necklace');
      cy.get(`[data-cy="accountCode"]`).should('have.value', 'necklace');

      cy.get(`[data-cy="accountName"]`).type('Savings Account');
      cy.get(`[data-cy="accountName"]`).should('have.value', 'Savings Account');

      cy.get(`[data-cy="accountType"]`).select('INCOME');

      cy.get(`[data-cy="subCategory"]`).type('sheepishly pension');
      cy.get(`[data-cy="subCategory"]`).should('have.value', 'sheepishly pension');

      cy.get(`[data-cy="remark"]`).type('ick shoot until');
      cy.get(`[data-cy="remark"]`).should('have.value', 'ick shoot until');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accountSet = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accountSetPageUrlPattern);
    });
  });
});
