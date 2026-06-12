package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.PettyCashLedgerAsserts.*;
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
import com.gvsolutions.domain.PettyCashLedger;
import com.gvsolutions.repository.PettyCashLedgerRepository;
import com.gvsolutions.repository.search.PettyCashLedgerSearchRepository;
import com.gvsolutions.service.dto.PettyCashLedgerDTO;
import com.gvsolutions.service.mapper.PettyCashLedgerMapper;
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
 * Integration tests for the {@link PettyCashLedgerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PettyCashLedgerResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PETTY_CASH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PETTY_CASH_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PETTY_CASH_VOUCHER_NO = "AAAAAAAAAA";
    private static final String UPDATED_PETTY_CASH_VOUCHER_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CASH_IN = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_IN = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_IN = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CASH_OUT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_OUT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_OUT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RUNNING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RUNNING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RUNNING_BALANCE = new BigDecimal(1 - 1);

    private static final String DEFAULT_LINKED_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LINKED_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/petty-cash-ledgers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/petty-cash-ledgers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PettyCashLedgerRepository pettyCashLedgerRepository;

    @Autowired
    private PettyCashLedgerMapper pettyCashLedgerMapper;

    @Autowired
    private PettyCashLedgerSearchRepository pettyCashLedgerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPettyCashLedgerMockMvc;

    private PettyCashLedger pettyCashLedger;

    private PettyCashLedger insertedPettyCashLedger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PettyCashLedger createEntity() {
        return new PettyCashLedger()
            .branchCode(DEFAULT_BRANCH_CODE)
            .pettyCashCode(DEFAULT_PETTY_CASH_CODE)
            .date(DEFAULT_DATE)
            .pettyCashVoucherNo(DEFAULT_PETTY_CASH_VOUCHER_NO)
            .description(DEFAULT_DESCRIPTION)
            .cashIn(DEFAULT_CASH_IN)
            .cashOut(DEFAULT_CASH_OUT)
            .runningBalance(DEFAULT_RUNNING_BALANCE)
            .linkedAccountCode(DEFAULT_LINKED_ACCOUNT_CODE)
            .referenceNo(DEFAULT_REFERENCE_NO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PettyCashLedger createUpdatedEntity() {
        return new PettyCashLedger()
            .branchCode(UPDATED_BRANCH_CODE)
            .pettyCashCode(UPDATED_PETTY_CASH_CODE)
            .date(UPDATED_DATE)
            .pettyCashVoucherNo(UPDATED_PETTY_CASH_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .cashIn(UPDATED_CASH_IN)
            .cashOut(UPDATED_CASH_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .linkedAccountCode(UPDATED_LINKED_ACCOUNT_CODE)
            .referenceNo(UPDATED_REFERENCE_NO);
    }

    @BeforeEach
    void initTest() {
        pettyCashLedger = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPettyCashLedger != null) {
            pettyCashLedgerRepository.delete(insertedPettyCashLedger);
            pettyCashLedgerSearchRepository.delete(insertedPettyCashLedger);
            insertedPettyCashLedger = null;
        }
    }

    @Test
    @Transactional
    void createPettyCashLedger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);
        var returnedPettyCashLedgerDTO = om.readValue(
            restPettyCashLedgerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pettyCashLedgerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PettyCashLedgerDTO.class
        );

        // Validate the PettyCashLedger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPettyCashLedger = pettyCashLedgerMapper.toEntity(returnedPettyCashLedgerDTO);
        assertPettyCashLedgerUpdatableFieldsEquals(returnedPettyCashLedger, getPersistedPettyCashLedger(returnedPettyCashLedger));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPettyCashLedger = returnedPettyCashLedger;
    }

    @Test
    @Transactional
    void createPettyCashLedgerWithExistingId() throws Exception {
        // Create the PettyCashLedger with an existing ID
        pettyCashLedger.setId(1L);
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPettyCashLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pettyCashLedgerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgers() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pettyCashLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].pettyCashCode").value(hasItem(DEFAULT_PETTY_CASH_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].pettyCashVoucherNo").value(hasItem(DEFAULT_PETTY_CASH_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cashIn").value(hasItem(sameNumber(DEFAULT_CASH_IN))))
            .andExpect(jsonPath("$.[*].cashOut").value(hasItem(sameNumber(DEFAULT_CASH_OUT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))))
            .andExpect(jsonPath("$.[*].linkedAccountCode").value(hasItem(DEFAULT_LINKED_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)));
    }

    @Test
    @Transactional
    void getPettyCashLedger() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get the pettyCashLedger
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_API_URL_ID, pettyCashLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pettyCashLedger.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.pettyCashCode").value(DEFAULT_PETTY_CASH_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.pettyCashVoucherNo").value(DEFAULT_PETTY_CASH_VOUCHER_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cashIn").value(sameNumber(DEFAULT_CASH_IN)))
            .andExpect(jsonPath("$.cashOut").value(sameNumber(DEFAULT_CASH_OUT)))
            .andExpect(jsonPath("$.runningBalance").value(sameNumber(DEFAULT_RUNNING_BALANCE)))
            .andExpect(jsonPath("$.linkedAccountCode").value(DEFAULT_LINKED_ACCOUNT_CODE))
            .andExpect(jsonPath("$.referenceNo").value(DEFAULT_REFERENCE_NO));
    }

    @Test
    @Transactional
    void getPettyCashLedgersByIdFiltering() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        Long id = pettyCashLedger.getId();

        defaultPettyCashLedgerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPettyCashLedgerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPettyCashLedgerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where branchCode equals to
        defaultPettyCashLedgerFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where branchCode in
        defaultPettyCashLedgerFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where branchCode is not null
        defaultPettyCashLedgerFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where branchCode contains
        defaultPettyCashLedgerFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where branchCode does not contain
        defaultPettyCashLedgerFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashCode equals to
        defaultPettyCashLedgerFiltering(
            "pettyCashCode.equals=" + DEFAULT_PETTY_CASH_CODE,
            "pettyCashCode.equals=" + UPDATED_PETTY_CASH_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashCode in
        defaultPettyCashLedgerFiltering(
            "pettyCashCode.in=" + DEFAULT_PETTY_CASH_CODE + "," + UPDATED_PETTY_CASH_CODE,
            "pettyCashCode.in=" + UPDATED_PETTY_CASH_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashCode is not null
        defaultPettyCashLedgerFiltering("pettyCashCode.specified=true", "pettyCashCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashCode contains
        defaultPettyCashLedgerFiltering(
            "pettyCashCode.contains=" + DEFAULT_PETTY_CASH_CODE,
            "pettyCashCode.contains=" + UPDATED_PETTY_CASH_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashCode does not contain
        defaultPettyCashLedgerFiltering(
            "pettyCashCode.doesNotContain=" + UPDATED_PETTY_CASH_CODE,
            "pettyCashCode.doesNotContain=" + DEFAULT_PETTY_CASH_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date equals to
        defaultPettyCashLedgerFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date in
        defaultPettyCashLedgerFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date is not null
        defaultPettyCashLedgerFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date is greater than or equal to
        defaultPettyCashLedgerFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date is less than or equal to
        defaultPettyCashLedgerFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date is less than
        defaultPettyCashLedgerFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where date is greater than
        defaultPettyCashLedgerFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashVoucherNo equals to
        defaultPettyCashLedgerFiltering(
            "pettyCashVoucherNo.equals=" + DEFAULT_PETTY_CASH_VOUCHER_NO,
            "pettyCashVoucherNo.equals=" + UPDATED_PETTY_CASH_VOUCHER_NO
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashVoucherNo in
        defaultPettyCashLedgerFiltering(
            "pettyCashVoucherNo.in=" + DEFAULT_PETTY_CASH_VOUCHER_NO + "," + UPDATED_PETTY_CASH_VOUCHER_NO,
            "pettyCashVoucherNo.in=" + UPDATED_PETTY_CASH_VOUCHER_NO
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashVoucherNo is not null
        defaultPettyCashLedgerFiltering("pettyCashVoucherNo.specified=true", "pettyCashVoucherNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashVoucherNoContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashVoucherNo contains
        defaultPettyCashLedgerFiltering(
            "pettyCashVoucherNo.contains=" + DEFAULT_PETTY_CASH_VOUCHER_NO,
            "pettyCashVoucherNo.contains=" + UPDATED_PETTY_CASH_VOUCHER_NO
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByPettyCashVoucherNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where pettyCashVoucherNo does not contain
        defaultPettyCashLedgerFiltering(
            "pettyCashVoucherNo.doesNotContain=" + UPDATED_PETTY_CASH_VOUCHER_NO,
            "pettyCashVoucherNo.doesNotContain=" + DEFAULT_PETTY_CASH_VOUCHER_NO
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where description equals to
        defaultPettyCashLedgerFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where description in
        defaultPettyCashLedgerFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where description is not null
        defaultPettyCashLedgerFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where description contains
        defaultPettyCashLedgerFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where description does not contain
        defaultPettyCashLedgerFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn equals to
        defaultPettyCashLedgerFiltering("cashIn.equals=" + DEFAULT_CASH_IN, "cashIn.equals=" + UPDATED_CASH_IN);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn in
        defaultPettyCashLedgerFiltering("cashIn.in=" + DEFAULT_CASH_IN + "," + UPDATED_CASH_IN, "cashIn.in=" + UPDATED_CASH_IN);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn is not null
        defaultPettyCashLedgerFiltering("cashIn.specified=true", "cashIn.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn is greater than or equal to
        defaultPettyCashLedgerFiltering("cashIn.greaterThanOrEqual=" + DEFAULT_CASH_IN, "cashIn.greaterThanOrEqual=" + UPDATED_CASH_IN);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn is less than or equal to
        defaultPettyCashLedgerFiltering("cashIn.lessThanOrEqual=" + DEFAULT_CASH_IN, "cashIn.lessThanOrEqual=" + SMALLER_CASH_IN);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn is less than
        defaultPettyCashLedgerFiltering("cashIn.lessThan=" + UPDATED_CASH_IN, "cashIn.lessThan=" + DEFAULT_CASH_IN);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashInIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashIn is greater than
        defaultPettyCashLedgerFiltering("cashIn.greaterThan=" + SMALLER_CASH_IN, "cashIn.greaterThan=" + DEFAULT_CASH_IN);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut equals to
        defaultPettyCashLedgerFiltering("cashOut.equals=" + DEFAULT_CASH_OUT, "cashOut.equals=" + UPDATED_CASH_OUT);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut in
        defaultPettyCashLedgerFiltering("cashOut.in=" + DEFAULT_CASH_OUT + "," + UPDATED_CASH_OUT, "cashOut.in=" + UPDATED_CASH_OUT);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut is not null
        defaultPettyCashLedgerFiltering("cashOut.specified=true", "cashOut.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut is greater than or equal to
        defaultPettyCashLedgerFiltering("cashOut.greaterThanOrEqual=" + DEFAULT_CASH_OUT, "cashOut.greaterThanOrEqual=" + UPDATED_CASH_OUT);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut is less than or equal to
        defaultPettyCashLedgerFiltering("cashOut.lessThanOrEqual=" + DEFAULT_CASH_OUT, "cashOut.lessThanOrEqual=" + SMALLER_CASH_OUT);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut is less than
        defaultPettyCashLedgerFiltering("cashOut.lessThan=" + UPDATED_CASH_OUT, "cashOut.lessThan=" + DEFAULT_CASH_OUT);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByCashOutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where cashOut is greater than
        defaultPettyCashLedgerFiltering("cashOut.greaterThan=" + SMALLER_CASH_OUT, "cashOut.greaterThan=" + DEFAULT_CASH_OUT);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance equals to
        defaultPettyCashLedgerFiltering(
            "runningBalance.equals=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.equals=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance in
        defaultPettyCashLedgerFiltering(
            "runningBalance.in=" + DEFAULT_RUNNING_BALANCE + "," + UPDATED_RUNNING_BALANCE,
            "runningBalance.in=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance is not null
        defaultPettyCashLedgerFiltering("runningBalance.specified=true", "runningBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance is greater than or equal to
        defaultPettyCashLedgerFiltering(
            "runningBalance.greaterThanOrEqual=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.greaterThanOrEqual=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance is less than or equal to
        defaultPettyCashLedgerFiltering(
            "runningBalance.lessThanOrEqual=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.lessThanOrEqual=" + SMALLER_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance is less than
        defaultPettyCashLedgerFiltering(
            "runningBalance.lessThan=" + UPDATED_RUNNING_BALANCE,
            "runningBalance.lessThan=" + DEFAULT_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByRunningBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where runningBalance is greater than
        defaultPettyCashLedgerFiltering(
            "runningBalance.greaterThan=" + SMALLER_RUNNING_BALANCE,
            "runningBalance.greaterThan=" + DEFAULT_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByLinkedAccountCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where linkedAccountCode equals to
        defaultPettyCashLedgerFiltering(
            "linkedAccountCode.equals=" + DEFAULT_LINKED_ACCOUNT_CODE,
            "linkedAccountCode.equals=" + UPDATED_LINKED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByLinkedAccountCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where linkedAccountCode in
        defaultPettyCashLedgerFiltering(
            "linkedAccountCode.in=" + DEFAULT_LINKED_ACCOUNT_CODE + "," + UPDATED_LINKED_ACCOUNT_CODE,
            "linkedAccountCode.in=" + UPDATED_LINKED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByLinkedAccountCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where linkedAccountCode is not null
        defaultPettyCashLedgerFiltering("linkedAccountCode.specified=true", "linkedAccountCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByLinkedAccountCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where linkedAccountCode contains
        defaultPettyCashLedgerFiltering(
            "linkedAccountCode.contains=" + DEFAULT_LINKED_ACCOUNT_CODE,
            "linkedAccountCode.contains=" + UPDATED_LINKED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByLinkedAccountCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where linkedAccountCode does not contain
        defaultPettyCashLedgerFiltering(
            "linkedAccountCode.doesNotContain=" + UPDATED_LINKED_ACCOUNT_CODE,
            "linkedAccountCode.doesNotContain=" + DEFAULT_LINKED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByReferenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where referenceNo equals to
        defaultPettyCashLedgerFiltering("referenceNo.equals=" + DEFAULT_REFERENCE_NO, "referenceNo.equals=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByReferenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where referenceNo in
        defaultPettyCashLedgerFiltering(
            "referenceNo.in=" + DEFAULT_REFERENCE_NO + "," + UPDATED_REFERENCE_NO,
            "referenceNo.in=" + UPDATED_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByReferenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where referenceNo is not null
        defaultPettyCashLedgerFiltering("referenceNo.specified=true", "referenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByReferenceNoContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where referenceNo contains
        defaultPettyCashLedgerFiltering("referenceNo.contains=" + DEFAULT_REFERENCE_NO, "referenceNo.contains=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllPettyCashLedgersByReferenceNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        // Get all the pettyCashLedgerList where referenceNo does not contain
        defaultPettyCashLedgerFiltering(
            "referenceNo.doesNotContain=" + UPDATED_REFERENCE_NO,
            "referenceNo.doesNotContain=" + DEFAULT_REFERENCE_NO
        );
    }

    private void defaultPettyCashLedgerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPettyCashLedgerShouldBeFound(shouldBeFound);
        defaultPettyCashLedgerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPettyCashLedgerShouldBeFound(String filter) throws Exception {
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pettyCashLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].pettyCashCode").value(hasItem(DEFAULT_PETTY_CASH_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].pettyCashVoucherNo").value(hasItem(DEFAULT_PETTY_CASH_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cashIn").value(hasItem(sameNumber(DEFAULT_CASH_IN))))
            .andExpect(jsonPath("$.[*].cashOut").value(hasItem(sameNumber(DEFAULT_CASH_OUT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))))
            .andExpect(jsonPath("$.[*].linkedAccountCode").value(hasItem(DEFAULT_LINKED_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)));

        // Check, that the count call also returns 1
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPettyCashLedgerShouldNotBeFound(String filter) throws Exception {
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPettyCashLedger() throws Exception {
        // Get the pettyCashLedger
        restPettyCashLedgerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPettyCashLedger() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        pettyCashLedgerSearchRepository.save(pettyCashLedger);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());

        // Update the pettyCashLedger
        PettyCashLedger updatedPettyCashLedger = pettyCashLedgerRepository.findById(pettyCashLedger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPettyCashLedger are not directly saved in db
        em.detach(updatedPettyCashLedger);
        updatedPettyCashLedger
            .branchCode(UPDATED_BRANCH_CODE)
            .pettyCashCode(UPDATED_PETTY_CASH_CODE)
            .date(UPDATED_DATE)
            .pettyCashVoucherNo(UPDATED_PETTY_CASH_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .cashIn(UPDATED_CASH_IN)
            .cashOut(UPDATED_CASH_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .linkedAccountCode(UPDATED_LINKED_ACCOUNT_CODE)
            .referenceNo(UPDATED_REFERENCE_NO);
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(updatedPettyCashLedger);

        restPettyCashLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pettyCashLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pettyCashLedgerDTO))
            )
            .andExpect(status().isOk());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPettyCashLedgerToMatchAllProperties(updatedPettyCashLedger);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PettyCashLedger> pettyCashLedgerSearchList = Streamable.of(pettyCashLedgerSearchRepository.findAll()).toList();
                PettyCashLedger testPettyCashLedgerSearch = pettyCashLedgerSearchList.get(searchDatabaseSizeAfter - 1);

                assertPettyCashLedgerAllPropertiesEquals(testPettyCashLedgerSearch, updatedPettyCashLedger);
            });
    }

    @Test
    @Transactional
    void putNonExistingPettyCashLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        pettyCashLedger.setId(longCount.incrementAndGet());

        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPettyCashLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pettyCashLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pettyCashLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPettyCashLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        pettyCashLedger.setId(longCount.incrementAndGet());

        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPettyCashLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pettyCashLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPettyCashLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        pettyCashLedger.setId(longCount.incrementAndGet());

        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPettyCashLedgerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pettyCashLedgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePettyCashLedgerWithPatch() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pettyCashLedger using partial update
        PettyCashLedger partialUpdatedPettyCashLedger = new PettyCashLedger();
        partialUpdatedPettyCashLedger.setId(pettyCashLedger.getId());

        partialUpdatedPettyCashLedger
            .date(UPDATED_DATE)
            .pettyCashVoucherNo(UPDATED_PETTY_CASH_VOUCHER_NO)
            .cashOut(UPDATED_CASH_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .linkedAccountCode(UPDATED_LINKED_ACCOUNT_CODE);

        restPettyCashLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPettyCashLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPettyCashLedger))
            )
            .andExpect(status().isOk());

        // Validate the PettyCashLedger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPettyCashLedgerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPettyCashLedger, pettyCashLedger),
            getPersistedPettyCashLedger(pettyCashLedger)
        );
    }

    @Test
    @Transactional
    void fullUpdatePettyCashLedgerWithPatch() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pettyCashLedger using partial update
        PettyCashLedger partialUpdatedPettyCashLedger = new PettyCashLedger();
        partialUpdatedPettyCashLedger.setId(pettyCashLedger.getId());

        partialUpdatedPettyCashLedger
            .branchCode(UPDATED_BRANCH_CODE)
            .pettyCashCode(UPDATED_PETTY_CASH_CODE)
            .date(UPDATED_DATE)
            .pettyCashVoucherNo(UPDATED_PETTY_CASH_VOUCHER_NO)
            .description(UPDATED_DESCRIPTION)
            .cashIn(UPDATED_CASH_IN)
            .cashOut(UPDATED_CASH_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE)
            .linkedAccountCode(UPDATED_LINKED_ACCOUNT_CODE)
            .referenceNo(UPDATED_REFERENCE_NO);

        restPettyCashLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPettyCashLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPettyCashLedger))
            )
            .andExpect(status().isOk());

        // Validate the PettyCashLedger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPettyCashLedgerUpdatableFieldsEquals(
            partialUpdatedPettyCashLedger,
            getPersistedPettyCashLedger(partialUpdatedPettyCashLedger)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPettyCashLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        pettyCashLedger.setId(longCount.incrementAndGet());

        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPettyCashLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pettyCashLedgerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pettyCashLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPettyCashLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        pettyCashLedger.setId(longCount.incrementAndGet());

        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPettyCashLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pettyCashLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPettyCashLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        pettyCashLedger.setId(longCount.incrementAndGet());

        // Create the PettyCashLedger
        PettyCashLedgerDTO pettyCashLedgerDTO = pettyCashLedgerMapper.toDto(pettyCashLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPettyCashLedgerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pettyCashLedgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PettyCashLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePettyCashLedger() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);
        pettyCashLedgerRepository.save(pettyCashLedger);
        pettyCashLedgerSearchRepository.save(pettyCashLedger);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the pettyCashLedger
        restPettyCashLedgerMockMvc
            .perform(delete(ENTITY_API_URL_ID, pettyCashLedger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pettyCashLedgerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPettyCashLedger() throws Exception {
        // Initialize the database
        insertedPettyCashLedger = pettyCashLedgerRepository.saveAndFlush(pettyCashLedger);
        pettyCashLedgerSearchRepository.save(pettyCashLedger);

        // Search the pettyCashLedger
        restPettyCashLedgerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + pettyCashLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pettyCashLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].pettyCashCode").value(hasItem(DEFAULT_PETTY_CASH_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].pettyCashVoucherNo").value(hasItem(DEFAULT_PETTY_CASH_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cashIn").value(hasItem(sameNumber(DEFAULT_CASH_IN))))
            .andExpect(jsonPath("$.[*].cashOut").value(hasItem(sameNumber(DEFAULT_CASH_OUT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))))
            .andExpect(jsonPath("$.[*].linkedAccountCode").value(hasItem(DEFAULT_LINKED_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)));
    }

    protected long getRepositoryCount() {
        return pettyCashLedgerRepository.count();
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

    protected PettyCashLedger getPersistedPettyCashLedger(PettyCashLedger pettyCashLedger) {
        return pettyCashLedgerRepository.findById(pettyCashLedger.getId()).orElseThrow();
    }

    protected void assertPersistedPettyCashLedgerToMatchAllProperties(PettyCashLedger expectedPettyCashLedger) {
        assertPettyCashLedgerAllPropertiesEquals(expectedPettyCashLedger, getPersistedPettyCashLedger(expectedPettyCashLedger));
    }

    protected void assertPersistedPettyCashLedgerToMatchUpdatableProperties(PettyCashLedger expectedPettyCashLedger) {
        assertPettyCashLedgerAllUpdatablePropertiesEquals(expectedPettyCashLedger, getPersistedPettyCashLedger(expectedPettyCashLedger));
    }
}
