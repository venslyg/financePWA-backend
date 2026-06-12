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

describe('MaintenanceLog e2e test', () => {
  const maintenanceLogPageUrl = '/maintenance-log';
  const maintenanceLogPageUrlPattern = new RegExp('/maintenance-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const maintenanceLogSample = {};

  let maintenanceLog;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/maintenance-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/maintenance-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/maintenance-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (maintenanceLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/maintenance-logs/${maintenanceLog.id}`,
      }).then(() => {
        maintenanceLog = undefined;
      });
    }
  });

  it('MaintenanceLogs menu should load MaintenanceLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('maintenance-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MaintenanceLog').should('exist');
    cy.url().should('match', maintenanceLogPageUrlPattern);
  });

  describe('MaintenanceLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(maintenanceLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MaintenanceLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/maintenance-log/new$'));
        cy.getEntityCreateUpdateHeading('MaintenanceLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', maintenanceLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/maintenance-logs',
          body: maintenanceLogSample,
        }).then(({ body }) => {
          maintenanceLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/maintenance-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/maintenance-logs?page=0&size=20>; rel="last",<http://localhost/api/maintenance-logs?page=0&size=20>; rel="first"',
              },
              body: [maintenanceLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(maintenanceLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MaintenanceLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('maintenanceLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', maintenanceLogPageUrlPattern);
      });

      it('edit button click should load edit MaintenanceLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MaintenanceLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', maintenanceLogPageUrlPattern);
      });

      it('edit button click should load edit MaintenanceLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MaintenanceLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', maintenanceLogPageUrlPattern);
      });

      it('last delete button click should delete instance of MaintenanceLog', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('maintenanceLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', maintenanceLogPageUrlPattern);

        maintenanceLog = undefined;
      });
    });
  });

  describe('new MaintenanceLog page', () => {
    beforeEach(() => {
      cy.visit(`${maintenanceLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MaintenanceLog');
    });

    it('should create an instance of MaintenanceLog', () => {
      cy.get(`[data-cy="branchCode"]`).type('mozzarella');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'mozzarella');

      cy.get(`[data-cy="branchId"]`).type('aw');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'aw');

      cy.get(`[data-cy="maintenanceLogCode"]`).type('crank superior');
      cy.get(`[data-cy="maintenanceLogCode"]`).should('have.value', 'crank superior');

      cy.get(`[data-cy="logDate"]`).type('2026-06-11');
      cy.get(`[data-cy="logDate"]`).blur();
      cy.get(`[data-cy="logDate"]`).should('have.value', '2026-06-11');

      cy.get(`[data-cy="logType"]`).select('REPAIR');

      cy.get(`[data-cy="description"]`).type('outside fooey whenever');
      cy.get(`[data-cy="description"]`).should('have.value', 'outside fooey whenever');

      cy.get(`[data-cy="cost"]`).type('18369.52');
      cy.get(`[data-cy="cost"]`).should('have.value', '18369.52');

      cy.get(`[data-cy="vendor"]`).type('unexpectedly while bewail');
      cy.get(`[data-cy="vendor"]`).should('have.value', 'unexpectedly while bewail');

      cy.get(`[data-cy="nextServiceDate"]`).type('2026-06-11');
      cy.get(`[data-cy="nextServiceDate"]`).blur();
      cy.get(`[data-cy="nextServiceDate"]`).should('have.value', '2026-06-11');

      cy.get(`[data-cy="note"]`).type('aha ha even');
      cy.get(`[data-cy="note"]`).should('have.value', 'aha ha even');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        maintenanceLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', maintenanceLogPageUrlPattern);
    });
  });
});
