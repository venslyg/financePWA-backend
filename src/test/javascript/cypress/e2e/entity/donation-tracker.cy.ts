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

describe('DonationTracker e2e test', () => {
  const donationTrackerPageUrl = '/donation-tracker';
  const donationTrackerPageUrlPattern = new RegExp('/donation-tracker(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const donationTrackerSample = {};

  let donationTracker;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/donation-trackers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/donation-trackers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/donation-trackers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (donationTracker) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/donation-trackers/${donationTracker.id}`,
      }).then(() => {
        donationTracker = undefined;
      });
    }
  });

  it('DonationTrackers menu should load DonationTrackers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('donation-tracker');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DonationTracker').should('exist');
    cy.url().should('match', donationTrackerPageUrlPattern);
  });

  describe('DonationTracker page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(donationTrackerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DonationTracker page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/donation-tracker/new$'));
        cy.getEntityCreateUpdateHeading('DonationTracker');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTrackerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/donation-trackers',
          body: donationTrackerSample,
        }).then(({ body }) => {
          donationTracker = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/donation-trackers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/donation-trackers?page=0&size=20>; rel="last",<http://localhost/api/donation-trackers?page=0&size=20>; rel="first"',
              },
              body: [donationTracker],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(donationTrackerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DonationTracker page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('donationTracker');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTrackerPageUrlPattern);
      });

      it('edit button click should load edit DonationTracker page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DonationTracker');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTrackerPageUrlPattern);
      });

      it('edit button click should load edit DonationTracker page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DonationTracker');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTrackerPageUrlPattern);
      });

      it('last delete button click should delete instance of DonationTracker', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('donationTracker').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTrackerPageUrlPattern);

        donationTracker = undefined;
      });
    });
  });

  describe('new DonationTracker page', () => {
    beforeEach(() => {
      cy.visit(`${donationTrackerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DonationTracker');
    });

    it('should create an instance of DonationTracker', () => {
      cy.get(`[data-cy="branchCode"]`).type('enthusiastically');
      cy.get(`[data-cy="branchCode"]`).should('have.value', 'enthusiastically');

      cy.get(`[data-cy="branchId"]`).type('ew');
      cy.get(`[data-cy="branchId"]`).should('have.value', 'ew');

      cy.get(`[data-cy="donationIdCode"]`).type('glass tray um');
      cy.get(`[data-cy="donationIdCode"]`).should('have.value', 'glass tray um');

      cy.get(`[data-cy="date"]`).type('2026-06-11');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2026-06-11');

      cy.get(`[data-cy="donorNameOrOrg"]`).type('lest because');
      cy.get(`[data-cy="donorNameOrOrg"]`).should('have.value', 'lest because');

      cy.get(`[data-cy="contactDetails"]`).type('yahoo');
      cy.get(`[data-cy="contactDetails"]`).should('have.value', 'yahoo');

      cy.get(`[data-cy="amount"]`).type('22355.4');
      cy.get(`[data-cy="amount"]`).should('have.value', '22355.4');

      cy.get(`[data-cy="purpose"]`).type('notarize extra-large');
      cy.get(`[data-cy="purpose"]`).should('have.value', 'notarize extra-large');

      cy.get(`[data-cy="receivedViaMode"]`).select('CASH');

      cy.get(`[data-cy="notes"]`).type('ick rightfully worth');
      cy.get(`[data-cy="notes"]`).should('have.value', 'ick rightfully worth');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        donationTracker = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', donationTrackerPageUrlPattern);
    });
  });
});
