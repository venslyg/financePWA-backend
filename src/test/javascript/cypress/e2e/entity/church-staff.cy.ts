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

describe('ChurchStaff e2e test', () => {
  const churchStaffPageUrl = '/church-staff';
  const churchStaffPageUrlPattern = new RegExp('/church-staff(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const churchStaffSample = {};

  let churchStaff;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/church-staffs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/church-staffs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/church-staffs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (churchStaff) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/church-staffs/${churchStaff.id}`,
      }).then(() => {
        churchStaff = undefined;
      });
    }
  });

  it('ChurchStaffs menu should load ChurchStaffs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('church-staff');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChurchStaff').should('exist');
    cy.url().should('match', churchStaffPageUrlPattern);
  });

  describe('ChurchStaff page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(churchStaffPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChurchStaff page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/church-staff/new$'));
        cy.getEntityCreateUpdateHeading('ChurchStaff');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchStaffPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/church-staffs',
          body: churchStaffSample,
        }).then(({ body }) => {
          churchStaff = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/church-staffs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/church-staffs?page=0&size=20>; rel="last",<http://localhost/api/church-staffs?page=0&size=20>; rel="first"',
              },
              body: [churchStaff],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(churchStaffPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChurchStaff page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('churchStaff');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchStaffPageUrlPattern);
      });

      it('edit button click should load edit ChurchStaff page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChurchStaff');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchStaffPageUrlPattern);
      });

      it('edit button click should load edit ChurchStaff page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChurchStaff');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchStaffPageUrlPattern);
      });

      it('last delete button click should delete instance of ChurchStaff', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('churchStaff').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchStaffPageUrlPattern);

        churchStaff = undefined;
      });
    });
  });

  describe('new ChurchStaff page', () => {
    beforeEach(() => {
      cy.visit(`${churchStaffPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChurchStaff');
    });

    it('should create an instance of ChurchStaff', () => {
      cy.get(`[data-cy="staffCode"]`).type('collaboration polyester');
      cy.get(`[data-cy="staffCode"]`).should('have.value', 'collaboration polyester');

      cy.get(`[data-cy="branchCode"]`).type('monasticism after');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'monasticism after');

      cy.get(`[data-cy="branchId"]`).type('till fill gah');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'till fill gah');

      cy.get(`[data-cy="fullName"]`).type('made-up desk regarding');
      cy.get(`[data-cy="fullName"]`).should('have.value', 'made-up desk regarding');

      cy.get(`[data-cy="position"]`).type('motor fatally whoever');
      cy.get(`[data-cy="position"]`).should('have.value', 'motor fatally whoever');

      cy.get(`[data-cy="staffType"]`).select('PART_TIME');

      cy.get(`[data-cy="contactNumber"]`).type('blah secularize');
      cy.get(`[data-cy="contactNumber"]`).should('have.value', 'blah secularize');

      cy.get(`[data-cy="hourlyRateOrMonthlySalary"]`).type('32143.69');
      cy.get(`[data-cy="hourlyRateOrMonthlySalary"]`).should('have.value', '32143.69');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        churchStaff = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', churchStaffPageUrlPattern);
    });
  });
});
