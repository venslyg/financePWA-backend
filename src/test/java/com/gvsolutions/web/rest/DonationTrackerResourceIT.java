package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.DonationTrackerAsserts.*;
import static com.gvsolutions.web.rest.TestUtil.createUpdateProxyForBean;
import static com.gvsolutions.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvsolutions.IntegrationTest;
import com.gvsolutions.domain.DonationTracker;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.repository.DonationTrackerRepository;
import com.gvsolutions.repository.search.DonationTrackerSearchRepository;
import com.gvsolutions.service.dto.DonationTrackerDTO;
import com.gvsolutions.service.mapper.DonationTrackerMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DonationTrackerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonationTrackerResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DONATION_ID_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DONATION_ID_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DONOR_NAME_OR_ORG = "AAAAAAAAAA";
    private static final String UPDATED_DONOR_NAME_OR_ORG = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_DETAILS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE = "BBBBBBBBBB";

    private static final PaymentMode DEFAULT_RECEIVED_VIA_MODE = PaymentMode.CASH;
    private static final PaymentMode UPDATED_RECEIVED_VIA_MODE = PaymentMode.BANK;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/donation-trackers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/donation-trackers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DonationTrackerRepository donationTrackerRepository;

    @Autowired
    private DonationTrackerMapper donationTrackerMapper;

    @Autowired
    private DonationTrackerSearchRepository donationTrackerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonationTrackerMockMvc;

    private DonationTracker donationTracker;

    private DonationTracker insertedDonationTracker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonationTracker createEntity() {
        return new DonationTracker()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .donationIdCode(DEFAULT_DONATION_ID_CODE)
            .date(DEFAULT_DATE)
            .donorNameOrOrg(DEFAULT_DONOR_NAME_OR_ORG)
            .contactDetails(DEFAULT_CONTACT_DETAILS)
            .amount(DEFAULT_AMOUNT)
            .purpose(DEFAULT_PURPOSE)
            .receivedViaMode(DEFAULT_RECEIVED_VIA_MODE)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonationTracker createUpdatedEntity() {
        return new DonationTracker()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .donationIdCode(UPDATED_DONATION_ID_CODE)
            .date(UPDATED_DATE)
            .donorNameOrOrg(UPDATED_DONOR_NAME_OR_ORG)
            .contactDetails(UPDATED_CONTACT_DETAILS)
            .amount(UPDATED_AMOUNT)
            .purpose(UPDATED_PURPOSE)
            .receivedViaMode(UPDATED_RECEIVED_VIA_MODE)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        donationTracker = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDonationTracker != null) {
            donationTrackerRepository.delete(insertedDonationTracker);
            donationTrackerSearchRepository.delete(insertedDonationTracker);
            insertedDonationTracker = null;
        }
    }

    @Test
    @Transactional
    void createDonationTracker() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);
        var returnedDonationTrackerDTO = om.readValue(
            restDonationTrackerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTrackerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DonationTrackerDTO.class
        );

        // Validate the DonationTracker in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDonationTracker = donationTrackerMapper.toEntity(returnedDonationTrackerDTO);
        assertDonationTrackerUpdatableFieldsEquals(returnedDonationTracker, getPersistedDonationTracker(returnedDonationTracker));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDonationTracker = returnedDonationTracker;
    }

    @Test
    @Transactional
    void createDonationTrackerWithExistingId() throws Exception {
        // Create the DonationTracker with an existing ID
        donationTracker.setId(1L);
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationTrackerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTrackerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDonationTrackers() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList
        restDonationTrackerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donationTracker.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].donationIdCode").value(hasItem(DEFAULT_DONATION_ID_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].donorNameOrOrg").value(hasItem(DEFAULT_DONOR_NAME_OR_ORG)))
            .andExpect(jsonPath("$.[*].contactDetails").value(hasItem(DEFAULT_CONTACT_DETAILS)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].receivedViaMode").value(hasItem(DEFAULT_RECEIVED_VIA_MODE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getDonationTracker() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get the donationTracker
        restDonationTrackerMockMvc
            .perform(get(ENTITY_API_URL_ID, donationTracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donationTracker.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.donationIdCode").value(DEFAULT_DONATION_ID_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.donorNameOrOrg").value(DEFAULT_DONOR_NAME_OR_ORG))
            .andExpect(jsonPath("$.contactDetails").value(DEFAULT_CONTACT_DETAILS))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE))
            .andExpect(jsonPath("$.receivedViaMode").value(DEFAULT_RECEIVED_VIA_MODE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getDonationTrackersByIdFiltering() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        Long id = donationTracker.getId();

        defaultDonationTrackerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDonationTrackerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDonationTrackerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchCode equals to
        defaultDonationTrackerFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchCode in
        defaultDonationTrackerFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchCode is not null
        defaultDonationTrackerFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchCode contains
        defaultDonationTrackerFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchCode does not contain
        defaultDonationTrackerFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchId equals to
        defaultDonationTrackerFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchId in
        defaultDonationTrackerFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchId is not null
        defaultDonationTrackerFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchId contains
        defaultDonationTrackerFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where branchId does not contain
        defaultDonationTrackerFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonationIdCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donationIdCode equals to
        defaultDonationTrackerFiltering(
            "donationIdCode.equals=" + DEFAULT_DONATION_ID_CODE,
            "donationIdCode.equals=" + UPDATED_DONATION_ID_CODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonationIdCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donationIdCode in
        defaultDonationTrackerFiltering(
            "donationIdCode.in=" + DEFAULT_DONATION_ID_CODE + "," + UPDATED_DONATION_ID_CODE,
            "donationIdCode.in=" + UPDATED_DONATION_ID_CODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonationIdCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donationIdCode is not null
        defaultDonationTrackerFiltering("donationIdCode.specified=true", "donationIdCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonationIdCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donationIdCode contains
        defaultDonationTrackerFiltering(
            "donationIdCode.contains=" + DEFAULT_DONATION_ID_CODE,
            "donationIdCode.contains=" + UPDATED_DONATION_ID_CODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonationIdCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donationIdCode does not contain
        defaultDonationTrackerFiltering(
            "donationIdCode.doesNotContain=" + UPDATED_DONATION_ID_CODE,
            "donationIdCode.doesNotContain=" + DEFAULT_DONATION_ID_CODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date equals to
        defaultDonationTrackerFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date in
        defaultDonationTrackerFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date is not null
        defaultDonationTrackerFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date is greater than or equal to
        defaultDonationTrackerFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date is less than or equal to
        defaultDonationTrackerFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date is less than
        defaultDonationTrackerFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where date is greater than
        defaultDonationTrackerFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonorNameOrOrgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donorNameOrOrg equals to
        defaultDonationTrackerFiltering(
            "donorNameOrOrg.equals=" + DEFAULT_DONOR_NAME_OR_ORG,
            "donorNameOrOrg.equals=" + UPDATED_DONOR_NAME_OR_ORG
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonorNameOrOrgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donorNameOrOrg in
        defaultDonationTrackerFiltering(
            "donorNameOrOrg.in=" + DEFAULT_DONOR_NAME_OR_ORG + "," + UPDATED_DONOR_NAME_OR_ORG,
            "donorNameOrOrg.in=" + UPDATED_DONOR_NAME_OR_ORG
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonorNameOrOrgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donorNameOrOrg is not null
        defaultDonationTrackerFiltering("donorNameOrOrg.specified=true", "donorNameOrOrg.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonorNameOrOrgContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donorNameOrOrg contains
        defaultDonationTrackerFiltering(
            "donorNameOrOrg.contains=" + DEFAULT_DONOR_NAME_OR_ORG,
            "donorNameOrOrg.contains=" + UPDATED_DONOR_NAME_OR_ORG
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByDonorNameOrOrgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where donorNameOrOrg does not contain
        defaultDonationTrackerFiltering(
            "donorNameOrOrg.doesNotContain=" + UPDATED_DONOR_NAME_OR_ORG,
            "donorNameOrOrg.doesNotContain=" + DEFAULT_DONOR_NAME_OR_ORG
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByContactDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where contactDetails equals to
        defaultDonationTrackerFiltering(
            "contactDetails.equals=" + DEFAULT_CONTACT_DETAILS,
            "contactDetails.equals=" + UPDATED_CONTACT_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByContactDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where contactDetails in
        defaultDonationTrackerFiltering(
            "contactDetails.in=" + DEFAULT_CONTACT_DETAILS + "," + UPDATED_CONTACT_DETAILS,
            "contactDetails.in=" + UPDATED_CONTACT_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByContactDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where contactDetails is not null
        defaultDonationTrackerFiltering("contactDetails.specified=true", "contactDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByContactDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where contactDetails contains
        defaultDonationTrackerFiltering(
            "contactDetails.contains=" + DEFAULT_CONTACT_DETAILS,
            "contactDetails.contains=" + UPDATED_CONTACT_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByContactDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where contactDetails does not contain
        defaultDonationTrackerFiltering(
            "contactDetails.doesNotContain=" + UPDATED_CONTACT_DETAILS,
            "contactDetails.doesNotContain=" + DEFAULT_CONTACT_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount equals to
        defaultDonationTrackerFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount in
        defaultDonationTrackerFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount is not null
        defaultDonationTrackerFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount is greater than or equal to
        defaultDonationTrackerFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount is less than or equal to
        defaultDonationTrackerFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount is less than
        defaultDonationTrackerFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where amount is greater than
        defaultDonationTrackerFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByPurposeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where purpose equals to
        defaultDonationTrackerFiltering("purpose.equals=" + DEFAULT_PURPOSE, "purpose.equals=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByPurposeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where purpose in
        defaultDonationTrackerFiltering("purpose.in=" + DEFAULT_PURPOSE + "," + UPDATED_PURPOSE, "purpose.in=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByPurposeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where purpose is not null
        defaultDonationTrackerFiltering("purpose.specified=true", "purpose.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByPurposeContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where purpose contains
        defaultDonationTrackerFiltering("purpose.contains=" + DEFAULT_PURPOSE, "purpose.contains=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByPurposeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where purpose does not contain
        defaultDonationTrackerFiltering("purpose.doesNotContain=" + UPDATED_PURPOSE, "purpose.doesNotContain=" + DEFAULT_PURPOSE);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByReceivedViaModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where receivedViaMode equals to
        defaultDonationTrackerFiltering(
            "receivedViaMode.equals=" + DEFAULT_RECEIVED_VIA_MODE,
            "receivedViaMode.equals=" + UPDATED_RECEIVED_VIA_MODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByReceivedViaModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where receivedViaMode in
        defaultDonationTrackerFiltering(
            "receivedViaMode.in=" + DEFAULT_RECEIVED_VIA_MODE + "," + UPDATED_RECEIVED_VIA_MODE,
            "receivedViaMode.in=" + UPDATED_RECEIVED_VIA_MODE
        );
    }

    @Test
    @Transactional
    void getAllDonationTrackersByReceivedViaModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where receivedViaMode is not null
        defaultDonationTrackerFiltering("receivedViaMode.specified=true", "receivedViaMode.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where notes equals to
        defaultDonationTrackerFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where notes in
        defaultDonationTrackerFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where notes is not null
        defaultDonationTrackerFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationTrackersByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where notes contains
        defaultDonationTrackerFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDonationTrackersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        // Get all the donationTrackerList where notes does not contain
        defaultDonationTrackerFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    private void defaultDonationTrackerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDonationTrackerShouldBeFound(shouldBeFound);
        defaultDonationTrackerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDonationTrackerShouldBeFound(String filter) throws Exception {
        restDonationTrackerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donationTracker.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].donationIdCode").value(hasItem(DEFAULT_DONATION_ID_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].donorNameOrOrg").value(hasItem(DEFAULT_DONOR_NAME_OR_ORG)))
            .andExpect(jsonPath("$.[*].contactDetails").value(hasItem(DEFAULT_CONTACT_DETAILS)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].receivedViaMode").value(hasItem(DEFAULT_RECEIVED_VIA_MODE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restDonationTrackerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDonationTrackerShouldNotBeFound(String filter) throws Exception {
        restDonationTrackerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDonationTrackerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDonationTracker() throws Exception {
        // Get the donationTracker
        restDonationTrackerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDonationTracker() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTrackerSearchRepository.save(donationTracker);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());

        // Update the donationTracker
        DonationTracker updatedDonationTracker = donationTrackerRepository.findById(donationTracker.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDonationTracker are not directly saved in db
        em.detach(updatedDonationTracker);
        updatedDonationTracker
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .donationIdCode(UPDATED_DONATION_ID_CODE)
            .date(UPDATED_DATE)
            .donorNameOrOrg(UPDATED_DONOR_NAME_OR_ORG)
            .contactDetails(UPDATED_CONTACT_DETAILS)
            .amount(UPDATED_AMOUNT)
            .purpose(UPDATED_PURPOSE)
            .receivedViaMode(UPDATED_RECEIVED_VIA_MODE)
            .notes(UPDATED_NOTES);
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(updatedDonationTracker);

        restDonationTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donationTrackerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donationTrackerDTO))
            )
            .andExpect(status().isOk());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDonationTrackerToMatchAllProperties(updatedDonationTracker);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DonationTracker> donationTrackerSearchList = Streamable.of(donationTrackerSearchRepository.findAll()).toList();
                DonationTracker testDonationTrackerSearch = donationTrackerSearchList.get(searchDatabaseSizeAfter - 1);

                assertDonationTrackerAllPropertiesEquals(testDonationTrackerSearch, updatedDonationTracker);
            });
    }

    @Test
    @Transactional
    void putNonExistingDonationTracker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        donationTracker.setId(longCount.incrementAndGet());

        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donationTrackerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donationTrackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonationTracker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        donationTracker.setId(longCount.incrementAndGet());

        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donationTrackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonationTracker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        donationTracker.setId(longCount.incrementAndGet());

        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTrackerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTrackerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDonationTrackerWithPatch() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donationTracker using partial update
        DonationTracker partialUpdatedDonationTracker = new DonationTracker();
        partialUpdatedDonationTracker.setId(donationTracker.getId());

        partialUpdatedDonationTracker
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .donationIdCode(UPDATED_DONATION_ID_CODE)
            .date(UPDATED_DATE)
            .donorNameOrOrg(UPDATED_DONOR_NAME_OR_ORG)
            .receivedViaMode(UPDATED_RECEIVED_VIA_MODE)
            .notes(UPDATED_NOTES);

        restDonationTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonationTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDonationTracker))
            )
            .andExpect(status().isOk());

        // Validate the DonationTracker in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDonationTrackerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDonationTracker, donationTracker),
            getPersistedDonationTracker(donationTracker)
        );
    }

    @Test
    @Transactional
    void fullUpdateDonationTrackerWithPatch() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donationTracker using partial update
        DonationTracker partialUpdatedDonationTracker = new DonationTracker();
        partialUpdatedDonationTracker.setId(donationTracker.getId());

        partialUpdatedDonationTracker
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .donationIdCode(UPDATED_DONATION_ID_CODE)
            .date(UPDATED_DATE)
            .donorNameOrOrg(UPDATED_DONOR_NAME_OR_ORG)
            .contactDetails(UPDATED_CONTACT_DETAILS)
            .amount(UPDATED_AMOUNT)
            .purpose(UPDATED_PURPOSE)
            .receivedViaMode(UPDATED_RECEIVED_VIA_MODE)
            .notes(UPDATED_NOTES);

        restDonationTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonationTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDonationTracker))
            )
            .andExpect(status().isOk());

        // Validate the DonationTracker in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDonationTrackerUpdatableFieldsEquals(
            partialUpdatedDonationTracker,
            getPersistedDonationTracker(partialUpdatedDonationTracker)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDonationTracker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        donationTracker.setId(longCount.incrementAndGet());

        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donationTrackerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(donationTrackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonationTracker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        donationTracker.setId(longCount.incrementAndGet());

        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(donationTrackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonationTracker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        donationTracker.setId(longCount.incrementAndGet());

        // Create the DonationTracker
        DonationTrackerDTO donationTrackerDTO = donationTrackerMapper.toDto(donationTracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTrackerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(donationTrackerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonationTracker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDonationTracker() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);
        donationTrackerRepository.save(donationTracker);
        donationTrackerSearchRepository.save(donationTracker);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the donationTracker
        restDonationTrackerMockMvc
            .perform(delete(ENTITY_API_URL_ID, donationTracker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donationTrackerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDonationTracker() throws Exception {
        // Initialize the database
        insertedDonationTracker = donationTrackerRepository.saveAndFlush(donationTracker);
        donationTrackerSearchRepository.save(donationTracker);

        // Search the donationTracker
        restDonationTrackerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + donationTracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donationTracker.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].donationIdCode").value(hasItem(DEFAULT_DONATION_ID_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].donorNameOrOrg").value(hasItem(DEFAULT_DONOR_NAME_OR_ORG)))
            .andExpect(jsonPath("$.[*].contactDetails").value(hasItem(DEFAULT_CONTACT_DETAILS)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].receivedViaMode").value(hasItem(DEFAULT_RECEIVED_VIA_MODE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    protected long getRepositoryCount() {
        return donationTrackerRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DonationTracker getPersistedDonationTracker(DonationTracker donationTracker) {
        return donationTrackerRepository.findById(donationTracker.getId()).orElseThrow();
    }

    protected void assertPersistedDonationTrackerToMatchAllProperties(DonationTracker expectedDonationTracker) {
        assertDonationTrackerAllPropertiesEquals(expectedDonationTracker, getPersistedDonationTracker(expectedDonationTracker));
    }

    protected void assertPersistedDonationTrackerToMatchUpdatableProperties(DonationTracker expectedDonationTracker) {
        assertDonationTrackerAllUpdatablePropertiesEquals(expectedDonationTracker, getPersistedDonationTracker(expectedDonationTracker));
    }
}
