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

describe('AssetDepreciationHistory e2e test', () => {
  const assetDepreciationHistoryPageUrl = '/asset-depreciation-history';
  const assetDepreciationHistoryPageUrlPattern = new RegExp('/asset-depreciation-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetDepreciationHistorySample = {};

  let assetDepreciationHistory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-depreciation-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-depreciation-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-depreciation-histories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetDepreciationHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-depreciation-histories/${assetDepreciationHistory.id}`,
      }).then(() => {
        assetDepreciationHistory = undefined;
      });
    }
  });

  it('AssetDepreciationHistories menu should load AssetDepreciationHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-depreciation-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetDepreciationHistory').should('exist');
    cy.url().should('match', assetDepreciationHistoryPageUrlPattern);
  });

  describe('AssetDepreciationHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetDepreciationHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetDepreciationHistory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-depreciation-history/new$'));
        cy.getEntityCreateUpdateHeading('AssetDepreciationHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDepreciationHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-depreciation-histories',
          body: assetDepreciationHistorySample,
        }).then(({ body }) => {
          assetDepreciationHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-depreciation-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-depreciation-histories?page=0&size=20>; rel="last",<http://localhost/api/asset-depreciation-histories?page=0&size=20>; rel="first"',
              },
              body: [assetDepreciationHistory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetDepreciationHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetDepreciationHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetDepreciationHistory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDepreciationHistoryPageUrlPattern);
      });

      it('edit button click should load edit AssetDepreciationHistory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetDepreciationHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDepreciationHistoryPageUrlPattern);
      });

      it('edit button click should load edit AssetDepreciationHistory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetDepreciationHistory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDepreciationHistoryPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetDepreciationHistory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetDepreciationHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDepreciationHistoryPageUrlPattern);

        assetDepreciationHistory = undefined;
      });
    });
  });

  describe('new AssetDepreciationHistory page', () => {
    beforeEach(() => {
      cy.visit(`${assetDepreciationHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetDepreciationHistory');
    });

    it('should create an instance of AssetDepreciationHistory', () => {
      cy.get(`[data-cy="branchCode"]`).type('agreement concrete');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'agreement concrete');

      cy.get(`[data-cy="branchId"]`).type('righteously after');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'righteously after');

      cy.get(`[data-cy="assetRegisterCode"]`).type('assured cantaloupe amidst');
      cy.get(`[data-cy="assetRegisterCode"]`).should('have.value', 'assured cantaloupe amidst');

      cy.get(`[data-cy="depreciationDate"]`).type('2026-06-12');
      cy.get(`[data-cy="depreciationDate"]`).blur();
      cy.get(`[data-cy="depreciationDate"]`).should('have.value', '2026-06-12');

      cy.get(`[data-cy="depreciationAmount"]`).type('8275.14');
      cy.get(`[data-cy="depreciationAmount"]`).should('have.value', '8275.14');

      cy.get(`[data-cy="valueAfterDepreciation"]`).type('16860.89');
      cy.get(`[data-cy="valueAfterDepreciation"]`).should('have.value', '16860.89');

      cy.get(`[data-cy="processedBy"]`).type('brr');
      cy.get(`[data-cy="processedBy"]`).should('have.value', 'brr');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        assetDepreciationHistory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', assetDepreciationHistoryPageUrlPattern);
    });
  });
});
