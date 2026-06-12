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

describe('PettyCashLedger e2e test', () => {
  const pettyCashLedgerPageUrl = '/petty-cash-ledger';
  const pettyCashLedgerPageUrlPattern = new RegExp('/petty-cash-ledger(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pettyCashLedgerSample = {};

  let pettyCashLedger;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/petty-cash-ledgers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/petty-cash-ledgers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/petty-cash-ledgers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pettyCashLedger) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/petty-cash-ledgers/${pettyCashLedger.id}`,
      }).then(() => {
        pettyCashLedger = undefined;
      });
    }
  });

  it('PettyCashLedgers menu should load PettyCashLedgers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('petty-cash-ledger');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PettyCashLedger').should('exist');
    cy.url().should('match', pettyCashLedgerPageUrlPattern);
  });

  describe('PettyCashLedger page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pettyCashLedgerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PettyCashLedger page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/petty-cash-ledger/new$'));
        cy.getEntityCreateUpdateHeading('PettyCashLedger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pettyCashLedgerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/petty-cash-ledgers',
          body: pettyCashLedgerSample,
        }).then(({ body }) => {
          pettyCashLedger = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/petty-cash-ledgers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/petty-cash-ledgers?page=0&size=20>; rel="last",<http://localhost/api/petty-cash-ledgers?page=0&size=20>; rel="first"',
              },
              body: [pettyCashLedger],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pettyCashLedgerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PettyCashLedger page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pettyCashLedger');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pettyCashLedgerPageUrlPattern);
      });

      it('edit button click should load edit PettyCashLedger page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PettyCashLedger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pettyCashLedgerPageUrlPattern);
      });

      it('edit button click should load edit PettyCashLedger page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PettyCashLedger');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pettyCashLedgerPageUrlPattern);
      });

      it('last delete button click should delete instance of PettyCashLedger', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pettyCashLedger').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pettyCashLedgerPageUrlPattern);

        pettyCashLedger = undefined;
      });
    });
  });

  describe('new PettyCashLedger page', () => {
    beforeEach(() => {
      cy.visit(`${pettyCashLedgerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PettyCashLedger');
    });

    it('should create an instance of PettyCashLedger', () => {
      cy.get(`[data-cy="branchCode"]`).type('rear internationalize');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'rear internationalize');

      cy.get(`[data-cy="branchId"]`).type('at arrange');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'at arrange');

      cy.get(`[data-cy="pettyCashCode"]`).type('whoa');
      cy.get(`[data-cy="pettyCashCode"]`).should('have.value', 'whoa');

      cy.get(`[data-cy="date"]`).type('2026-06-12');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2026-06-12');

      cy.get(`[data-cy="pettyCashVoucherNo"]`).type('if');
      cy.get(`[data-cy="pettyCashVoucherNo"]`).should('have.value', 'if');

      cy.get(`[data-cy="description"]`).type('meanwhile accredit');
      cy.get(`[data-cy="description"]`).should('have.value', 'meanwhile accredit');

      cy.get(`[data-cy="cashIn"]`).type('3503.67');
      cy.get(`[data-cy="cashIn"]`).should('have.value', '3503.67');

      cy.get(`[data-cy="cashOut"]`).type('13907.65');
      cy.get(`[data-cy="cashOut"]`).should('have.value', '13907.65');

      cy.get(`[data-cy="runningBalance"]`).type('22183.52');
      cy.get(`[data-cy="runningBalance"]`).should('have.value', '22183.52');

      cy.get(`[data-cy="linkedAccountCode"]`).type('gripper');
      cy.get(`[data-cy="linkedAccountCode"]`).should('have.value', 'gripper');

      cy.get(`[data-cy="referenceNo"]`).type('fortunately loyally');
      cy.get(`[data-cy="referenceNo"]`).should('have.value', 'fortunately loyally');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pettyCashLedger = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pettyCashLedgerPageUrlPattern);
    });
  });
});
