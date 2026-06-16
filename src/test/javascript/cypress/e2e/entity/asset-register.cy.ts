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

describe('AssetRegister e2e test', () => {
  const assetRegisterPageUrl = '/asset-register';
  const assetRegisterPageUrlPattern = new RegExp('/asset-register(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetRegisterSample = {};

  let assetRegister;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-registers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-registers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-registers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetRegister) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-registers/${assetRegister.id}`,
      }).then(() => {
        assetRegister = undefined;
      });
    }
  });

  it('AssetRegisters menu should load AssetRegisters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-register');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetRegister').should('exist');
    cy.url().should('match', assetRegisterPageUrlPattern);
  });

  describe('AssetRegister page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetRegisterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetRegister page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-register/new$'));
        cy.getEntityCreateUpdateHeading('AssetRegister');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegisterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-registers',
          body: assetRegisterSample,
        }).then(({ body }) => {
          assetRegister = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-registers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-registers?page=0&size=20>; rel="last",<http://localhost/api/asset-registers?page=0&size=20>; rel="first"',
              },
              body: [assetRegister],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetRegisterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetRegister page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetRegister');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegisterPageUrlPattern);
      });

      it('edit button click should load edit AssetRegister page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetRegister');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegisterPageUrlPattern);
      });

      it('edit button click should load edit AssetRegister page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetRegister');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegisterPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetRegister', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetRegister').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegisterPageUrlPattern);

        assetRegister = undefined;
      });
    });
  });

  describe('new AssetRegister page', () => {
    beforeEach(() => {
      cy.visit(`${assetRegisterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetRegister');
    });

    it('should create an instance of AssetRegister', () => {
      cy.get(`[data-cy="branchCode"]`).type('wholly huzzah');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'wholly huzzah');

      cy.get(`[data-cy="branchId"]`).type('trash till');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'trash till');

      cy.get(`[data-cy="assetRegisterCode"]`).type('quit');
      cy.get(`[data-cy="assetRegisterCode"]`).should('have.value', 'quit');

      cy.get(`[data-cy="assetCategoryCode"]`).type('glisten rebuke above');
      cy.get(`[data-cy="assetCategoryCode"]`).should('have.value', 'glisten rebuke above');

      cy.get(`[data-cy="assetSubCategoryCode"]`).type('hateful scarcely');
      cy.get(`[data-cy="assetSubCategoryCode"]`).should('have.value', 'hateful scarcely');

      cy.get(`[data-cy="assetName"]`).type('foodstuffs regular');
      cy.get(`[data-cy="assetName"]`).should('have.value', 'foodstuffs regular');

      cy.get(`[data-cy="category"]`).type('oblong geez');
      cy.get(`[data-cy="category"]`).should('have.value', 'oblong geez');

      cy.get(`[data-cy="purchaseDate"]`).type('2026-06-11');
      cy.get(`[data-cy="purchaseDate"]`).blur();
      cy.get(`[data-cy="purchaseDate"]`).should('have.value', '2026-06-11');

      cy.get(`[data-cy="purchaseCost"]`).type('7822.71');
      cy.get(`[data-cy="purchaseCost"]`).should('have.value', '7822.71');

      cy.get(`[data-cy="currentValue"]`).type('12255.68');
      cy.get(`[data-cy="currentValue"]`).should('have.value', '12255.68');

      cy.get(`[data-cy="depreciationRate"]`).type('18721.3');
      cy.get(`[data-cy="depreciationRate"]`).should('have.value', '18721.3');

      cy.get(`[data-cy="accumulatedDepreciation"]`).type('28977.02');
      cy.get(`[data-cy="accumulatedDepreciation"]`).should('have.value', '28977.02');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        assetRegister = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', assetRegisterPageUrlPattern);
    });
  });
});
