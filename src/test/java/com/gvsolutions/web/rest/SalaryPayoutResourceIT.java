package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.SalaryPayoutAsserts.*;
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
import com.gvsolutions.domain.SalaryPayout;
import com.gvsolutions.repository.SalaryPayoutRepository;
import com.gvsolutions.repository.search.SalaryPayoutSearchRepository;
import com.gvsolutions.service.dto.SalaryPayoutDTO;
import com.gvsolutions.service.mapper.SalaryPayoutMapper;
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
 * Integration tests for the {@link SalaryPayoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalaryPayoutResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SALARY_PAYOUT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SALARY_PAYOUT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STAFF_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PAY_PERIOD = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BASE_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASE_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_BASE_SALARY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ALLOWANCES = new BigDecimal(1);
    private static final BigDecimal UPDATED_ALLOWANCES = new BigDecimal(2);
    private static final BigDecimal SMALLER_ALLOWANCES = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DEDUCTIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEDUCTIONS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEDUCTIONS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_PAY = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_PAY = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_PAY = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_PAYOUT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYOUT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYOUT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/salary-payouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/salary-payouts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SalaryPayoutRepository salaryPayoutRepository;

    @Autowired
    private SalaryPayoutMapper salaryPayoutMapper;

    @Autowired
    private SalaryPayoutSearchRepository salaryPayoutSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryPayoutMockMvc;

    private SalaryPayout salaryPayout;

    private SalaryPayout insertedSalaryPayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryPayout createEntity() {
        return new SalaryPayout()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .salaryPayoutCode(DEFAULT_SALARY_PAYOUT_CODE)
            .staffCode(DEFAULT_STAFF_CODE)
            .payPeriod(DEFAULT_PAY_PERIOD)
            .baseSalary(DEFAULT_BASE_SALARY)
            .allowances(DEFAULT_ALLOWANCES)
            .deductions(DEFAULT_DEDUCTIONS)
            .netPay(DEFAULT_NET_PAY)
            .payoutDate(DEFAULT_PAYOUT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryPayout createUpdatedEntity() {
        return new SalaryPayout()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .salaryPayoutCode(UPDATED_SALARY_PAYOUT_CODE)
            .staffCode(UPDATED_STAFF_CODE)
            .payPeriod(UPDATED_PAY_PERIOD)
            .baseSalary(UPDATED_BASE_SALARY)
            .allowances(UPDATED_ALLOWANCES)
            .deductions(UPDATED_DEDUCTIONS)
            .netPay(UPDATED_NET_PAY)
            .payoutDate(UPDATED_PAYOUT_DATE);
    }

    @BeforeEach
    void initTest() {
        salaryPayout = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSalaryPayout != null) {
            salaryPayoutRepository.delete(insertedSalaryPayout);
            salaryPayoutSearchRepository.delete(insertedSalaryPayout);
            insertedSalaryPayout = null;
        }
    }

    @Test
    @Transactional
    void createSalaryPayout() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);
        var returnedSalaryPayoutDTO = om.readValue(
            restSalaryPayoutMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryPayoutDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SalaryPayoutDTO.class
        );

        // Validate the SalaryPayout in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSalaryPayout = salaryPayoutMapper.toEntity(returnedSalaryPayoutDTO);
        assertSalaryPayoutUpdatableFieldsEquals(returnedSalaryPayout, getPersistedSalaryPayout(returnedSalaryPayout));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSalaryPayout = returnedSalaryPayout;
    }

    @Test
    @Transactional
    void createSalaryPayoutWithExistingId() throws Exception {
        // Create the SalaryPayout with an existing ID
        salaryPayout.setId(1L);
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryPayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryPayoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSalaryPayouts() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].salaryPayoutCode").value(hasItem(DEFAULT_SALARY_PAYOUT_CODE)))
            .andExpect(jsonPath("$.[*].staffCode").value(hasItem(DEFAULT_STAFF_CODE)))
            .andExpect(jsonPath("$.[*].payPeriod").value(hasItem(DEFAULT_PAY_PERIOD)))
            .andExpect(jsonPath("$.[*].baseSalary").value(hasItem(sameNumber(DEFAULT_BASE_SALARY))))
            .andExpect(jsonPath("$.[*].allowances").value(hasItem(sameNumber(DEFAULT_ALLOWANCES))))
            .andExpect(jsonPath("$.[*].deductions").value(hasItem(sameNumber(DEFAULT_DEDUCTIONS))))
            .andExpect(jsonPath("$.[*].netPay").value(hasItem(sameNumber(DEFAULT_NET_PAY))))
            .andExpect(jsonPath("$.[*].payoutDate").value(hasItem(DEFAULT_PAYOUT_DATE.toString())));
    }

    @Test
    @Transactional
    void getSalaryPayout() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get the salaryPayout
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, salaryPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salaryPayout.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.salaryPayoutCode").value(DEFAULT_SALARY_PAYOUT_CODE))
            .andExpect(jsonPath("$.staffCode").value(DEFAULT_STAFF_CODE))
            .andExpect(jsonPath("$.payPeriod").value(DEFAULT_PAY_PERIOD))
            .andExpect(jsonPath("$.baseSalary").value(sameNumber(DEFAULT_BASE_SALARY)))
            .andExpect(jsonPath("$.allowances").value(sameNumber(DEFAULT_ALLOWANCES)))
            .andExpect(jsonPath("$.deductions").value(sameNumber(DEFAULT_DEDUCTIONS)))
            .andExpect(jsonPath("$.netPay").value(sameNumber(DEFAULT_NET_PAY)))
            .andExpect(jsonPath("$.payoutDate").value(DEFAULT_PAYOUT_DATE.toString()));
    }

    @Test
    @Transactional
    void getSalaryPayoutsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        Long id = salaryPayout.getId();

        defaultSalaryPayoutFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSalaryPayoutFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSalaryPayoutFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchCode equals to
        defaultSalaryPayoutFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchCode in
        defaultSalaryPayoutFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchCode is not null
        defaultSalaryPayoutFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchCode contains
        defaultSalaryPayoutFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchCode does not contain
        defaultSalaryPayoutFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchId equals to
        defaultSalaryPayoutFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchId in
        defaultSalaryPayoutFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchId is not null
        defaultSalaryPayoutFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchId contains
        defaultSalaryPayoutFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where branchId does not contain
        defaultSalaryPayoutFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsBySalaryPayoutCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where salaryPayoutCode equals to
        defaultSalaryPayoutFiltering(
            "salaryPayoutCode.equals=" + DEFAULT_SALARY_PAYOUT_CODE,
            "salaryPayoutCode.equals=" + UPDATED_SALARY_PAYOUT_CODE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsBySalaryPayoutCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where salaryPayoutCode in
        defaultSalaryPayoutFiltering(
            "salaryPayoutCode.in=" + DEFAULT_SALARY_PAYOUT_CODE + "," + UPDATED_SALARY_PAYOUT_CODE,
            "salaryPayoutCode.in=" + UPDATED_SALARY_PAYOUT_CODE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsBySalaryPayoutCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where salaryPayoutCode is not null
        defaultSalaryPayoutFiltering("salaryPayoutCode.specified=true", "salaryPayoutCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsBySalaryPayoutCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where salaryPayoutCode contains
        defaultSalaryPayoutFiltering(
            "salaryPayoutCode.contains=" + DEFAULT_SALARY_PAYOUT_CODE,
            "salaryPayoutCode.contains=" + UPDATED_SALARY_PAYOUT_CODE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsBySalaryPayoutCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where salaryPayoutCode does not contain
        defaultSalaryPayoutFiltering(
            "salaryPayoutCode.doesNotContain=" + UPDATED_SALARY_PAYOUT_CODE,
            "salaryPayoutCode.doesNotContain=" + DEFAULT_SALARY_PAYOUT_CODE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByStaffCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where staffCode equals to
        defaultSalaryPayoutFiltering("staffCode.equals=" + DEFAULT_STAFF_CODE, "staffCode.equals=" + UPDATED_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByStaffCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where staffCode in
        defaultSalaryPayoutFiltering("staffCode.in=" + DEFAULT_STAFF_CODE + "," + UPDATED_STAFF_CODE, "staffCode.in=" + UPDATED_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByStaffCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where staffCode is not null
        defaultSalaryPayoutFiltering("staffCode.specified=true", "staffCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByStaffCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where staffCode contains
        defaultSalaryPayoutFiltering("staffCode.contains=" + DEFAULT_STAFF_CODE, "staffCode.contains=" + UPDATED_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByStaffCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where staffCode does not contain
        defaultSalaryPayoutFiltering("staffCode.doesNotContain=" + UPDATED_STAFF_CODE, "staffCode.doesNotContain=" + DEFAULT_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payPeriod equals to
        defaultSalaryPayoutFiltering("payPeriod.equals=" + DEFAULT_PAY_PERIOD, "payPeriod.equals=" + UPDATED_PAY_PERIOD);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payPeriod in
        defaultSalaryPayoutFiltering("payPeriod.in=" + DEFAULT_PAY_PERIOD + "," + UPDATED_PAY_PERIOD, "payPeriod.in=" + UPDATED_PAY_PERIOD);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payPeriod is not null
        defaultSalaryPayoutFiltering("payPeriod.specified=true", "payPeriod.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payPeriod contains
        defaultSalaryPayoutFiltering("payPeriod.contains=" + DEFAULT_PAY_PERIOD, "payPeriod.contains=" + UPDATED_PAY_PERIOD);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payPeriod does not contain
        defaultSalaryPayoutFiltering("payPeriod.doesNotContain=" + UPDATED_PAY_PERIOD, "payPeriod.doesNotContain=" + DEFAULT_PAY_PERIOD);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary equals to
        defaultSalaryPayoutFiltering("baseSalary.equals=" + DEFAULT_BASE_SALARY, "baseSalary.equals=" + UPDATED_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary in
        defaultSalaryPayoutFiltering(
            "baseSalary.in=" + DEFAULT_BASE_SALARY + "," + UPDATED_BASE_SALARY,
            "baseSalary.in=" + UPDATED_BASE_SALARY
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary is not null
        defaultSalaryPayoutFiltering("baseSalary.specified=true", "baseSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary is greater than or equal to
        defaultSalaryPayoutFiltering(
            "baseSalary.greaterThanOrEqual=" + DEFAULT_BASE_SALARY,
            "baseSalary.greaterThanOrEqual=" + UPDATED_BASE_SALARY
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary is less than or equal to
        defaultSalaryPayoutFiltering(
            "baseSalary.lessThanOrEqual=" + DEFAULT_BASE_SALARY,
            "baseSalary.lessThanOrEqual=" + SMALLER_BASE_SALARY
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary is less than
        defaultSalaryPayoutFiltering("baseSalary.lessThan=" + UPDATED_BASE_SALARY, "baseSalary.lessThan=" + DEFAULT_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByBaseSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where baseSalary is greater than
        defaultSalaryPayoutFiltering("baseSalary.greaterThan=" + SMALLER_BASE_SALARY, "baseSalary.greaterThan=" + DEFAULT_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances equals to
        defaultSalaryPayoutFiltering("allowances.equals=" + DEFAULT_ALLOWANCES, "allowances.equals=" + UPDATED_ALLOWANCES);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances in
        defaultSalaryPayoutFiltering(
            "allowances.in=" + DEFAULT_ALLOWANCES + "," + UPDATED_ALLOWANCES,
            "allowances.in=" + UPDATED_ALLOWANCES
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances is not null
        defaultSalaryPayoutFiltering("allowances.specified=true", "allowances.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances is greater than or equal to
        defaultSalaryPayoutFiltering(
            "allowances.greaterThanOrEqual=" + DEFAULT_ALLOWANCES,
            "allowances.greaterThanOrEqual=" + UPDATED_ALLOWANCES
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances is less than or equal to
        defaultSalaryPayoutFiltering(
            "allowances.lessThanOrEqual=" + DEFAULT_ALLOWANCES,
            "allowances.lessThanOrEqual=" + SMALLER_ALLOWANCES
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances is less than
        defaultSalaryPayoutFiltering("allowances.lessThan=" + UPDATED_ALLOWANCES, "allowances.lessThan=" + DEFAULT_ALLOWANCES);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByAllowancesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where allowances is greater than
        defaultSalaryPayoutFiltering("allowances.greaterThan=" + SMALLER_ALLOWANCES, "allowances.greaterThan=" + DEFAULT_ALLOWANCES);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions equals to
        defaultSalaryPayoutFiltering("deductions.equals=" + DEFAULT_DEDUCTIONS, "deductions.equals=" + UPDATED_DEDUCTIONS);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions in
        defaultSalaryPayoutFiltering(
            "deductions.in=" + DEFAULT_DEDUCTIONS + "," + UPDATED_DEDUCTIONS,
            "deductions.in=" + UPDATED_DEDUCTIONS
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions is not null
        defaultSalaryPayoutFiltering("deductions.specified=true", "deductions.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions is greater than or equal to
        defaultSalaryPayoutFiltering(
            "deductions.greaterThanOrEqual=" + DEFAULT_DEDUCTIONS,
            "deductions.greaterThanOrEqual=" + UPDATED_DEDUCTIONS
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions is less than or equal to
        defaultSalaryPayoutFiltering(
            "deductions.lessThanOrEqual=" + DEFAULT_DEDUCTIONS,
            "deductions.lessThanOrEqual=" + SMALLER_DEDUCTIONS
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions is less than
        defaultSalaryPayoutFiltering("deductions.lessThan=" + UPDATED_DEDUCTIONS, "deductions.lessThan=" + DEFAULT_DEDUCTIONS);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByDeductionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where deductions is greater than
        defaultSalaryPayoutFiltering("deductions.greaterThan=" + SMALLER_DEDUCTIONS, "deductions.greaterThan=" + DEFAULT_DEDUCTIONS);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay equals to
        defaultSalaryPayoutFiltering("netPay.equals=" + DEFAULT_NET_PAY, "netPay.equals=" + UPDATED_NET_PAY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay in
        defaultSalaryPayoutFiltering("netPay.in=" + DEFAULT_NET_PAY + "," + UPDATED_NET_PAY, "netPay.in=" + UPDATED_NET_PAY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay is not null
        defaultSalaryPayoutFiltering("netPay.specified=true", "netPay.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay is greater than or equal to
        defaultSalaryPayoutFiltering("netPay.greaterThanOrEqual=" + DEFAULT_NET_PAY, "netPay.greaterThanOrEqual=" + UPDATED_NET_PAY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay is less than or equal to
        defaultSalaryPayoutFiltering("netPay.lessThanOrEqual=" + DEFAULT_NET_PAY, "netPay.lessThanOrEqual=" + SMALLER_NET_PAY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay is less than
        defaultSalaryPayoutFiltering("netPay.lessThan=" + UPDATED_NET_PAY, "netPay.lessThan=" + DEFAULT_NET_PAY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByNetPayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where netPay is greater than
        defaultSalaryPayoutFiltering("netPay.greaterThan=" + SMALLER_NET_PAY, "netPay.greaterThan=" + DEFAULT_NET_PAY);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate equals to
        defaultSalaryPayoutFiltering("payoutDate.equals=" + DEFAULT_PAYOUT_DATE, "payoutDate.equals=" + UPDATED_PAYOUT_DATE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate in
        defaultSalaryPayoutFiltering(
            "payoutDate.in=" + DEFAULT_PAYOUT_DATE + "," + UPDATED_PAYOUT_DATE,
            "payoutDate.in=" + UPDATED_PAYOUT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate is not null
        defaultSalaryPayoutFiltering("payoutDate.specified=true", "payoutDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate is greater than or equal to
        defaultSalaryPayoutFiltering(
            "payoutDate.greaterThanOrEqual=" + DEFAULT_PAYOUT_DATE,
            "payoutDate.greaterThanOrEqual=" + UPDATED_PAYOUT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate is less than or equal to
        defaultSalaryPayoutFiltering(
            "payoutDate.lessThanOrEqual=" + DEFAULT_PAYOUT_DATE,
            "payoutDate.lessThanOrEqual=" + SMALLER_PAYOUT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate is less than
        defaultSalaryPayoutFiltering("payoutDate.lessThan=" + UPDATED_PAYOUT_DATE, "payoutDate.lessThan=" + DEFAULT_PAYOUT_DATE);
    }

    @Test
    @Transactional
    void getAllSalaryPayoutsByPayoutDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        // Get all the salaryPayoutList where payoutDate is greater than
        defaultSalaryPayoutFiltering("payoutDate.greaterThan=" + SMALLER_PAYOUT_DATE, "payoutDate.greaterThan=" + DEFAULT_PAYOUT_DATE);
    }

    private void defaultSalaryPayoutFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSalaryPayoutShouldBeFound(shouldBeFound);
        defaultSalaryPayoutShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSalaryPayoutShouldBeFound(String filter) throws Exception {
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].salaryPayoutCode").value(hasItem(DEFAULT_SALARY_PAYOUT_CODE)))
            .andExpect(jsonPath("$.[*].staffCode").value(hasItem(DEFAULT_STAFF_CODE)))
            .andExpect(jsonPath("$.[*].payPeriod").value(hasItem(DEFAULT_PAY_PERIOD)))
            .andExpect(jsonPath("$.[*].baseSalary").value(hasItem(sameNumber(DEFAULT_BASE_SALARY))))
            .andExpect(jsonPath("$.[*].allowances").value(hasItem(sameNumber(DEFAULT_ALLOWANCES))))
            .andExpect(jsonPath("$.[*].deductions").value(hasItem(sameNumber(DEFAULT_DEDUCTIONS))))
            .andExpect(jsonPath("$.[*].netPay").value(hasItem(sameNumber(DEFAULT_NET_PAY))))
            .andExpect(jsonPath("$.[*].payoutDate").value(hasItem(DEFAULT_PAYOUT_DATE.toString())));

        // Check, that the count call also returns 1
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSalaryPayoutShouldNotBeFound(String filter) throws Exception {
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSalaryPayout() throws Exception {
        // Get the salaryPayout
        restSalaryPayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalaryPayout() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryPayoutSearchRepository.save(salaryPayout);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());

        // Update the salaryPayout
        SalaryPayout updatedSalaryPayout = salaryPayoutRepository.findById(salaryPayout.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSalaryPayout are not directly saved in db
        em.detach(updatedSalaryPayout);
        updatedSalaryPayout
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .salaryPayoutCode(UPDATED_SALARY_PAYOUT_CODE)
            .staffCode(UPDATED_STAFF_CODE)
            .payPeriod(UPDATED_PAY_PERIOD)
            .baseSalary(UPDATED_BASE_SALARY)
            .allowances(UPDATED_ALLOWANCES)
            .deductions(UPDATED_DEDUCTIONS)
            .netPay(UPDATED_NET_PAY)
            .payoutDate(UPDATED_PAYOUT_DATE);
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(updatedSalaryPayout);

        restSalaryPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryPayoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salaryPayoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSalaryPayoutToMatchAllProperties(updatedSalaryPayout);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SalaryPayout> salaryPayoutSearchList = Streamable.of(salaryPayoutSearchRepository.findAll()).toList();
                SalaryPayout testSalaryPayoutSearch = salaryPayoutSearchList.get(searchDatabaseSizeAfter - 1);

                assertSalaryPayoutAllPropertiesEquals(testSalaryPayoutSearch, updatedSalaryPayout);
            });
    }

    @Test
    @Transactional
    void putNonExistingSalaryPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        salaryPayout.setId(longCount.incrementAndGet());

        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryPayoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salaryPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalaryPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        salaryPayout.setId(longCount.incrementAndGet());

        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salaryPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalaryPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        salaryPayout.setId(longCount.incrementAndGet());

        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryPayoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryPayoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSalaryPayoutWithPatch() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salaryPayout using partial update
        SalaryPayout partialUpdatedSalaryPayout = new SalaryPayout();
        partialUpdatedSalaryPayout.setId(salaryPayout.getId());

        partialUpdatedSalaryPayout.branchCode(UPDATED_BRANCH_CODE).staffCode(UPDATED_STAFF_CODE).payoutDate(UPDATED_PAYOUT_DATE);

        restSalaryPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalaryPayout))
            )
            .andExpect(status().isOk());

        // Validate the SalaryPayout in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalaryPayoutUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSalaryPayout, salaryPayout),
            getPersistedSalaryPayout(salaryPayout)
        );
    }

    @Test
    @Transactional
    void fullUpdateSalaryPayoutWithPatch() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salaryPayout using partial update
        SalaryPayout partialUpdatedSalaryPayout = new SalaryPayout();
        partialUpdatedSalaryPayout.setId(salaryPayout.getId());

        partialUpdatedSalaryPayout
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .salaryPayoutCode(UPDATED_SALARY_PAYOUT_CODE)
            .staffCode(UPDATED_STAFF_CODE)
            .payPeriod(UPDATED_PAY_PERIOD)
            .baseSalary(UPDATED_BASE_SALARY)
            .allowances(UPDATED_ALLOWANCES)
            .deductions(UPDATED_DEDUCTIONS)
            .netPay(UPDATED_NET_PAY)
            .payoutDate(UPDATED_PAYOUT_DATE);

        restSalaryPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalaryPayout))
            )
            .andExpect(status().isOk());

        // Validate the SalaryPayout in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalaryPayoutUpdatableFieldsEquals(partialUpdatedSalaryPayout, getPersistedSalaryPayout(partialUpdatedSalaryPayout));
    }

    @Test
    @Transactional
    void patchNonExistingSalaryPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        salaryPayout.setId(longCount.incrementAndGet());

        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaryPayoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salaryPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalaryPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        salaryPayout.setId(longCount.incrementAndGet());

        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salaryPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalaryPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        salaryPayout.setId(longCount.incrementAndGet());

        // Create the SalaryPayout
        SalaryPayoutDTO salaryPayoutDTO = salaryPayoutMapper.toDto(salaryPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryPayoutMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(salaryPayoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryPayout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSalaryPayout() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);
        salaryPayoutRepository.save(salaryPayout);
        salaryPayoutSearchRepository.save(salaryPayout);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the salaryPayout
        restSalaryPayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, salaryPayout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(salaryPayoutSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSalaryPayout() throws Exception {
        // Initialize the database
        insertedSalaryPayout = salaryPayoutRepository.saveAndFlush(salaryPayout);
        salaryPayoutSearchRepository.save(salaryPayout);

        // Search the salaryPayout
        restSalaryPayoutMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + salaryPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].salaryPayoutCode").value(hasItem(DEFAULT_SALARY_PAYOUT_CODE)))
            .andExpect(jsonPath("$.[*].staffCode").value(hasItem(DEFAULT_STAFF_CODE)))
            .andExpect(jsonPath("$.[*].payPeriod").value(hasItem(DEFAULT_PAY_PERIOD)))
            .andExpect(jsonPath("$.[*].baseSalary").value(hasItem(sameNumber(DEFAULT_BASE_SALARY))))
            .andExpect(jsonPath("$.[*].allowances").value(hasItem(sameNumber(DEFAULT_ALLOWANCES))))
            .andExpect(jsonPath("$.[*].deductions").value(hasItem(sameNumber(DEFAULT_DEDUCTIONS))))
            .andExpect(jsonPath("$.[*].netPay").value(hasItem(sameNumber(DEFAULT_NET_PAY))))
            .andExpect(jsonPath("$.[*].payoutDate").value(hasItem(DEFAULT_PAYOUT_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return salaryPayoutRepository.count();
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

    protected SalaryPayout getPersistedSalaryPayout(SalaryPayout salaryPayout) {
        return salaryPayoutRepository.findById(salaryPayout.getId()).orElseThrow();
    }

    protected void assertPersistedSalaryPayoutToMatchAllProperties(SalaryPayout expectedSalaryPayout) {
        assertSalaryPayoutAllPropertiesEquals(expectedSalaryPayout, getPersistedSalaryPayout(expectedSalaryPayout));
    }

    protected void assertPersistedSalaryPayoutToMatchUpdatableProperties(SalaryPayout expectedSalaryPayout) {
        assertSalaryPayoutAllUpdatablePropertiesEquals(expectedSalaryPayout, getPersistedSalaryPayout(expectedSalaryPayout));
    }
}
