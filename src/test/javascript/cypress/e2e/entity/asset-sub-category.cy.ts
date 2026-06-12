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

describe('AssetSubCategory e2e test', () => {
  const assetSubCategoryPageUrl = '/asset-sub-category';
  const assetSubCategoryPageUrlPattern = new RegExp('/asset-sub-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetSubCategorySample = {};

  let assetSubCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-sub-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-sub-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-sub-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetSubCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-sub-categories/${assetSubCategory.id}`,
      }).then(() => {
        assetSubCategory = undefined;
      });
    }
  });

  it('AssetSubCategories menu should load AssetSubCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-sub-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetSubCategory').should('exist');
    cy.url().should('match', assetSubCategoryPageUrlPattern);
  });

  describe('AssetSubCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetSubCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetSubCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-sub-category/new$'));
        cy.getEntityCreateUpdateHeading('AssetSubCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetSubCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-sub-categories',
          body: assetSubCategorySample,
        }).then(({ body }) => {
          assetSubCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-sub-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-sub-categories?page=0&size=20>; rel="last",<http://localhost/api/asset-sub-categories?page=0&size=20>; rel="first"',
              },
              body: [assetSubCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetSubCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetSubCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetSubCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetSubCategoryPageUrlPattern);
      });

      it('edit button click should load edit AssetSubCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetSubCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetSubCategoryPageUrlPattern);
      });

      it('edit button click should load edit AssetSubCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetSubCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetSubCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetSubCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetSubCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assetSubCategoryPageUrlPattern);

        assetSubCategory = undefined;
      });
    });
  });

  describe('new AssetSubCategory page', () => {
    beforeEach(() => {
      cy.visit(`${assetSubCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetSubCategory');
    });

    it('should create an instance of AssetSubCategory', () => {
      cy.get(`[data-cy="assetCategoryCode"]`).type('plumber incidentally wafer');
      cy.get(`[data-cy="assetCategoryCode"]`).should('have.value', 'plumber incidentally wafer');

      cy.get(`[data-cy="assetSubCategoryCode"]`).type('captain tensely');
      cy.get(`[data-cy="assetSubCategoryCode"]`).should('have.value', 'captain tensely');

      cy.get(`[data-cy="assetSubCategoryName"]`).type('though');
      cy.get(`[data-cy="assetSubCategoryName"]`).should('have.value', 'though');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        assetSubCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', assetSubCategoryPageUrlPattern);
    });
  });
});
