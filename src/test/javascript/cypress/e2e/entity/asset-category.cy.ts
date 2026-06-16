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

describe('AssetCategory e2e test', () => {
  const assetCategoryPageUrl = '/asset-category';
  const assetCategoryPageUrlPattern = new RegExp('/asset-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetCategorySample = {};

  let assetCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-categories/${assetCategory.id}`,
      }).then(() => {
        assetCategory = undefined;
      });
    }
  });

  it('AssetCategories menu should load AssetCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetCategory').should('exist');
    cy.url().should('match', assetCategoryPageUrlPattern);
  });

  describe('AssetCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-category/new$'));
        cy.getEntityCreateUpdateHeading('AssetCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-categories',
          body: assetCategorySample,
        }).then(({ body }) => {
          assetCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-categories?page=0&size=20>; rel="last",<http://localhost/api/asset-categories?page=0&size=20>; rel="first"',
              },
              body: [assetCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCategoryPageUrlPattern);
      });

      it('edit button click should load edit AssetCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCategoryPageUrlPattern);
      });

      it('edit button click should load edit AssetCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCategoryPageUrlPattern);

        assetCategory = undefined;
      });
    });
  });

  describe('new AssetCategory page', () => {
    beforeEach(() => {
      cy.visit(`${assetCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetCategory');
    });

    it('should create an instance of AssetCategory', () => {
      cy.get(`[data-cy="branchCode"]`).type('throughout treasure stylish');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'throughout treasure stylish');

      cy.get(`[data-cy="branchId"]`).type('veto');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'veto');

      cy.get(`[data-cy="assetCategoryCode"]`).type('kick');
      cy.get(`[data-cy="assetCategoryCode"]`).should('have.value', 'kick');

      cy.get(`[data-cy="assetCategoryName"]`).type('cram onto');
      cy.get(`[data-cy="assetCategoryName"]`).should('have.value', 'cram onto');

      cy.get(`[data-cy="description"]`).type('for hence');
      cy.get(`[data-cy="description"]`).should('have.value', 'for hence');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        assetCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', assetCategoryPageUrlPattern);
    });
  });
});
