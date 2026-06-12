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

describe('LiabilityLog e2e test', () => {
  const liabilityLogPageUrl = '/liability-log';
  const liabilityLogPageUrlPattern = new RegExp('/liability-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const liabilityLogSample = {};

  let liabilityLog;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/liability-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/liability-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/liability-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (liabilityLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/liability-logs/${liabilityLog.id}`,
      }).then(() => {
        liabilityLog = undefined;
      });
    }
  });

  it('LiabilityLogs menu should load LiabilityLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('liability-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LiabilityLog').should('exist');
    cy.url().should('match', liabilityLogPageUrlPattern);
  });

  describe('LiabilityLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(liabilityLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LiabilityLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/liability-log/new$'));
        cy.getEntityCreateUpdateHeading('LiabilityLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/liability-logs',
          body: liabilityLogSample,
        }).then(({ body }) => {
          liabilityLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/liability-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/liability-logs?page=0&size=20>; rel="last",<http://localhost/api/liability-logs?page=0&size=20>; rel="first"',
              },
              body: [liabilityLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(liabilityLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LiabilityLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('liabilityLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityLogPageUrlPattern);
      });

      it('edit button click should load edit LiabilityLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LiabilityLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityLogPageUrlPattern);
      });

      it('edit button click should load edit LiabilityLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LiabilityLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityLogPageUrlPattern);
      });

      it('last delete button click should delete instance of LiabilityLog', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('liabilityLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityLogPageUrlPattern);

        liabilityLog = undefined;
      });
    });
  });

  describe('new LiabilityLog page', () => {
    beforeEach(() => {
      cy.visit(`${liabilityLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LiabilityLog');
    });

    it('should create an instance of LiabilityLog', () => {
      cy.get(`[data-cy="branchCode"]`).type('before meadow unlike');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'before meadow unlike');

      cy.get(`[data-cy="liabilityCode"]`).type('close');
      cy.get(`[data-cy="liabilityCode"]`).should('have.value', 'close');

      cy.get(`[data-cy="loanFrom"]`).type('circa woot stump');
      cy.get(`[data-cy="loanFrom"]`).should('have.value', 'circa woot stump');

      cy.get(`[data-cy="description"]`).type('depart breastplate ack');
      cy.get(`[data-cy="description"]`).should('have.value', 'depart breastplate ack');

      cy.get(`[data-cy="liabilityType"]`).select('LONG_TERM');

      cy.get(`[data-cy="totalLoanAmount"]`).type('19465.16');
      cy.get(`[data-cy="totalLoanAmount"]`).should('have.value', '19465.16');

      cy.get(`[data-cy="startDate"]`).type('2026-06-12');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-06-12');

      cy.get(`[data-cy="endDate"]`).type('2026-06-12');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2026-06-12');

      cy.get(`[data-cy="interestPercentage"]`).type('31038.05');
      cy.get(`[data-cy="interestPercentage"]`).should('have.value', '31038.05');

      cy.get(`[data-cy="monthlyPaymentAmount"]`).type('16627.89');
      cy.get(`[data-cy="monthlyPaymentAmount"]`).should('have.value', '16627.89');

      cy.get(`[data-cy="principalPaid"]`).type('21954.18');
      cy.get(`[data-cy="principalPaid"]`).should('have.value', '21954.18');

      cy.get(`[data-cy="balanceToPay"]`).type('20442.77');
      cy.get(`[data-cy="balanceToPay"]`).should('have.value', '20442.77');

      cy.get(`[data-cy="status"]`).select('DECLINED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        liabilityLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', liabilityLogPageUrlPattern);
    });
  });
});
