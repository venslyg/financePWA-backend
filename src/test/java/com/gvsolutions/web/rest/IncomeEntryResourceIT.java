package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.IncomeEntryAsserts.*;
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
import com.gvsolutions.domain.IncomeEntry;
import com.gvsolutions.domain.enumeration.IncomeType;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import com.gvsolutions.repository.IncomeEntryRepository;
import com.gvsolutions.repository.search.IncomeEntrySearchRepository;
import com.gvsolutions.service.dto.IncomeEntryDTO;
import com.gvsolutions.service.mapper.IncomeEntryMapper;
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
 * Integration tests for the {@link IncomeEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncomeEntryResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INCOME_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INCOME_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_USERNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RECEIPT_NO = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final IncomeType DEFAULT_INCOME_TYPE = IncomeType.REGULAR;
    private static final IncomeType UPDATED_INCOME_TYPE = IncomeType.DONATION;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final PaymentMode DEFAULT_PAYMENT_METHOD = PaymentMode.CASH;
    private static final PaymentMode UPDATED_PAYMENT_METHOD = PaymentMode.BANK;

    private static final String DEFAULT_RECEIVABLE_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVABLE_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVED_BY = "BBBBBBBBBB";

    private static final SyncStatus DEFAULT_SYNC_STATUS = SyncStatus.SYNCED;
    private static final SyncStatus UPDATED_SYNC_STATUS = SyncStatus.PENDING_OFFLINE;

    private static final String ENTITY_API_URL = "/api/income-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/income-entries/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncomeEntryRepository incomeEntryRepository;

    @Autowired
    private IncomeEntryMapper incomeEntryMapper;

    @Autowired
    private IncomeEntrySearchRepository incomeEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomeEntryMockMvc;

    private IncomeEntry incomeEntry;

    private IncomeEntry insertedIncomeEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeEntry createEntity() {
        return new IncomeEntry()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .accountCode(DEFAULT_ACCOUNT_CODE)
            .incomeCode(DEFAULT_INCOME_CODE)
            .createdByUsername(DEFAULT_CREATED_BY_USERNAME)
            .date(DEFAULT_DATE)
            .receiptNo(DEFAULT_RECEIPT_NO)
            .description(DEFAULT_DESCRIPTION)
            .incomeType(DEFAULT_INCOME_TYPE)
            .amount(DEFAULT_AMOUNT)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .receivablePerson(DEFAULT_RECEIVABLE_PERSON)
            .receivedBy(DEFAULT_RECEIVED_BY)
            .syncStatus(DEFAULT_SYNC_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeEntry createUpdatedEntity() {
        return new IncomeEntry()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .incomeCode(UPDATED_INCOME_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .receiptNo(UPDATED_RECEIPT_NO)
            .description(UPDATED_DESCRIPTION)
            .incomeType(UPDATED_INCOME_TYPE)
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .receivablePerson(UPDATED_RECEIVABLE_PERSON)
            .receivedBy(UPDATED_RECEIVED_BY)
            .syncStatus(UPDATED_SYNC_STATUS);
    }

    @BeforeEach
    void initTest() {
        incomeEntry = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIncomeEntry != null) {
            incomeEntryRepository.delete(insertedIncomeEntry);
            incomeEntrySearchRepository.delete(insertedIncomeEntry);
            insertedIncomeEntry = null;
        }
    }

    @Test
    @Transactional
    void createIncomeEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);
        var returnedIncomeEntryDTO = om.readValue(
            restIncomeEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incomeEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IncomeEntryDTO.class
        );

        // Validate the IncomeEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIncomeEntry = incomeEntryMapper.toEntity(returnedIncomeEntryDTO);
        assertIncomeEntryUpdatableFieldsEquals(returnedIncomeEntry, getPersistedIncomeEntry(returnedIncomeEntry));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedIncomeEntry = returnedIncomeEntry;
    }

    @Test
    @Transactional
    void createIncomeEntryWithExistingId() throws Exception {
        // Create the IncomeEntry with an existing ID
        incomeEntry.setId(1L);
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomeEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incomeEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllIncomeEntries() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList
        restIncomeEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].incomeCode").value(hasItem(DEFAULT_INCOME_CODE)))
            .andExpect(jsonPath("$.[*].createdByUsername").value(hasItem(DEFAULT_CREATED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptNo").value(hasItem(DEFAULT_RECEIPT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].incomeType").value(hasItem(DEFAULT_INCOME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].receivablePerson").value(hasItem(DEFAULT_RECEIVABLE_PERSON)))
            .andExpect(jsonPath("$.[*].receivedBy").value(hasItem(DEFAULT_RECEIVED_BY)))
            .andExpect(jsonPath("$.[*].syncStatus").value(hasItem(DEFAULT_SYNC_STATUS.toString())));
    }

    @Test
    @Transactional
    void getIncomeEntry() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get the incomeEntry
        restIncomeEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, incomeEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomeEntry.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.accountCode").value(DEFAULT_ACCOUNT_CODE))
            .andExpect(jsonPath("$.incomeCode").value(DEFAULT_INCOME_CODE))
            .andExpect(jsonPath("$.createdByUsername").value(DEFAULT_CREATED_BY_USERNAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.receiptNo").value(DEFAULT_RECEIPT_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.incomeType").value(DEFAULT_INCOME_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.receivablePerson").value(DEFAULT_RECEIVABLE_PERSON))
            .andExpect(jsonPath("$.receivedBy").value(DEFAULT_RECEIVED_BY))
            .andExpect(jsonPath("$.syncStatus").value(DEFAULT_SYNC_STATUS.toString()));
    }

    @Test
    @Transactional
    void getIncomeEntriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        Long id = incomeEntry.getId();

        defaultIncomeEntryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIncomeEntryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIncomeEntryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchCode equals to
        defaultIncomeEntryFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchCode in
        defaultIncomeEntryFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchCode is not null
        defaultIncomeEntryFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchCode contains
        defaultIncomeEntryFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchCode does not contain
        defaultIncomeEntryFiltering("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE, "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchId equals to
        defaultIncomeEntryFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchId in
        defaultIncomeEntryFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchId is not null
        defaultIncomeEntryFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchId contains
        defaultIncomeEntryFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where branchId does not contain
        defaultIncomeEntryFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAccountCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where accountCode equals to
        defaultIncomeEntryFiltering("accountCode.equals=" + DEFAULT_ACCOUNT_CODE, "accountCode.equals=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAccountCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where accountCode in
        defaultIncomeEntryFiltering(
            "accountCode.in=" + DEFAULT_ACCOUNT_CODE + "," + UPDATED_ACCOUNT_CODE,
            "accountCode.in=" + UPDATED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAccountCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where accountCode is not null
        defaultIncomeEntryFiltering("accountCode.specified=true", "accountCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAccountCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where accountCode contains
        defaultIncomeEntryFiltering("accountCode.contains=" + DEFAULT_ACCOUNT_CODE, "accountCode.contains=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAccountCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where accountCode does not contain
        defaultIncomeEntryFiltering(
            "accountCode.doesNotContain=" + UPDATED_ACCOUNT_CODE,
            "accountCode.doesNotContain=" + DEFAULT_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeCode equals to
        defaultIncomeEntryFiltering("incomeCode.equals=" + DEFAULT_INCOME_CODE, "incomeCode.equals=" + UPDATED_INCOME_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeCode in
        defaultIncomeEntryFiltering(
            "incomeCode.in=" + DEFAULT_INCOME_CODE + "," + UPDATED_INCOME_CODE,
            "incomeCode.in=" + UPDATED_INCOME_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeCode is not null
        defaultIncomeEntryFiltering("incomeCode.specified=true", "incomeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeCode contains
        defaultIncomeEntryFiltering("incomeCode.contains=" + DEFAULT_INCOME_CODE, "incomeCode.contains=" + UPDATED_INCOME_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeCode does not contain
        defaultIncomeEntryFiltering("incomeCode.doesNotContain=" + UPDATED_INCOME_CODE, "incomeCode.doesNotContain=" + DEFAULT_INCOME_CODE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByCreatedByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where createdByUsername equals to
        defaultIncomeEntryFiltering(
            "createdByUsername.equals=" + DEFAULT_CREATED_BY_USERNAME,
            "createdByUsername.equals=" + UPDATED_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByCreatedByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where createdByUsername in
        defaultIncomeEntryFiltering(
            "createdByUsername.in=" + DEFAULT_CREATED_BY_USERNAME + "," + UPDATED_CREATED_BY_USERNAME,
            "createdByUsername.in=" + UPDATED_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByCreatedByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where createdByUsername is not null
        defaultIncomeEntryFiltering("createdByUsername.specified=true", "createdByUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByCreatedByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where createdByUsername contains
        defaultIncomeEntryFiltering(
            "createdByUsername.contains=" + DEFAULT_CREATED_BY_USERNAME,
            "createdByUsername.contains=" + UPDATED_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByCreatedByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where createdByUsername does not contain
        defaultIncomeEntryFiltering(
            "createdByUsername.doesNotContain=" + UPDATED_CREATED_BY_USERNAME,
            "createdByUsername.doesNotContain=" + DEFAULT_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date equals to
        defaultIncomeEntryFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date in
        defaultIncomeEntryFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date is not null
        defaultIncomeEntryFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date is greater than or equal to
        defaultIncomeEntryFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date is less than or equal to
        defaultIncomeEntryFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date is less than
        defaultIncomeEntryFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where date is greater than
        defaultIncomeEntryFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceiptNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receiptNo equals to
        defaultIncomeEntryFiltering("receiptNo.equals=" + DEFAULT_RECEIPT_NO, "receiptNo.equals=" + UPDATED_RECEIPT_NO);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceiptNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receiptNo in
        defaultIncomeEntryFiltering("receiptNo.in=" + DEFAULT_RECEIPT_NO + "," + UPDATED_RECEIPT_NO, "receiptNo.in=" + UPDATED_RECEIPT_NO);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceiptNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receiptNo is not null
        defaultIncomeEntryFiltering("receiptNo.specified=true", "receiptNo.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceiptNoContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receiptNo contains
        defaultIncomeEntryFiltering("receiptNo.contains=" + DEFAULT_RECEIPT_NO, "receiptNo.contains=" + UPDATED_RECEIPT_NO);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceiptNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receiptNo does not contain
        defaultIncomeEntryFiltering("receiptNo.doesNotContain=" + UPDATED_RECEIPT_NO, "receiptNo.doesNotContain=" + DEFAULT_RECEIPT_NO);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where description equals to
        defaultIncomeEntryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where description in
        defaultIncomeEntryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where description is not null
        defaultIncomeEntryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where description contains
        defaultIncomeEntryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where description does not contain
        defaultIncomeEntryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeType equals to
        defaultIncomeEntryFiltering("incomeType.equals=" + DEFAULT_INCOME_TYPE, "incomeType.equals=" + UPDATED_INCOME_TYPE);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeType in
        defaultIncomeEntryFiltering(
            "incomeType.in=" + DEFAULT_INCOME_TYPE + "," + UPDATED_INCOME_TYPE,
            "incomeType.in=" + UPDATED_INCOME_TYPE
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByIncomeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where incomeType is not null
        defaultIncomeEntryFiltering("incomeType.specified=true", "incomeType.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount equals to
        defaultIncomeEntryFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount in
        defaultIncomeEntryFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount is not null
        defaultIncomeEntryFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount is greater than or equal to
        defaultIncomeEntryFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount is less than or equal to
        defaultIncomeEntryFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount is less than
        defaultIncomeEntryFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where amount is greater than
        defaultIncomeEntryFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where paymentMethod equals to
        defaultIncomeEntryFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where paymentMethod in
        defaultIncomeEntryFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where paymentMethod is not null
        defaultIncomeEntryFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivablePersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivablePerson equals to
        defaultIncomeEntryFiltering(
            "receivablePerson.equals=" + DEFAULT_RECEIVABLE_PERSON,
            "receivablePerson.equals=" + UPDATED_RECEIVABLE_PERSON
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivablePersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivablePerson in
        defaultIncomeEntryFiltering(
            "receivablePerson.in=" + DEFAULT_RECEIVABLE_PERSON + "," + UPDATED_RECEIVABLE_PERSON,
            "receivablePerson.in=" + UPDATED_RECEIVABLE_PERSON
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivablePersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivablePerson is not null
        defaultIncomeEntryFiltering("receivablePerson.specified=true", "receivablePerson.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivablePersonContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivablePerson contains
        defaultIncomeEntryFiltering(
            "receivablePerson.contains=" + DEFAULT_RECEIVABLE_PERSON,
            "receivablePerson.contains=" + UPDATED_RECEIVABLE_PERSON
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivablePersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivablePerson does not contain
        defaultIncomeEntryFiltering(
            "receivablePerson.doesNotContain=" + UPDATED_RECEIVABLE_PERSON,
            "receivablePerson.doesNotContain=" + DEFAULT_RECEIVABLE_PERSON
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivedBy equals to
        defaultIncomeEntryFiltering("receivedBy.equals=" + DEFAULT_RECEIVED_BY, "receivedBy.equals=" + UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivedBy in
        defaultIncomeEntryFiltering(
            "receivedBy.in=" + DEFAULT_RECEIVED_BY + "," + UPDATED_RECEIVED_BY,
            "receivedBy.in=" + UPDATED_RECEIVED_BY
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivedBy is not null
        defaultIncomeEntryFiltering("receivedBy.specified=true", "receivedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivedByContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivedBy contains
        defaultIncomeEntryFiltering("receivedBy.contains=" + DEFAULT_RECEIVED_BY, "receivedBy.contains=" + UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesByReceivedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where receivedBy does not contain
        defaultIncomeEntryFiltering("receivedBy.doesNotContain=" + UPDATED_RECEIVED_BY, "receivedBy.doesNotContain=" + DEFAULT_RECEIVED_BY);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesBySyncStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where syncStatus equals to
        defaultIncomeEntryFiltering("syncStatus.equals=" + DEFAULT_SYNC_STATUS, "syncStatus.equals=" + UPDATED_SYNC_STATUS);
    }

    @Test
    @Transactional
    void getAllIncomeEntriesBySyncStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where syncStatus in
        defaultIncomeEntryFiltering(
            "syncStatus.in=" + DEFAULT_SYNC_STATUS + "," + UPDATED_SYNC_STATUS,
            "syncStatus.in=" + UPDATED_SYNC_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIncomeEntriesBySyncStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        // Get all the incomeEntryList where syncStatus is not null
        defaultIncomeEntryFiltering("syncStatus.specified=true", "syncStatus.specified=false");
    }

    private void defaultIncomeEntryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIncomeEntryShouldBeFound(shouldBeFound);
        defaultIncomeEntryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIncomeEntryShouldBeFound(String filter) throws Exception {
        restIncomeEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].incomeCode").value(hasItem(DEFAULT_INCOME_CODE)))
            .andExpect(jsonPath("$.[*].createdByUsername").value(hasItem(DEFAULT_CREATED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptNo").value(hasItem(DEFAULT_RECEIPT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].incomeType").value(hasItem(DEFAULT_INCOME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].receivablePerson").value(hasItem(DEFAULT_RECEIVABLE_PERSON)))
            .andExpect(jsonPath("$.[*].receivedBy").value(hasItem(DEFAULT_RECEIVED_BY)))
            .andExpect(jsonPath("$.[*].syncStatus").value(hasItem(DEFAULT_SYNC_STATUS.toString())));

        // Check, that the count call also returns 1
        restIncomeEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIncomeEntryShouldNotBeFound(String filter) throws Exception {
        restIncomeEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIncomeEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIncomeEntry() throws Exception {
        // Get the incomeEntry
        restIncomeEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncomeEntry() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomeEntrySearchRepository.save(incomeEntry);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());

        // Update the incomeEntry
        IncomeEntry updatedIncomeEntry = incomeEntryRepository.findById(incomeEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIncomeEntry are not directly saved in db
        em.detach(updatedIncomeEntry);
        updatedIncomeEntry
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .incomeCode(UPDATED_INCOME_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .receiptNo(UPDATED_RECEIPT_NO)
            .description(UPDATED_DESCRIPTION)
            .incomeType(UPDATED_INCOME_TYPE)
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .receivablePerson(UPDATED_RECEIVABLE_PERSON)
            .receivedBy(UPDATED_RECEIVED_BY)
            .syncStatus(UPDATED_SYNC_STATUS);
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(updatedIncomeEntry);

        restIncomeEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomeEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incomeEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIncomeEntryToMatchAllProperties(updatedIncomeEntry);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<IncomeEntry> incomeEntrySearchList = Streamable.of(incomeEntrySearchRepository.findAll()).toList();
                IncomeEntry testIncomeEntrySearch = incomeEntrySearchList.get(searchDatabaseSizeAfter - 1);

                assertIncomeEntryAllPropertiesEquals(testIncomeEntrySearch, updatedIncomeEntry);
            });
    }

    @Test
    @Transactional
    void putNonExistingIncomeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        incomeEntry.setId(longCount.incrementAndGet());

        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomeEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incomeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncomeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        incomeEntry.setId(longCount.incrementAndGet());

        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incomeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncomeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        incomeEntry.setId(longCount.incrementAndGet());

        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incomeEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateIncomeEntryWithPatch() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incomeEntry using partial update
        IncomeEntry partialUpdatedIncomeEntry = new IncomeEntry();
        partialUpdatedIncomeEntry.setId(incomeEntry.getId());

        partialUpdatedIncomeEntry
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .incomeCode(UPDATED_INCOME_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .incomeType(UPDATED_INCOME_TYPE)
            .amount(UPDATED_AMOUNT)
            .receivedBy(UPDATED_RECEIVED_BY);

        restIncomeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncomeEntry))
            )
            .andExpect(status().isOk());

        // Validate the IncomeEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncomeEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIncomeEntry, incomeEntry),
            getPersistedIncomeEntry(incomeEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateIncomeEntryWithPatch() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incomeEntry using partial update
        IncomeEntry partialUpdatedIncomeEntry = new IncomeEntry();
        partialUpdatedIncomeEntry.setId(incomeEntry.getId());

        partialUpdatedIncomeEntry
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .incomeCode(UPDATED_INCOME_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .receiptNo(UPDATED_RECEIPT_NO)
            .description(UPDATED_DESCRIPTION)
            .incomeType(UPDATED_INCOME_TYPE)
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .receivablePerson(UPDATED_RECEIVABLE_PERSON)
            .receivedBy(UPDATED_RECEIVED_BY)
            .syncStatus(UPDATED_SYNC_STATUS);

        restIncomeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncomeEntry))
            )
            .andExpect(status().isOk());

        // Validate the IncomeEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncomeEntryUpdatableFieldsEquals(partialUpdatedIncomeEntry, getPersistedIncomeEntry(partialUpdatedIncomeEntry));
    }

    @Test
    @Transactional
    void patchNonExistingIncomeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        incomeEntry.setId(longCount.incrementAndGet());

        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incomeEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incomeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncomeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        incomeEntry.setId(longCount.incrementAndGet());

        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incomeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncomeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        incomeEntry.setId(longCount.incrementAndGet());

        // Create the IncomeEntry
        IncomeEntryDTO incomeEntryDTO = incomeEntryMapper.toDto(incomeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(incomeEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteIncomeEntry() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);
        incomeEntryRepository.save(incomeEntry);
        incomeEntrySearchRepository.save(incomeEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the incomeEntry
        restIncomeEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, incomeEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(incomeEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchIncomeEntry() throws Exception {
        // Initialize the database
        insertedIncomeEntry = incomeEntryRepository.saveAndFlush(incomeEntry);
        incomeEntrySearchRepository.save(incomeEntry);

        // Search the incomeEntry
        restIncomeEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + incomeEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].incomeCode").value(hasItem(DEFAULT_INCOME_CODE)))
            .andExpect(jsonPath("$.[*].createdByUsername").value(hasItem(DEFAULT_CREATED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptNo").value(hasItem(DEFAULT_RECEIPT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].incomeType").value(hasItem(DEFAULT_INCOME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].receivablePerson").value(hasItem(DEFAULT_RECEIVABLE_PERSON)))
            .andExpect(jsonPath("$.[*].receivedBy").value(hasItem(DEFAULT_RECEIVED_BY)))
            .andExpect(jsonPath("$.[*].syncStatus").value(hasItem(DEFAULT_SYNC_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return incomeEntryRepository.count();
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

    protected IncomeEntry getPersistedIncomeEntry(IncomeEntry incomeEntry) {
        return incomeEntryRepository.findById(incomeEntry.getId()).orElseThrow();
    }

    protected void assertPersistedIncomeEntryToMatchAllProperties(IncomeEntry expectedIncomeEntry) {
        assertIncomeEntryAllPropertiesEquals(expectedIncomeEntry, getPersistedIncomeEntry(expectedIncomeEntry));
    }

    protected void assertPersistedIncomeEntryToMatchUpdatableProperties(IncomeEntry expectedIncomeEntry) {
        assertIncomeEntryAllUpdatablePropertiesEquals(expectedIncomeEntry, getPersistedIncomeEntry(expectedIncomeEntry));
    }
}
