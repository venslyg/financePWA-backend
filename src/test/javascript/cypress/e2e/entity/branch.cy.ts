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

describe('Branch e2e test', () => {
  const branchPageUrl = '/branch';
  const branchPageUrlPattern = new RegExp('/branch(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const branchSample = {};

  let branch;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/branches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/branches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/branches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (branch) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/branches/${branch.id}`,
      }).then(() => {
        branch = undefined;
      });
    }
  });

  it('Branches menu should load Branches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Branch').should('exist');
    cy.url().should('match', branchPageUrlPattern);
  });

  describe('Branch page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(branchPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Branch page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/branch/new$'));
        cy.getEntityCreateUpdateHeading('Branch');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', branchPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/branches',
          body: branchSample,
        }).then(({ body }) => {
          branch = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/branches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/branches?page=0&size=20>; rel="last",<http://localhost/api/branches?page=0&size=20>; rel="first"',
              },
              body: [branch],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(branchPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Branch page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('branch');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', branchPageUrlPattern);
      });

      it('edit button click should load edit Branch page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Branch');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', branchPageUrlPattern);
      });

      it('edit button click should load edit Branch page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Branch');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', branchPageUrlPattern);
      });

      it('last delete button click should delete instance of Branch', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('branch').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', branchPageUrlPattern);

        branch = undefined;
      });
    });
  });

  describe('new Branch page', () => {
    beforeEach(() => {
      cy.visit(`${branchPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Branch');
    });

    it('should create an instance of Branch', () => {
      cy.get(`[data-cy="branchCode"]`).type('utilized');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'utilized');

      cy.get(`[data-cy="branchName"]`).type('ugly');
      cy.get(`[data-cy="branchName"]`).should('have.value', 'ugly');

      cy.get(`[data-cy="location"]`).type('boohoo whether');
      cy.get(`[data-cy="location"]`).should('have.value', 'boohoo whether');

      cy.get(`[data-cy="phoneNumber"]`).type('before');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'before');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        branch = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', branchPageUrlPattern);
    });
  });
});
