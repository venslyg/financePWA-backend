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

describe('InventoryItem e2e test', () => {
  const inventoryItemPageUrl = '/inventory-item';
  const inventoryItemPageUrlPattern = new RegExp('/inventory-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inventoryItemSample = {};

  let inventoryItem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inventoryItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-items/${inventoryItem.id}`,
      }).then(() => {
        inventoryItem = undefined;
      });
    }
  });

  it('InventoryItems menu should load InventoryItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryItem').should('exist');
    cy.url().should('match', inventoryItemPageUrlPattern);
  });

  describe('InventoryItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inventory-item/new$'));
        cy.getEntityCreateUpdateHeading('InventoryItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-items',
          body: inventoryItemSample,
        }).then(({ body }) => {
          inventoryItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventory-items?page=0&size=20>; rel="last",<http://localhost/api/inventory-items?page=0&size=20>; rel="first"',
              },
              body: [inventoryItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InventoryItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryItemPageUrlPattern);
      });

      it('edit button click should load edit InventoryItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryItemPageUrlPattern);
      });

      it('edit button click should load edit InventoryItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryItemPageUrlPattern);
      });

      it('last delete button click should delete instance of InventoryItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryItemPageUrlPattern);

        inventoryItem = undefined;
      });
    });
  });

  describe('new InventoryItem page', () => {
    beforeEach(() => {
      cy.visit(`${inventoryItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryItem');
    });

    it('should create an instance of InventoryItem', () => {
      cy.get(`[data-cy="branchCode"]`).type('progress dilate angrily');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'progress dilate angrily');

      cy.get(`[data-cy="branchId"]`).type('around');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'around');

      cy.get(`[data-cy="inventoryItemCode"]`).type('understanding beside');
      cy.get(`[data-cy="inventoryItemCode"]`).should('have.value', 'understanding beside');

      cy.get(`[data-cy="itemName"]`).type('anenst inside gifted');
      cy.get(`[data-cy="itemName"]`).should('have.value', 'anenst inside gifted');

      cy.get(`[data-cy="category"]`).type('solace');
      cy.get(`[data-cy="category"]`).should('have.value', 'solace');

      cy.get(`[data-cy="quantity"]`).type('17094.36');
      cy.get(`[data-cy="quantity"]`).should('have.value', '17094.36');

      cy.get(`[data-cy="unitPrice"]`).type('5322.04');
      cy.get(`[data-cy="unitPrice"]`).should('have.value', '5322.04');

      cy.get(`[data-cy="runningStockCount"]`).type('17427.06');
      cy.get(`[data-cy="runningStockCount"]`).should('have.value', '17427.06');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventoryItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', inventoryItemPageUrlPattern);
    });
  });
});
