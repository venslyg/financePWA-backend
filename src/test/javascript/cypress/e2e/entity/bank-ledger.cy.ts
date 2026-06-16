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

describe('BankLedger e2e test', () => {
  const bankLedgerPageUrl = '/bank-ledger';
  const bankLedgerPageUrlPattern = new RegExp('/bank-ledger(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bankLedgerSample = {};

  let bankLedger;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bank-ledgers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bank-ledgers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bank-ledgers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bankLedger) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-ledgers/${bankLedger.id}`,
      }).then(() => {
        bankLedger = undefined;
      });
    }
  });

  it('BankLedgers menu should load BankLedgers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bank-ledger');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BankLedger').should('exist');
    cy.url().should('match', bankLedgerPageUrlPattern);
  });

  describe('BankLedger page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bankLedgerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BankLedger page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bank-ledger/new$'));
        cy.getEntityCreateUpdateHeading('BankLedger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankLedgerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bank-ledgers',
          body: bankLedgerSample,
        }).then(({ body }) => {
          bankLedger = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bank-ledgers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/bank-ledgers?page=0&size=20>; rel="last",<http://localhost/api/bank-ledgers?page=0&size=20>; rel="first"',
              },
              body: [bankLedger],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bankLedgerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BankLedger page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bankLedger');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankLedgerPageUrlPattern);
      });

      it('edit button click should load edit BankLedger page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BankLedger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankLedgerPageUrlPattern);
      });

      it('edit button click should load edit BankLedger page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BankLedger');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankLedgerPageUrlPattern);
      });

      it('last delete button click should delete instance of BankLedger', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bankLedger').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankLedgerPageUrlPattern);

        bankLedger = undefined;
      });
    });
  });

  describe('new BankLedger page', () => {
    beforeEach(() => {
      cy.visit(`${bankLedgerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BankLedger');
    });

    it('should create an instance of BankLedger', () => {
      cy.get(`[data-cy="branchCode"]`).type('brightly only unfortunately');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'brightly only unfortunately');

      cy.get(`[data-cy="branchId"]`).type('finally notwithstanding mispronounce');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'finally notwithstanding mispronounce');

      cy.get(`[data-cy="bankLedgerCode"]`).type('popularity spring rewrite');
      cy.get(`[data-cy="bankLedgerCode"]`).should('have.value', 'popularity spring rewrite');

      cy.get(`[data-cy="date"]`).type('2026-06-12');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2026-06-12');

      cy.get(`[data-cy="referenceNo"]`).type('inasmuch poor');
      cy.get(`[data-cy="referenceNo"]`).should('have.value', 'inasmuch poor');

      cy.get(`[data-cy="description"]`).type('conjecture');
      cy.get(`[data-cy="description"]`).should('have.value', 'conjecture');

      cy.get(`[data-cy="depositAmount"]`).type('21956.83');
      cy.get(`[data-cy="depositAmount"]`).should('have.value', '21956.83');

      cy.get(`[data-cy="withdrawalAmount"]`).type('18193.98');
      cy.get(`[data-cy="withdrawalAmount"]`).should('have.value', '18193.98');

      cy.get(`[data-cy="runningBalance"]`).type('21741.12');
      cy.get(`[data-cy="runningBalance"]`).should('have.value', '21741.12');

      cy.get(`[data-cy="remark"]`).type('slow able');
      cy.get(`[data-cy="remark"]`).should('have.value', 'slow able');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        bankLedger = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', bankLedgerPageUrlPattern);
    });
  });
});
