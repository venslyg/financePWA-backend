package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.BankLedgerAsserts.*;
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
import com.gvsolutions.domain.BankLedger;
import com.gvsolutions.repository.BankLedgerRepository;
import com.gvsolutions.repository.search.BankLedgerSearchRepository;
import com.gvsolutions.service.dto.BankLedgerDTO;
import com.gvsolutions.service.mapper.BankLedgerMapper;
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
 * Integration tests for the {@link BankLedgerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankLedgerResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_LEDGER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_LEDGER_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEPOSIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPOSIT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPOSIT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_WITHDRAWAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WITHDRAWAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_WITHDRAWAL_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RUNNING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RUNNING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RUNNING_BALANCE = new BigDecimal(1 - 1);

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bank-ledgers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/bank-ledgers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankLedgerRepository bankLedgerRepository;

    @Autowired
    private BankLedgerMapper bankLedgerMapper;

    @Autowired
    private BankLedgerSearchRepository bankLedgerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankLedgerMockMvc;

    private BankLedger bankLedger;

    private BankLedger insertedBankLedger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankLedger createEntity() {
        return new BankLedger()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .bankLedgerCode(DEFAULT_BANK_LEDGER_CODE)
            .date(DEFAULT_DATE)
            .referenceNo(DEFAULT_REFERENCE_NO)
            .description(DEFAULT_DESCRIPTION)
            .depositAmount(DEFAULT_DEPOSIT_AMOUNT)
            .withdrawalAmount(DEFAULT_WITHDRAWAL_AMOUNT)
            .runningBalance(DEFAULT_RUNNING_BALANCE)
            .remark(DEFAULT_REMARK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankLedger createUpdatedEntity() {
        return new BankLedger()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .bankLedgerCode(UPDATED_BANK_LEDGER_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .withdrawalAmount(UPDATED_WITHDRAWAL_AMOUNT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .remark(UPDATED_REMARK);
    }

    @BeforeEach
    void initTest() {
        bankLedger = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBankLedger != null) {
            bankLedgerRepository.delete(insertedBankLedger);
            bankLedgerSearchRepository.delete(insertedBankLedger);
            insertedBankLedger = null;
        }
    }

    @Test
    @Transactional
    void createBankLedger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);
        var returnedBankLedgerDTO = om.readValue(
            restBankLedgerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankLedgerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankLedgerDTO.class
        );

        // Validate the BankLedger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBankLedger = bankLedgerMapper.toEntity(returnedBankLedgerDTO);
        assertBankLedgerUpdatableFieldsEquals(returnedBankLedger, getPersistedBankLedger(returnedBankLedger));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBankLedger = returnedBankLedger;
    }

    @Test
    @Transactional
    void createBankLedgerWithExistingId() throws Exception {
        // Create the BankLedger with an existing ID
        bankLedger.setId(1L);
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankLedgerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBankLedgers() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList
        restBankLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].bankLedgerCode").value(hasItem(DEFAULT_BANK_LEDGER_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depositAmount").value(hasItem(sameNumber(DEFAULT_DEPOSIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].withdrawalAmount").value(hasItem(sameNumber(DEFAULT_WITHDRAWAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    @Test
    @Transactional
    void getBankLedger() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get the bankLedger
        restBankLedgerMockMvc
            .perform(get(ENTITY_API_URL_ID, bankLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankLedger.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.bankLedgerCode").value(DEFAULT_BANK_LEDGER_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.referenceNo").value(DEFAULT_REFERENCE_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.depositAmount").value(sameNumber(DEFAULT_DEPOSIT_AMOUNT)))
            .andExpect(jsonPath("$.withdrawalAmount").value(sameNumber(DEFAULT_WITHDRAWAL_AMOUNT)))
            .andExpect(jsonPath("$.runningBalance").value(sameNumber(DEFAULT_RUNNING_BALANCE)))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }

    @Test
    @Transactional
    void getBankLedgersByIdFiltering() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        Long id = bankLedger.getId();

        defaultBankLedgerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBankLedgerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBankLedgerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchCode equals to
        defaultBankLedgerFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchCode in
        defaultBankLedgerFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchCode is not null
        defaultBankLedgerFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchCode contains
        defaultBankLedgerFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchCode does not contain
        defaultBankLedgerFiltering("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE, "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchId equals to
        defaultBankLedgerFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchId in
        defaultBankLedgerFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchId is not null
        defaultBankLedgerFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchId contains
        defaultBankLedgerFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where branchId does not contain
        defaultBankLedgerFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBankLedgersByBankLedgerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where bankLedgerCode equals to
        defaultBankLedgerFiltering(
            "bankLedgerCode.equals=" + DEFAULT_BANK_LEDGER_CODE,
            "bankLedgerCode.equals=" + UPDATED_BANK_LEDGER_CODE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByBankLedgerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where bankLedgerCode in
        defaultBankLedgerFiltering(
            "bankLedgerCode.in=" + DEFAULT_BANK_LEDGER_CODE + "," + UPDATED_BANK_LEDGER_CODE,
            "bankLedgerCode.in=" + UPDATED_BANK_LEDGER_CODE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByBankLedgerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where bankLedgerCode is not null
        defaultBankLedgerFiltering("bankLedgerCode.specified=true", "bankLedgerCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByBankLedgerCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where bankLedgerCode contains
        defaultBankLedgerFiltering(
            "bankLedgerCode.contains=" + DEFAULT_BANK_LEDGER_CODE,
            "bankLedgerCode.contains=" + UPDATED_BANK_LEDGER_CODE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByBankLedgerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where bankLedgerCode does not contain
        defaultBankLedgerFiltering(
            "bankLedgerCode.doesNotContain=" + UPDATED_BANK_LEDGER_CODE,
            "bankLedgerCode.doesNotContain=" + DEFAULT_BANK_LEDGER_CODE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date equals to
        defaultBankLedgerFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date in
        defaultBankLedgerFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date is not null
        defaultBankLedgerFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date is greater than or equal to
        defaultBankLedgerFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date is less than or equal to
        defaultBankLedgerFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date is less than
        defaultBankLedgerFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where date is greater than
        defaultBankLedgerFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByReferenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where referenceNo equals to
        defaultBankLedgerFiltering("referenceNo.equals=" + DEFAULT_REFERENCE_NO, "referenceNo.equals=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllBankLedgersByReferenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where referenceNo in
        defaultBankLedgerFiltering(
            "referenceNo.in=" + DEFAULT_REFERENCE_NO + "," + UPDATED_REFERENCE_NO,
            "referenceNo.in=" + UPDATED_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByReferenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where referenceNo is not null
        defaultBankLedgerFiltering("referenceNo.specified=true", "referenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByReferenceNoContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where referenceNo contains
        defaultBankLedgerFiltering("referenceNo.contains=" + DEFAULT_REFERENCE_NO, "referenceNo.contains=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllBankLedgersByReferenceNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where referenceNo does not contain
        defaultBankLedgerFiltering(
            "referenceNo.doesNotContain=" + UPDATED_REFERENCE_NO,
            "referenceNo.doesNotContain=" + DEFAULT_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where description equals to
        defaultBankLedgerFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where description in
        defaultBankLedgerFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where description is not null
        defaultBankLedgerFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where description contains
        defaultBankLedgerFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where description does not contain
        defaultBankLedgerFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount equals to
        defaultBankLedgerFiltering("depositAmount.equals=" + DEFAULT_DEPOSIT_AMOUNT, "depositAmount.equals=" + UPDATED_DEPOSIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount in
        defaultBankLedgerFiltering(
            "depositAmount.in=" + DEFAULT_DEPOSIT_AMOUNT + "," + UPDATED_DEPOSIT_AMOUNT,
            "depositAmount.in=" + UPDATED_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount is not null
        defaultBankLedgerFiltering("depositAmount.specified=true", "depositAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount is greater than or equal to
        defaultBankLedgerFiltering(
            "depositAmount.greaterThanOrEqual=" + DEFAULT_DEPOSIT_AMOUNT,
            "depositAmount.greaterThanOrEqual=" + UPDATED_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount is less than or equal to
        defaultBankLedgerFiltering(
            "depositAmount.lessThanOrEqual=" + DEFAULT_DEPOSIT_AMOUNT,
            "depositAmount.lessThanOrEqual=" + SMALLER_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount is less than
        defaultBankLedgerFiltering("depositAmount.lessThan=" + UPDATED_DEPOSIT_AMOUNT, "depositAmount.lessThan=" + DEFAULT_DEPOSIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBankLedgersByDepositAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where depositAmount is greater than
        defaultBankLedgerFiltering(
            "depositAmount.greaterThan=" + SMALLER_DEPOSIT_AMOUNT,
            "depositAmount.greaterThan=" + DEFAULT_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount equals to
        defaultBankLedgerFiltering(
            "withdrawalAmount.equals=" + DEFAULT_WITHDRAWAL_AMOUNT,
            "withdrawalAmount.equals=" + UPDATED_WITHDRAWAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount in
        defaultBankLedgerFiltering(
            "withdrawalAmount.in=" + DEFAULT_WITHDRAWAL_AMOUNT + "," + UPDATED_WITHDRAWAL_AMOUNT,
            "withdrawalAmount.in=" + UPDATED_WITHDRAWAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount is not null
        defaultBankLedgerFiltering("withdrawalAmount.specified=true", "withdrawalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount is greater than or equal to
        defaultBankLedgerFiltering(
            "withdrawalAmount.greaterThanOrEqual=" + DEFAULT_WITHDRAWAL_AMOUNT,
            "withdrawalAmount.greaterThanOrEqual=" + UPDATED_WITHDRAWAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount is less than or equal to
        defaultBankLedgerFiltering(
            "withdrawalAmount.lessThanOrEqual=" + DEFAULT_WITHDRAWAL_AMOUNT,
            "withdrawalAmount.lessThanOrEqual=" + SMALLER_WITHDRAWAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount is less than
        defaultBankLedgerFiltering(
            "withdrawalAmount.lessThan=" + UPDATED_WITHDRAWAL_AMOUNT,
            "withdrawalAmount.lessThan=" + DEFAULT_WITHDRAWAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByWithdrawalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where withdrawalAmount is greater than
        defaultBankLedgerFiltering(
            "withdrawalAmount.greaterThan=" + SMALLER_WITHDRAWAL_AMOUNT,
            "withdrawalAmount.greaterThan=" + DEFAULT_WITHDRAWAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance equals to
        defaultBankLedgerFiltering("runningBalance.equals=" + DEFAULT_RUNNING_BALANCE, "runningBalance.equals=" + UPDATED_RUNNING_BALANCE);
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance in
        defaultBankLedgerFiltering(
            "runningBalance.in=" + DEFAULT_RUNNING_BALANCE + "," + UPDATED_RUNNING_BALANCE,
            "runningBalance.in=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance is not null
        defaultBankLedgerFiltering("runningBalance.specified=true", "runningBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance is greater than or equal to
        defaultBankLedgerFiltering(
            "runningBalance.greaterThanOrEqual=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.greaterThanOrEqual=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance is less than or equal to
        defaultBankLedgerFiltering(
            "runningBalance.lessThanOrEqual=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.lessThanOrEqual=" + SMALLER_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance is less than
        defaultBankLedgerFiltering(
            "runningBalance.lessThan=" + UPDATED_RUNNING_BALANCE,
            "runningBalance.lessThan=" + DEFAULT_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByRunningBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where runningBalance is greater than
        defaultBankLedgerFiltering(
            "runningBalance.greaterThan=" + SMALLER_RUNNING_BALANCE,
            "runningBalance.greaterThan=" + DEFAULT_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankLedgersByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where remark equals to
        defaultBankLedgerFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllBankLedgersByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where remark in
        defaultBankLedgerFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllBankLedgersByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where remark is not null
        defaultBankLedgerFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllBankLedgersByRemarkContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where remark contains
        defaultBankLedgerFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllBankLedgersByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        // Get all the bankLedgerList where remark does not contain
        defaultBankLedgerFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    private void defaultBankLedgerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBankLedgerShouldBeFound(shouldBeFound);
        defaultBankLedgerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankLedgerShouldBeFound(String filter) throws Exception {
        restBankLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].bankLedgerCode").value(hasItem(DEFAULT_BANK_LEDGER_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depositAmount").value(hasItem(sameNumber(DEFAULT_DEPOSIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].withdrawalAmount").value(hasItem(sameNumber(DEFAULT_WITHDRAWAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));

        // Check, that the count call also returns 1
        restBankLedgerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankLedgerShouldNotBeFound(String filter) throws Exception {
        restBankLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankLedgerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBankLedger() throws Exception {
        // Get the bankLedger
        restBankLedgerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBankLedger() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankLedgerSearchRepository.save(bankLedger);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());

        // Update the bankLedger
        BankLedger updatedBankLedger = bankLedgerRepository.findById(bankLedger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBankLedger are not directly saved in db
        em.detach(updatedBankLedger);
        updatedBankLedger
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .bankLedgerCode(UPDATED_BANK_LEDGER_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .withdrawalAmount(UPDATED_WITHDRAWAL_AMOUNT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .remark(UPDATED_REMARK);
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(updatedBankLedger);

        restBankLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankLedgerDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankLedgerToMatchAllProperties(updatedBankLedger);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BankLedger> bankLedgerSearchList = Streamable.of(bankLedgerSearchRepository.findAll()).toList();
                BankLedger testBankLedgerSearch = bankLedgerSearchList.get(searchDatabaseSizeAfter - 1);

                assertBankLedgerAllPropertiesEquals(testBankLedgerSearch, updatedBankLedger);
            });
    }

    @Test
    @Transactional
    void putNonExistingBankLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        bankLedger.setId(longCount.incrementAndGet());

        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        bankLedger.setId(longCount.incrementAndGet());

        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        bankLedger.setId(longCount.incrementAndGet());

        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankLedgerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankLedgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBankLedgerWithPatch() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankLedger using partial update
        BankLedger partialUpdatedBankLedger = new BankLedger();
        partialUpdatedBankLedger.setId(bankLedger.getId());

        partialUpdatedBankLedger
            .branchId(UPDATED_BRANCH_ID)
            .bankLedgerCode(UPDATED_BANK_LEDGER_CODE)
            .description(UPDATED_DESCRIPTION)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .runningBalance(UPDATED_RUNNING_BALANCE);

        restBankLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankLedger))
            )
            .andExpect(status().isOk());

        // Validate the BankLedger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankLedgerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBankLedger, bankLedger),
            getPersistedBankLedger(bankLedger)
        );
    }

    @Test
    @Transactional
    void fullUpdateBankLedgerWithPatch() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankLedger using partial update
        BankLedger partialUpdatedBankLedger = new BankLedger();
        partialUpdatedBankLedger.setId(bankLedger.getId());

        partialUpdatedBankLedger
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .bankLedgerCode(UPDATED_BANK_LEDGER_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .withdrawalAmount(UPDATED_WITHDRAWAL_AMOUNT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .remark(UPDATED_REMARK);

        restBankLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankLedger))
            )
            .andExpect(status().isOk());

        // Validate the BankLedger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankLedgerUpdatableFieldsEquals(partialUpdatedBankLedger, getPersistedBankLedger(partialUpdatedBankLedger));
    }

    @Test
    @Transactional
    void patchNonExistingBankLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        bankLedger.setId(longCount.incrementAndGet());

        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankLedgerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        bankLedger.setId(longCount.incrementAndGet());

        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        bankLedger.setId(longCount.incrementAndGet());

        // Create the BankLedger
        BankLedgerDTO bankLedgerDTO = bankLedgerMapper.toDto(bankLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankLedgerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankLedgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBankLedger() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);
        bankLedgerRepository.save(bankLedger);
        bankLedgerSearchRepository.save(bankLedger);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the bankLedger
        restBankLedgerMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankLedger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBankLedger() throws Exception {
        // Initialize the database
        insertedBankLedger = bankLedgerRepository.saveAndFlush(bankLedger);
        bankLedgerSearchRepository.save(bankLedger);

        // Search the bankLedger
        restBankLedgerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + bankLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].bankLedgerCode").value(hasItem(DEFAULT_BANK_LEDGER_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depositAmount").value(hasItem(sameNumber(DEFAULT_DEPOSIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].withdrawalAmount").value(hasItem(sameNumber(DEFAULT_WITHDRAWAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    protected long getRepositoryCount() {
        return bankLedgerRepository.count();
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

    protected BankLedger getPersistedBankLedger(BankLedger bankLedger) {
        return bankLedgerRepository.findById(bankLedger.getId()).orElseThrow();
    }

    protected void assertPersistedBankLedgerToMatchAllProperties(BankLedger expectedBankLedger) {
        assertBankLedgerAllPropertiesEquals(expectedBankLedger, getPersistedBankLedger(expectedBankLedger));
    }

    protected void assertPersistedBankLedgerToMatchUpdatableProperties(BankLedger expectedBankLedger) {
        assertBankLedgerAllUpdatablePropertiesEquals(expectedBankLedger, getPersistedBankLedger(expectedBankLedger));
    }
}
