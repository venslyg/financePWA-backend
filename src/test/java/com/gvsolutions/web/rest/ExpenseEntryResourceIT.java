package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.ExpenseEntryAsserts.*;
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
import com.gvsolutions.domain.ExpenseEntry;
import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import com.gvsolutions.repository.ExpenseEntryRepository;
import com.gvsolutions.repository.search.ExpenseEntrySearchRepository;
import com.gvsolutions.service.dto.ExpenseEntryDTO;
import com.gvsolutions.service.mapper.ExpenseEntryMapper;
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
 * Integration tests for the {@link ExpenseEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseEntryResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_SUB_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_SUB_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_USERNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_VOUCHER_NO = "AAAAAAAAAA";
    private static final String UPDATED_VOUCHER_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final PaymentMode DEFAULT_PAYMENT_MODE = PaymentMode.CASH;
    private static final PaymentMode UPDATED_PAYMENT_MODE = PaymentMode.BANK;

    private static final ApprovalStatus DEFAULT_APPROVAL_STATUS = ApprovalStatus.APPROVED;
    private static final ApprovalStatus UPDATED_APPROVAL_STATUS = ApprovalStatus.DECLINED;

    private static final String DEFAULT_APPROVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final SyncStatus DEFAULT_SYNC_STATUS = SyncStatus.SYNCED;
    private static final SyncStatus UPDATED_SYNC_STATUS = SyncStatus.PENDING_OFFLINE;

    private static final String ENTITY_API_URL = "/api/expense-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/expense-entries/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpenseEntryRepository expenseEntryRepository;

    @Autowired
    private ExpenseEntryMapper expenseEntryMapper;

    @Autowired
    private ExpenseEntrySearchRepository expenseEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseEntryMockMvc;

    private ExpenseEntry expenseEntry;

    private ExpenseEntry insertedExpenseEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseEntry createEntity() {
        return new ExpenseEntry()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .accountCode(DEFAULT_ACCOUNT_CODE)
            .expenseCode(DEFAULT_EXPENSE_CODE)
            .expenseCategoryCode(DEFAULT_EXPENSE_CATEGORY_CODE)
            .expenseSubCategoryCode(DEFAULT_EXPENSE_SUB_CATEGORY_CODE)
            .createdByUsername(DEFAULT_CREATED_BY_USERNAME)
            .date(DEFAULT_DATE)
            .voucherNo(DEFAULT_VOUCHER_NO)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .paymentMode(DEFAULT_PAYMENT_MODE)
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .approvedBy(DEFAULT_APPROVED_BY)
            .vendor(DEFAULT_VENDOR)
            .syncStatus(DEFAULT_SYNC_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseEntry createUpdatedEntity() {
        return new ExpenseEntry()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseCategoryCode(UPDATED_EXPENSE_CATEGORY_CODE)
            .expenseSubCategoryCode(UPDATED_EXPENSE_SUB_CATEGORY_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .voucherNo(UPDATED_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .vendor(UPDATED_VENDOR)
            .syncStatus(UPDATED_SYNC_STATUS);
    }

    @BeforeEach
    void initTest() {
        expenseEntry = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExpenseEntry != null) {
            expenseEntryRepository.delete(insertedExpenseEntry);
            expenseEntrySearchRepository.delete(insertedExpenseEntry);
            insertedExpenseEntry = null;
        }
    }

    @Test
    @Transactional
    void createExpenseEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);
        var returnedExpenseEntryDTO = om.readValue(
            restExpenseEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpenseEntryDTO.class
        );

        // Validate the ExpenseEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExpenseEntry = expenseEntryMapper.toEntity(returnedExpenseEntryDTO);
        assertExpenseEntryUpdatableFieldsEquals(returnedExpenseEntry, getPersistedExpenseEntry(returnedExpenseEntry));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedExpenseEntry = returnedExpenseEntry;
    }

    @Test
    @Transactional
    void createExpenseEntryWithExistingId() throws Exception {
        // Create the ExpenseEntry with an existing ID
        expenseEntry.setId(1L);
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllExpenseEntries() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList
        restExpenseEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseCategoryCode").value(hasItem(DEFAULT_EXPENSE_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].expenseSubCategoryCode").value(hasItem(DEFAULT_EXPENSE_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].createdByUsername").value(hasItem(DEFAULT_CREATED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].syncStatus").value(hasItem(DEFAULT_SYNC_STATUS.toString())));
    }

    @Test
    @Transactional
    void getExpenseEntry() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get the expenseEntry
        restExpenseEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseEntry.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.accountCode").value(DEFAULT_ACCOUNT_CODE))
            .andExpect(jsonPath("$.expenseCode").value(DEFAULT_EXPENSE_CODE))
            .andExpect(jsonPath("$.expenseCategoryCode").value(DEFAULT_EXPENSE_CATEGORY_CODE))
            .andExpect(jsonPath("$.expenseSubCategoryCode").value(DEFAULT_EXPENSE_SUB_CATEGORY_CODE))
            .andExpect(jsonPath("$.createdByUsername").value(DEFAULT_CREATED_BY_USERNAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.voucherNo").value(DEFAULT_VOUCHER_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE.toString()))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS.toString()))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR))
            .andExpect(jsonPath("$.syncStatus").value(DEFAULT_SYNC_STATUS.toString()));
    }

    @Test
    @Transactional
    void getExpenseEntriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        Long id = expenseEntry.getId();

        defaultExpenseEntryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExpenseEntryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExpenseEntryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchCode equals to
        defaultExpenseEntryFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchCode in
        defaultExpenseEntryFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchCode is not null
        defaultExpenseEntryFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchCode contains
        defaultExpenseEntryFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchCode does not contain
        defaultExpenseEntryFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchId equals to
        defaultExpenseEntryFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchId in
        defaultExpenseEntryFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchId is not null
        defaultExpenseEntryFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchId contains
        defaultExpenseEntryFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where branchId does not contain
        defaultExpenseEntryFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAccountCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where accountCode equals to
        defaultExpenseEntryFiltering("accountCode.equals=" + DEFAULT_ACCOUNT_CODE, "accountCode.equals=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAccountCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where accountCode in
        defaultExpenseEntryFiltering(
            "accountCode.in=" + DEFAULT_ACCOUNT_CODE + "," + UPDATED_ACCOUNT_CODE,
            "accountCode.in=" + UPDATED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAccountCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where accountCode is not null
        defaultExpenseEntryFiltering("accountCode.specified=true", "accountCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAccountCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where accountCode contains
        defaultExpenseEntryFiltering("accountCode.contains=" + DEFAULT_ACCOUNT_CODE, "accountCode.contains=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAccountCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where accountCode does not contain
        defaultExpenseEntryFiltering(
            "accountCode.doesNotContain=" + UPDATED_ACCOUNT_CODE,
            "accountCode.doesNotContain=" + DEFAULT_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCode equals to
        defaultExpenseEntryFiltering("expenseCode.equals=" + DEFAULT_EXPENSE_CODE, "expenseCode.equals=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCode in
        defaultExpenseEntryFiltering(
            "expenseCode.in=" + DEFAULT_EXPENSE_CODE + "," + UPDATED_EXPENSE_CODE,
            "expenseCode.in=" + UPDATED_EXPENSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCode is not null
        defaultExpenseEntryFiltering("expenseCode.specified=true", "expenseCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCode contains
        defaultExpenseEntryFiltering("expenseCode.contains=" + DEFAULT_EXPENSE_CODE, "expenseCode.contains=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCode does not contain
        defaultExpenseEntryFiltering(
            "expenseCode.doesNotContain=" + UPDATED_EXPENSE_CODE,
            "expenseCode.doesNotContain=" + DEFAULT_EXPENSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCategoryCode equals to
        defaultExpenseEntryFiltering(
            "expenseCategoryCode.equals=" + DEFAULT_EXPENSE_CATEGORY_CODE,
            "expenseCategoryCode.equals=" + UPDATED_EXPENSE_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCategoryCode in
        defaultExpenseEntryFiltering(
            "expenseCategoryCode.in=" + DEFAULT_EXPENSE_CATEGORY_CODE + "," + UPDATED_EXPENSE_CATEGORY_CODE,
            "expenseCategoryCode.in=" + UPDATED_EXPENSE_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCategoryCode is not null
        defaultExpenseEntryFiltering("expenseCategoryCode.specified=true", "expenseCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCategoryCode contains
        defaultExpenseEntryFiltering(
            "expenseCategoryCode.contains=" + DEFAULT_EXPENSE_CATEGORY_CODE,
            "expenseCategoryCode.contains=" + UPDATED_EXPENSE_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseCategoryCode does not contain
        defaultExpenseEntryFiltering(
            "expenseCategoryCode.doesNotContain=" + UPDATED_EXPENSE_CATEGORY_CODE,
            "expenseCategoryCode.doesNotContain=" + DEFAULT_EXPENSE_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseSubCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseSubCategoryCode equals to
        defaultExpenseEntryFiltering(
            "expenseSubCategoryCode.equals=" + DEFAULT_EXPENSE_SUB_CATEGORY_CODE,
            "expenseSubCategoryCode.equals=" + UPDATED_EXPENSE_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseSubCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseSubCategoryCode in
        defaultExpenseEntryFiltering(
            "expenseSubCategoryCode.in=" + DEFAULT_EXPENSE_SUB_CATEGORY_CODE + "," + UPDATED_EXPENSE_SUB_CATEGORY_CODE,
            "expenseSubCategoryCode.in=" + UPDATED_EXPENSE_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseSubCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseSubCategoryCode is not null
        defaultExpenseEntryFiltering("expenseSubCategoryCode.specified=true", "expenseSubCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseSubCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseSubCategoryCode contains
        defaultExpenseEntryFiltering(
            "expenseSubCategoryCode.contains=" + DEFAULT_EXPENSE_SUB_CATEGORY_CODE,
            "expenseSubCategoryCode.contains=" + UPDATED_EXPENSE_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByExpenseSubCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where expenseSubCategoryCode does not contain
        defaultExpenseEntryFiltering(
            "expenseSubCategoryCode.doesNotContain=" + UPDATED_EXPENSE_SUB_CATEGORY_CODE,
            "expenseSubCategoryCode.doesNotContain=" + DEFAULT_EXPENSE_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByCreatedByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where createdByUsername equals to
        defaultExpenseEntryFiltering(
            "createdByUsername.equals=" + DEFAULT_CREATED_BY_USERNAME,
            "createdByUsername.equals=" + UPDATED_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByCreatedByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where createdByUsername in
        defaultExpenseEntryFiltering(
            "createdByUsername.in=" + DEFAULT_CREATED_BY_USERNAME + "," + UPDATED_CREATED_BY_USERNAME,
            "createdByUsername.in=" + UPDATED_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByCreatedByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where createdByUsername is not null
        defaultExpenseEntryFiltering("createdByUsername.specified=true", "createdByUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByCreatedByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where createdByUsername contains
        defaultExpenseEntryFiltering(
            "createdByUsername.contains=" + DEFAULT_CREATED_BY_USERNAME,
            "createdByUsername.contains=" + UPDATED_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByCreatedByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where createdByUsername does not contain
        defaultExpenseEntryFiltering(
            "createdByUsername.doesNotContain=" + UPDATED_CREATED_BY_USERNAME,
            "createdByUsername.doesNotContain=" + DEFAULT_CREATED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date equals to
        defaultExpenseEntryFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date in
        defaultExpenseEntryFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date is not null
        defaultExpenseEntryFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date is greater than or equal to
        defaultExpenseEntryFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date is less than or equal to
        defaultExpenseEntryFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date is less than
        defaultExpenseEntryFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where date is greater than
        defaultExpenseEntryFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where voucherNo equals to
        defaultExpenseEntryFiltering("voucherNo.equals=" + DEFAULT_VOUCHER_NO, "voucherNo.equals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where voucherNo in
        defaultExpenseEntryFiltering("voucherNo.in=" + DEFAULT_VOUCHER_NO + "," + UPDATED_VOUCHER_NO, "voucherNo.in=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where voucherNo is not null
        defaultExpenseEntryFiltering("voucherNo.specified=true", "voucherNo.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVoucherNoContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where voucherNo contains
        defaultExpenseEntryFiltering("voucherNo.contains=" + DEFAULT_VOUCHER_NO, "voucherNo.contains=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVoucherNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where voucherNo does not contain
        defaultExpenseEntryFiltering("voucherNo.doesNotContain=" + UPDATED_VOUCHER_NO, "voucherNo.doesNotContain=" + DEFAULT_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where description equals to
        defaultExpenseEntryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where description in
        defaultExpenseEntryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where description is not null
        defaultExpenseEntryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where description contains
        defaultExpenseEntryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where description does not contain
        defaultExpenseEntryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount equals to
        defaultExpenseEntryFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount in
        defaultExpenseEntryFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount is not null
        defaultExpenseEntryFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount is greater than or equal to
        defaultExpenseEntryFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount is less than or equal to
        defaultExpenseEntryFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount is less than
        defaultExpenseEntryFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where amount is greater than
        defaultExpenseEntryFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByPaymentModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where paymentMode equals to
        defaultExpenseEntryFiltering("paymentMode.equals=" + DEFAULT_PAYMENT_MODE, "paymentMode.equals=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByPaymentModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where paymentMode in
        defaultExpenseEntryFiltering(
            "paymentMode.in=" + DEFAULT_PAYMENT_MODE + "," + UPDATED_PAYMENT_MODE,
            "paymentMode.in=" + UPDATED_PAYMENT_MODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByPaymentModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where paymentMode is not null
        defaultExpenseEntryFiltering("paymentMode.specified=true", "paymentMode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvalStatus equals to
        defaultExpenseEntryFiltering(
            "approvalStatus.equals=" + DEFAULT_APPROVAL_STATUS,
            "approvalStatus.equals=" + UPDATED_APPROVAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvalStatus in
        defaultExpenseEntryFiltering(
            "approvalStatus.in=" + DEFAULT_APPROVAL_STATUS + "," + UPDATED_APPROVAL_STATUS,
            "approvalStatus.in=" + UPDATED_APPROVAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvalStatus is not null
        defaultExpenseEntryFiltering("approvalStatus.specified=true", "approvalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvedBy equals to
        defaultExpenseEntryFiltering("approvedBy.equals=" + DEFAULT_APPROVED_BY, "approvedBy.equals=" + UPDATED_APPROVED_BY);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvedBy in
        defaultExpenseEntryFiltering(
            "approvedBy.in=" + DEFAULT_APPROVED_BY + "," + UPDATED_APPROVED_BY,
            "approvedBy.in=" + UPDATED_APPROVED_BY
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvedBy is not null
        defaultExpenseEntryFiltering("approvedBy.specified=true", "approvedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovedByContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvedBy contains
        defaultExpenseEntryFiltering("approvedBy.contains=" + DEFAULT_APPROVED_BY, "approvedBy.contains=" + UPDATED_APPROVED_BY);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByApprovedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where approvedBy does not contain
        defaultExpenseEntryFiltering(
            "approvedBy.doesNotContain=" + UPDATED_APPROVED_BY,
            "approvedBy.doesNotContain=" + DEFAULT_APPROVED_BY
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVendorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where vendor equals to
        defaultExpenseEntryFiltering("vendor.equals=" + DEFAULT_VENDOR, "vendor.equals=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVendorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where vendor in
        defaultExpenseEntryFiltering("vendor.in=" + DEFAULT_VENDOR + "," + UPDATED_VENDOR, "vendor.in=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVendorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where vendor is not null
        defaultExpenseEntryFiltering("vendor.specified=true", "vendor.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVendorContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where vendor contains
        defaultExpenseEntryFiltering("vendor.contains=" + DEFAULT_VENDOR, "vendor.contains=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesByVendorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where vendor does not contain
        defaultExpenseEntryFiltering("vendor.doesNotContain=" + UPDATED_VENDOR, "vendor.doesNotContain=" + DEFAULT_VENDOR);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesBySyncStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where syncStatus equals to
        defaultExpenseEntryFiltering("syncStatus.equals=" + DEFAULT_SYNC_STATUS, "syncStatus.equals=" + UPDATED_SYNC_STATUS);
    }

    @Test
    @Transactional
    void getAllExpenseEntriesBySyncStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where syncStatus in
        defaultExpenseEntryFiltering(
            "syncStatus.in=" + DEFAULT_SYNC_STATUS + "," + UPDATED_SYNC_STATUS,
            "syncStatus.in=" + UPDATED_SYNC_STATUS
        );
    }

    @Test
    @Transactional
    void getAllExpenseEntriesBySyncStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        // Get all the expenseEntryList where syncStatus is not null
        defaultExpenseEntryFiltering("syncStatus.specified=true", "syncStatus.specified=false");
    }

    private void defaultExpenseEntryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExpenseEntryShouldBeFound(shouldBeFound);
        defaultExpenseEntryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseEntryShouldBeFound(String filter) throws Exception {
        restExpenseEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseCategoryCode").value(hasItem(DEFAULT_EXPENSE_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].expenseSubCategoryCode").value(hasItem(DEFAULT_EXPENSE_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].createdByUsername").value(hasItem(DEFAULT_CREATED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].syncStatus").value(hasItem(DEFAULT_SYNC_STATUS.toString())));

        // Check, that the count call also returns 1
        restExpenseEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseEntryShouldNotBeFound(String filter) throws Exception {
        restExpenseEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpenseEntry() throws Exception {
        // Get the expenseEntry
        restExpenseEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseEntry() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseEntrySearchRepository.save(expenseEntry);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());

        // Update the expenseEntry
        ExpenseEntry updatedExpenseEntry = expenseEntryRepository.findById(expenseEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpenseEntry are not directly saved in db
        em.detach(updatedExpenseEntry);
        updatedExpenseEntry
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseCategoryCode(UPDATED_EXPENSE_CATEGORY_CODE)
            .expenseSubCategoryCode(UPDATED_EXPENSE_SUB_CATEGORY_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .voucherNo(UPDATED_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .vendor(UPDATED_VENDOR)
            .syncStatus(UPDATED_SYNC_STATUS);
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(updatedExpenseEntry);

        restExpenseEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpenseEntryToMatchAllProperties(updatedExpenseEntry);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ExpenseEntry> expenseEntrySearchList = Streamable.of(expenseEntrySearchRepository.findAll()).toList();
                ExpenseEntry testExpenseEntrySearch = expenseEntrySearchList.get(searchDatabaseSizeAfter - 1);

                assertExpenseEntryAllPropertiesEquals(testExpenseEntrySearch, updatedExpenseEntry);
            });
    }

    @Test
    @Transactional
    void putNonExistingExpenseEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        expenseEntry.setId(longCount.incrementAndGet());

        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        expenseEntry.setId(longCount.incrementAndGet());

        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        expenseEntry.setId(longCount.incrementAndGet());

        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateExpenseEntryWithPatch() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseEntry using partial update
        ExpenseEntry partialUpdatedExpenseEntry = new ExpenseEntry();
        partialUpdatedExpenseEntry.setId(expenseEntry.getId());

        partialUpdatedExpenseEntry
            .expenseCode(UPDATED_EXPENSE_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .voucherNo(UPDATED_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .vendor(UPDATED_VENDOR);

        restExpenseEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseEntry))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExpenseEntry, expenseEntry),
            getPersistedExpenseEntry(expenseEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateExpenseEntryWithPatch() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseEntry using partial update
        ExpenseEntry partialUpdatedExpenseEntry = new ExpenseEntry();
        partialUpdatedExpenseEntry.setId(expenseEntry.getId());

        partialUpdatedExpenseEntry
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseCategoryCode(UPDATED_EXPENSE_CATEGORY_CODE)
            .expenseSubCategoryCode(UPDATED_EXPENSE_SUB_CATEGORY_CODE)
            .createdByUsername(UPDATED_CREATED_BY_USERNAME)
            .date(UPDATED_DATE)
            .voucherNo(UPDATED_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .vendor(UPDATED_VENDOR)
            .syncStatus(UPDATED_SYNC_STATUS);

        restExpenseEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseEntry))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseEntryUpdatableFieldsEquals(partialUpdatedExpenseEntry, getPersistedExpenseEntry(partialUpdatedExpenseEntry));
    }

    @Test
    @Transactional
    void patchNonExistingExpenseEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        expenseEntry.setId(longCount.incrementAndGet());

        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        expenseEntry.setId(longCount.incrementAndGet());

        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        expenseEntry.setId(longCount.incrementAndGet());

        // Create the ExpenseEntry
        ExpenseEntryDTO expenseEntryDTO = expenseEntryMapper.toDto(expenseEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expenseEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteExpenseEntry() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);
        expenseEntryRepository.save(expenseEntry);
        expenseEntrySearchRepository.save(expenseEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the expenseEntry
        restExpenseEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseEntrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchExpenseEntry() throws Exception {
        // Initialize the database
        insertedExpenseEntry = expenseEntryRepository.saveAndFlush(expenseEntry);
        expenseEntrySearchRepository.save(expenseEntry);

        // Search the expenseEntry
        restExpenseEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + expenseEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseCategoryCode").value(hasItem(DEFAULT_EXPENSE_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].expenseSubCategoryCode").value(hasItem(DEFAULT_EXPENSE_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].createdByUsername").value(hasItem(DEFAULT_CREATED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].syncStatus").value(hasItem(DEFAULT_SYNC_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return expenseEntryRepository.count();
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

    protected ExpenseEntry getPersistedExpenseEntry(ExpenseEntry expenseEntry) {
        return expenseEntryRepository.findById(expenseEntry.getId()).orElseThrow();
    }

    protected void assertPersistedExpenseEntryToMatchAllProperties(ExpenseEntry expectedExpenseEntry) {
        assertExpenseEntryAllPropertiesEquals(expectedExpenseEntry, getPersistedExpenseEntry(expectedExpenseEntry));
    }

    protected void assertPersistedExpenseEntryToMatchUpdatableProperties(ExpenseEntry expectedExpenseEntry) {
        assertExpenseEntryAllUpdatablePropertiesEquals(expectedExpenseEntry, getPersistedExpenseEntry(expectedExpenseEntry));
    }
}
